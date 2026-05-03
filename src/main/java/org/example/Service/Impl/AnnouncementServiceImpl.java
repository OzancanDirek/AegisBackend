package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AnnouncementDto.AnnouncementRequestDto;
import org.example.Dtos.AnnouncementDto.AnnouncementResponseDto;
import org.example.Model.Announcement;
import org.example.Model.Users;
import org.example.Repository.AnnouncementRepository;
import org.example.Repository.UserRepository;
import org.example.Service.IAnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements IAnnouncementService
{
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Override
    public AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto dto, String createdByEmail)
    {
        Users user = userRepository.findByEmail(createdByEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Announcement announcement = Announcement.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .type(Announcement.AnnouncementType.valueOf(dto.getType()))
                .targetRole(dto.getTargetRole() == null || dto.getTargetRole().isBlank() ? null : dto.getTargetRole())
                .createdBy(user)
                .build();

        Announcement saved = announcementRepository.save(announcement);
        return mapToDto(saved);
    }

    @Override
    public List<AnnouncementResponseDto> getForRole(String role)
    {
        return announcementRepository.findByTargetRoleOrNull(role)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<AnnouncementResponseDto> getTop5ForRole(String role)
    {
        return announcementRepository.findTop5ByTargetRoleOrNull(role)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(String id)
    {
        announcementRepository.deleteById(id);
    }

    private AnnouncementResponseDto mapToDto(Announcement announcement)
    {
        return AnnouncementResponseDto.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .message(announcement.getMessage())
                .type(announcement.getType().name())
                .targetRole(announcement.getTargetRole())
                .createdByName(announcement.getCreatedBy().getName() + " " + announcement.getCreatedBy().getSurname())
                .createdByEmail(announcement.getCreatedBy().getEmail())
                .createdAt(announcement.getCreatedAt())
                .build();
    }
}