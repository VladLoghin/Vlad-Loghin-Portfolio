package org.vlad.vladportfoliobackend.Emailing.servicelayer;

//import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Emailing.datalayer.EmailRequestModel;


@Service
@RequiredArgsConstructor
public class ContactEmailService {
/*
    private final JavaMailSender mailSender;

    @Value("${app.mail.adminTo}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    public void sendContactEmail(EmailRequestModel request){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


            helper.setTo(adminEmail);
            helper.setFrom(fromEmail);
            helper.setReplyTo(request.getEmail());
            helper.setSubject("New Contact Message from " + request.getName());

            helper.setText(buildHtml(request), true);

            mailSender.send(message);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtml(EmailRequestModel request){
        return "<html>" +
                "<body>" +
                "<h2>New Contact Message</h2>" +
                "<p><strong>Name:</strong> " + request.getName() + "</p>" +
                "<p><strong>Email:</strong> " + request.getEmail() + "</p>" +
                "<p><strong>Message:</strong><br/>" + request.getMessage() + "</p>" +
                "</body>" +
                "</html>"
                .formatted(request.getName())
                .formatted(request.getEmail())
                .formatted(request.getMessage());
    }

    private String escape(String value) {
        return org.springframework.web.util.HtmlUtils.htmlEscape(value);
    }

 */
}
