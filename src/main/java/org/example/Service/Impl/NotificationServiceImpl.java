package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Service.INotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService
{
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(String topic, String message)
    {
        messagingTemplate.convertAndSend("/topic/"+topic, message);
    }
}
