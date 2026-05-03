package org.example.Dtos.AnnouncementDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnnouncementResponseDto
{
    private String id;
    private String title;
    private String message;
    private String type;
    private String targetRole;
    private String createdByName;
    private String createdByEmail;
    private LocalDateTime createdAt;
}
