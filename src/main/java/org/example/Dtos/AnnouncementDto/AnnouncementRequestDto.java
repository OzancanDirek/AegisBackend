package org.example.Dtos.AnnouncementDto;

import lombok.Data;

@Data
public class AnnouncementRequestDto
{
    private String title;
    private String message;
    private String type; // GENERAL, URGENT, TASK
    private String targetRole; //Nulda olabilir
}
