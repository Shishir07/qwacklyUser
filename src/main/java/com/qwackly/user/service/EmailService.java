package com.qwackly.user.service;

import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.model.EmailEntity;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender emailSender;

    public List<EmailEntity> findAllByStatus(EmailStatus emailStatus){
        return emailRepository.findByStatus(emailStatus);
    }

    public void addEmailEntity(EmailEntity emailEntity){
        emailRepository.save(emailEntity);
    }

    public EmailEntity findById(Integer id){
        return emailRepository.findById(id);
    }

    public EmailEntity findByOrderId(String orderId){
        OrderEntity orderEntity = orderService.getOrder(orderId);
        return emailRepository.findByOrderEntity(orderEntity);
    }

    public void sendemail(EmailEntity emailEntity) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.qwackly.com");
        props.put("mail.smtp.port", "587");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "Developer!");
        /*model.put("location", "United States");
        model.put("sign", "Java Developer");*/

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("celebs@qwackly.com", "Allin4bharat@123");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("celebs@qwackly.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailEntity.getCustomerEmail()));
        msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("abhinav@qwackly.com,celebs@qwackly.com"));
        msg.setSubject("Qwackly Testing");
        msg.setContent("Hello Hello Helooo", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        Context context = new Context();
        context.setVariables(model);
        String html = templateEngine.process("mail-template",new Context(Locale.getDefault(), model));
        messageBodyPart.setContent(html, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void sendmail(EmailEntity emailEntity, String details) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", emailEntity.getCustomerName());
        model.put("requiredDetails",details);
        model.put("orderid", emailEntity.getOrderEntity().getId());
        model.put("orderamount",emailEntity.getOrderAmount());
        model.put("sign", "Team Qwackly");
        context.setVariables(model);

        String html = templateEngine.process("mail-template", context);

        helper.setTo(emailEntity.getCustomerEmail());
        String[] bccEmails = {"abhinav@qwackly.com","shishir@qwackly.com"};
        helper.setBcc(bccEmails);
        helper.setText(html, true);
        helper.setSubject("Qwackly Order Details");
        helper.setFrom("admin@qwackly.com");

        emailSender.send(message);
    }
}
