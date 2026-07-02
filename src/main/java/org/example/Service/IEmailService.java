package org.example.Service;

public interface IEmailService
{
    void sendTaskAssignedEmail(String to, String volunteerName, String taskDescription);

    void sendAidRequestStatusEmail(String to, String householdName, String status);

    void sendCriticalStockEmail(String to, String warehouseName, String itemName, int quantity);
}