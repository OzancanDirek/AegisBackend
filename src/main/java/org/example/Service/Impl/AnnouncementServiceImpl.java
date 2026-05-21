package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AnnouncementDto.AnnouncementRequestDto;
import org.example.Dtos.AnnouncementDto.AnnouncementResponseDto;
import org.example.Model.Announcement;
import org.example.Model.Users;
import org.example.Repository.AnnouncementRepository;
import org.example.Repository.UserRepository;
import org.example.Service.IAnnouncementService;
import org.example.Service.IAuditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements IAnnouncementService
{
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final IAuditService auditService;

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

        auditService.log(
                createdByEmail,
                "CREATE",
                "ANNOUNCEMENT",
                saved.getId(),
                "\"" + saved.getTitle() + "\" duyurusu oluşturuldu" +
                        (saved.getTargetRole() != null ? " → " + saved.getTargetRole() : " → Herkese")
        );

        return mapToDto(saved);
    }

    @Override
    public void delete(String id)
    {
        announcementRepository.findById(id).ifPresent(a ->
                auditService.log(
                        "system",
                        "DELETE",
                        "ANNOUNCEMENT",
                        id,
                        "\"" + a.getTitle() + "\" duyurusu silindi"
                )
        );

        announcementRepository.deleteById(id);
    }

    @Override
    public List<AnnouncementResponseDto> getForRole(String role)
    {
        return announcementRepository.findByTargetRoleOrNull(role)
                .stream().map(this::mapToDto).toList();
    }

    @Override
    public List<AnnouncementResponseDto> getTop5ForRole(String role)
    {
        return announcementRepository.findTop5ByTargetRoleOrNull(role)
                .stream().map(this::mapToDto).toList();
    }

    private AnnouncementResponseDto mapToDto(Announcement a)
    {
        return AnnouncementResponseDto.builder()
                .id(a.getId())
                .title(a.getTitle())
                .message(a.getMessage())
                .type(a.getType().name())
                .targetRole(a.getTargetRole())
                .createdByName(a.getCreatedBy().getName() + " " + a.getCreatedBy().getSurname())
                .createdByEmail(a.getCreatedBy().getEmail())
                .createdAt(a.getCreatedAt())
                .build();
    }
}