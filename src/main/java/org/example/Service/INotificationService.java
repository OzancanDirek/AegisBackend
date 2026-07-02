package org.example.Service;

public interface INotificationService
{
    void sendNotification(String topic, String message);

    void sendGlobalNotification(String message);
}
