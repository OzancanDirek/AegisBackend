package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Service.IEmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class EmailServiceImpl implements IEmailService
{
    private final JavaMailSender mailSender;

    private void send(String to, String subject, String content)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ozandirek820@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }


    @Override
    public void sendTaskAssignedEmail(String to, String volunteerName, String taskDescription)
    {
        String subject = "Aegis — Yeni Görev Atandı";
        String body = String.format(
                "Merhaba %s,\n\nSize yeni bir görev atandı:\n\n%s\n\nGörevi görüntülemek için sisteme giriş yapınız.\n\nAegis Afet Yönetim Sistemi",
                volunteerName, taskDescription
        );
        send(to, subject, body);
    }

    @Override
    public void sendAidRequestStatusEmail(String to, String householdName, String status)
    {
        String subject = "Aegis — Yardım Talebiniz Güncellendi";
        String body = String.format(
                "Merhaba %s,\n\nYardım talebinizin durumu güncellendi: %s\n\nDetayları görüntülemek için sisteme giriş yapınız.\n\nAegis Afet Yönetim Sistemi",
                householdName, status
        );
        send(to, subject, body);
    }


    @Override
    public void sendCriticalStockEmail(String to, String warehouseName, String itemName, int quantity)
    {
        String subject = "Aegis — Kritik Stok Uyarısı";
        String body = String.format(
                "Merhaba,\n\n%s deposunda kritik stok uyarısı:\n\nÜrün: %s\nMevcut Miktar: %d\n\nLütfen stok durumunu kontrol ediniz.\n\nAegis Afet Yönetim Sistemi",
                warehouseName, itemName, quantity
        );
        send(to, subject, body);
    }
}
