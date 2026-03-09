package com.talk.social.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.talk.social.notification_service.config.RabbitMQConfig;
import com.talk.social.notification_service.dto.NotificationEvent;

import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationConsumer {

	private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	public NotificationConsumer(JavaMailSender mailSender, TemplateEngine templateEngine) {
		super();
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleNotification(NotificationEvent event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            // 1. Prepare Thymeleaf Context
            Context context = new Context();
            context.setVariable("recipient", event.recipientUsername());
            context.setVariable("actor", event.actorUsername());
            context.setVariable("action", event.message());
            context.setVariable("postId", event.postId());

            // 2. Process Template into String
            String htmlContent = templateEngine.process("notification-email", context);

            // 3. Set Email Details
            helper.setFrom("no-reply@socialtalk.com");
            helper.setTo(event.recipientUsername());
            helper.setSubject("You have a new notification!");
            helper.setText(htmlContent, true); // 'true' enables HTML mode

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send HTML email", e);
        }
        
        // Here you would integrate with JavaMailSender or a WebSocket
    }
}