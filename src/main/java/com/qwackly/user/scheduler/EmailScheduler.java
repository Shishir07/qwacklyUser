package com.qwackly.user.scheduler;

import com.qwackly.user.aspects.LoggerWrapper;
import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.model.EmailEntity;
import com.qwackly.user.service.EmailService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Component
public class EmailScheduler {

    private EmailService emailService;
    private OrderService orderService;
    private PaymentService paymentService;
    private LoggerWrapper logger;

    @Autowired
    public EmailScheduler(EmailService emailService, OrderService orderService, PaymentService paymentService, LoggerWrapper logger){
        this.emailService=emailService;
        this.orderService=orderService;
        this.paymentService=paymentService;
        this.logger=logger;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void sendEmail() throws MessagingException {
        List<EmailEntity> emailEntityList = emailService.findAllByStatus(EmailStatus.PENDING_SEND);
        for (EmailEntity emailEntity : emailEntityList){
            emailService.sendmail(emailEntity);
        }
    }
}
