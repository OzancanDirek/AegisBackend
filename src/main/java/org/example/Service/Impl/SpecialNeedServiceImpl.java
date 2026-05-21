package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.SpecialNeedsDto.CreateSpeicalNeeds;
import org.example.Model.SpecialNeeds;
import org.example.Repository.SpecialNeedRepository;
import org.example.Service.ISpecialNeedService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialNeedServiceImpl implements ISpecialNeedService
{
    private final SpecialNeedRepository specialNeedRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    @Override
    public SpecialNeeds createSpecialNeed(CreateSpeicalNeeds createSpeicalNeeds)
    {
        if (specialNeedRepository.existsByNeedName(createSpeicalNeeds.needName))
            throw new RuntimeException("Bu ihtiyaç zaten oluşturulmuş");

        SpecialNeeds need = new SpecialNeeds();
        need.setNeedName(createSpeicalNeeds.needName);
        SpecialNeeds saved = specialNeedRepository.save(need);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "SPECIAL_NEED",
                String.valueOf(saved.getNeedId()),
                "\"" + saved.getNeedName() + "\" özel ihtiyacı oluşturuldu"
        );

        return saved;
    }
}