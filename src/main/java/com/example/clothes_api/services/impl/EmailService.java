package com.example.clothes_api.services.impl;

import com.example.clothes_api.dto.MailRequest;
import com.example.clothes_api.dto.MailResponse;
import com.example.clothes_api.exception.UsernameNotFoundException;
import com.example.clothes_api.repository.AccountRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender sender;
    private final Configuration config;
    private final RedisService redisService;
    private final AccountRepository accountRepository;

    @Scheduled(fixedDelay = 50000)
    public MailResponse sendEmail(MailRequest request, Map<String, Object> model){
        accountRepository.findByEmail(request.getTo())
                .orElseThrow(
                        ()->new UsernameNotFoundException("User not found")
                );

        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();

        Random random=new Random();
        int otp=random.nextInt(1000000);
        String otpString=String.format("%06d",otp);
        redisService.saveOtp(request.getTo(),otpString);
        model.put("otp",otpString);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);


            Template t = config.getTemplate("email-template.ftl");


            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setFrom(request.getFrom());
            helper.setSubject(request.getSubject());
            helper.setText(html, true);


            sender.send(message);

            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(true);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail send failure: " + e.getMessage());
            response.setStatus(false);
        }
        return response;
    }

    public String verifyOtp(String email, String otp) {
        accountRepository.findByEmail(email)
                .orElseThrow(
                        ()->new UsernameNotFoundException("User not found")
                );
        boolean checkOtp =redisService.checkOtp(email,otp);
        if(checkOtp){
            return "OTP verified successfully";
        }
        return "Invalid OTP";
    }
}
