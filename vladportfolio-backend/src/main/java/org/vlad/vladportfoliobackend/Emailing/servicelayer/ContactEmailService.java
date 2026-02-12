package org.vlad.vladportfoliobackend.Emailing.servicelayer;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.vlad.vladportfoliobackend.Emailing.datalayer.EmailRequestModel;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
@RequiredArgsConstructor
public class ContactEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.adminTo}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    public Mono<Void> sendContactEmail(EmailRequestModel request) {
        return Mono.fromCallable(() -> {
            String domain = request.getEmail().substring(request.getEmail().indexOf('@') + 1);
            if (!hasMxRecord(domain)) {
                throw new InvalidEmailDomainException("The email domain '" + domain + "' does not appear to accept mail.");
            }

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(adminEmail);
            helper.setFrom(fromEmail);
            helper.setReplyTo(request.getEmail());
            helper.setSubject("New Contact Message from " + escape(request.getName()));

            helper.setText(buildHtml(request), true);

            mailSender.send(message);
            return (Void) null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private boolean hasMxRecord(String domain) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            ctx.close();
            return attrs.get("MX") != null && attrs.get("MX").size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private String buildHtml(EmailRequestModel request) {
        return "<html>" +
                "<body>" +
                "<h2>New Contact Message</h2>" +
                "<p><strong>Name:</strong> " + escape(request.getName()) + "</p>" +
                "<p><strong>Email:</strong> " + escape(request.getEmail()) + "</p>" +
                "<p><strong>Message:</strong><br/>" + escape(request.getMessage()) + "</p>" +
                "</body>" +
                "</html>";
    }

    private String escape(String value) {
        return HtmlUtils.htmlEscape(value);
    }
}
