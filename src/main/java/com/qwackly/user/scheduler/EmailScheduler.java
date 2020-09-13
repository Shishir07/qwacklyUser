package com.qwackly.user.scheduler;

import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.model.EmailEntity;
import com.qwackly.user.service.EmailService;
import com.qwackly.user.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Component
public class EmailScheduler {

    private EmailService emailService;
    private OrderProductService orderProductService;

    private final static String SHOUT_OUT_DETAILS = "Please reply to this email with the details, what is the occassion and whom is this wish for and some more details for this request.";
    private final static String BRAND_DETAILS = "Please reply to this email with the details of the brand to be promoted and your budget for the same. We will then connect you to the celebrity.";

    @Autowired
    public EmailScheduler(EmailService emailService, OrderProductService orderProductService){
        this.emailService=emailService;
        this.orderProductService=orderProductService;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void sendEmail() throws MessagingException {
        List<EmailEntity> emailEntityList = emailService.findAllByStatus(EmailStatus.PENDING_SEND);
        for (EmailEntity emailEntity : emailEntityList){
            if (isPaymentCompleted(emailEntity)) {
                String requiredDetails = isBrandDeal(emailEntity) ? BRAND_DETAILS : SHOUT_OUT_DETAILS;
                emailService.sendmail(emailEntity,requiredDetails);
            }
            emailEntity.setStatus(EmailStatus.SENT);
            emailService.addEmailEntity(emailEntity);
        }
    }

    private boolean isBrandDeal(EmailEntity emailEntity){
        return orderProductService.findByOrderEntity(emailEntity.getOrderEntity()).getProductEntity().getProductType().equalsIgnoreCase("brand");
    }

    private boolean isPaymentCompleted (EmailEntity emailEntity){
        return emailEntity.getOrderEntity().getState().equals(OrderStatus.PAYMENT_COMPLETED);
    }

}
