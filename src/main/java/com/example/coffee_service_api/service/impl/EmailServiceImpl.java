package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.exception.BadRequestException;
import com.example.coffee_service_api.service.abs.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("arman458792@gmail.com");
            helper.setTo(to);
            helper.setSubject("Your Verification Code");
            helper.setText(buildEmailTemplate(code), true);

            mailSender.send(message);
            log.info("Verification code sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new BadRequestException("Failed to send verification email");
        }
    }

    private String buildEmailTemplate(String code) {
        String template = """
                <!DOCTYPE html>
                       <html lang="en">
                       <head>
                           <meta charset="UTF-8">
                           <meta name="viewport" content="width=device-width, initial-scale=1.0">
                           <title>Verification Code</title>
                       </head>
                       <body style="margin:0; padding:0; background:#667eea; background:linear-gradient(135deg,#667eea 0%,#764ba2 100%); font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',Arial,sans-serif;">
                       <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%">
                           <tr>
                               <td align="center" style="padding:40px 20px;">
                                   <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%" style="max-width:600px; background:#ffffff; border-radius:16px; box-shadow:0 12px 40px rgba(0,0,0,0.2); overflow:hidden;">
                                       <!-- Header -->
                                       <tr>
                                           <td align="center" style="background:linear-gradient(135deg,#667eea 0%,#764ba2 100%); padding:40px 30px;">
                                               <div style="width:80px; height:80px; background:rgba(255,255,255,0.2); border-radius:50%; display:flex; align-items:center; justify-content:center; margin-bottom:20px;">
                                                   <img src="https://cdn-icons-png.flaticon.com/512/2972/2972360.png" alt="Coffee Logo" width="40" height="40" style="display:block;">
                                               </div>
                                               <h1 style="color:#ffffff; font-size:26px; margin:0; font-weight:700;">Coffee Service</h1>
                                               <p style="color:rgba(255,255,255,0.9); font-size:15px; margin:10px 0 0;">Your verification code is ready</p>
                                           </td>
                                       </tr>
                       
                                       <!-- Body -->
                                       <tr>
                                           <td style="padding:40px;">
                                               <p style="color:#4a5568; font-size:16px; margin:0 0 20px;">Hello there!</p>
                                               <p style="color:#4a5568; font-size:16px; line-height:1.6; margin:0 0 25px;">
                                                   You requested a verification code to access your Coffee Service account. Use the code below to complete your sign-in:
                                               </p>
                       
                                               <!-- Code Box -->
                                               <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%" style="margin:20px 0;">
                                                   <tr>
                                                       <td align="center" style="background:#f6f8fb; border:2px dashed #667eea; border-radius:12px; padding:25px;">
                                                           <p style="color:#718096; font-size:14px; text-transform:uppercase; margin:0 0 10px; font-weight:600;">Your Verification Code</p>
                                                           <p style="font-size:38px; font-weight:700; letter-spacing:6px; margin:0; background:linear-gradient(135deg,#667eea,#764ba2); -webkit-background-clip:text; -webkit-text-fill-color:transparent; font-family:'Courier New',monospace;">
                                                               CODE_PLACEHOLDER
                                                           </p>
                                                           <p style="color:#718096; font-size:13px; margin:10px 0 0; font-style:italic;">Click the code to copy it easily</p>
                                                       </td>
                                                   </tr>
                                               </table>
                       
                                               <!-- Info -->
                                               <table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%" style="margin:30px 0;">
                                                   <tr>
                                                       <td style="background:#fff3cd; border-left:4px solid #ffc107; padding:15px 20px; border-radius:8px;">
                                                           <p style="color:#856404; font-size:14px; margin:0; line-height:1.6;">
                                                               <strong>⚠ Important:</strong> This code will expire in <strong>5 minutes</strong>. If you didn't request this code, please ignore this email.
                                                           </p>
                                                       </td>
                                                   </tr>
                                               </table>
                       
                                               <p style="color:#4a5568; font-size:16px; line-height:1.6; margin:30px 0 0;">
                                                   Thank you for using Coffee Service!
                                               </p>
                                           </td>
                                       </tr>
                       
                                       <!-- Footer -->
                                       <tr>
                                           <td align="center" style="background:#f7fafc; padding:25px 30px; border-top:1px solid #e2e8f0;">
                                               <p style="color:#a0aec0; font-size:13px; margin:0 0 8px;">
                                                   This is an automated message from Coffee Service
                                               </p>
                                               <p style="color:#a0aec0; font-size:13px; margin:0;">
                                                   © 2025 Coffee Service. All rights reserved.
                                               </p>
                                               <div style="margin-top:15px;">
                                                   <span style="display:inline-block; width:8px; height:8px; background:#667eea; border-radius:50%; margin:0 3px;"></span>
                                                   <span style="display:inline-block; width:8px; height:8px; background:#764ba2; border-radius:50%; margin:0 3px;"></span>
                                                   <span style="display:inline-block; width:8px; height:8px; background:#667eea; border-radius:50%; margin:0 3px;"></span>
                                               </div>
                                           </td>
                                       </tr>
                                   </table>
                               </td>
                           </tr>
                       </table>
                       </body>
                       </html>
                       
                """;
        return template.replace("CODE_PLACEHOLDER", code);
    }
}
