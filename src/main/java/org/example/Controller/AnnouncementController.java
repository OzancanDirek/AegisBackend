package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AnnouncementDto.AnnouncementRequestDto;
import org.example.Dtos.AnnouncementDto.AnnouncementResponseDto;
import org.example.Service.IAnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController
{
    private final IAnnouncementService announcementService;

    // Duyuru oluştur (Admin, Calisan)
    @PostMapping
    public ResponseEntity<AnnouncementResponseDto> createAnnouncement
    (@RequestBody AnnouncementRequestDto dto, Authentication authentication)
    {
        String email = authentication.getName();
        return ResponseEntity.ok(announcementService.createAnnouncement(dto, email));
    }

    // Role göre tüm duyurular
    @GetMapping
    public ResponseEntity<List<AnnouncementResponseDto>> getAll(
            @RequestParam String role)
    {
        return ResponseEntity.ok(announcementService.getForRole(role));
    }

    // Header zil için son 5
    @GetMapping("/top5")
    public ResponseEntity<List<AnnouncementResponseDto>> getTop5(
            @RequestParam String role)
    {
        return ResponseEntity.ok(announcementService.getTop5ForRole(role));
    }

    // Duyuru sil (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id)
    {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
