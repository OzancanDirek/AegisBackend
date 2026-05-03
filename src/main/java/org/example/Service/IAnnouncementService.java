package org.example.Service;

import org.example.Dtos.AnnouncementDto.AnnouncementRequestDto;
import org.example.Dtos.AnnouncementDto.AnnouncementResponseDto;
import org.example.Model.Announcement;

import java.util.List;

public interface IAnnouncementService
{
    AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto dto, String createdByEmail);

    List<AnnouncementResponseDto> getForRole(String role);

    List<AnnouncementResponseDto> getTop5ForRole(String role);

    void delete(String id);
}
