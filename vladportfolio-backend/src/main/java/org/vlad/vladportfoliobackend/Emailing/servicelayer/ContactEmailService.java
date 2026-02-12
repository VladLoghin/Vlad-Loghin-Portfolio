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
