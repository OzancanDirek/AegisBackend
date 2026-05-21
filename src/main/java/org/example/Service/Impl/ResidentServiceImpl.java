package org.example.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.Model.PriorityLevel;
import org.example.Dtos.ResidentDto.ResidentResponseDto;
import org.example.Dtos.SpecialNeedsDto.SpecialNeedsResponseDto;
import org.example.Model.Resident;
import org.example.Model.SpecialNeeds;
import org.example.Repository.ResidentRepository;
import org.example.Repository.SpecialNeedRepository;
import org.example.Service.IResidentService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResidentServiceImpl implements IResidentService
{
    private final ResidentRepository residentRepository;
    private final SpecialNeedRepository specialNeedRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    @Override
    @Transactional
    public ResidentResponseDto addSpecialNeeds(Integer residentId, List<Integer> needIds)
    {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident bulunamadı: " + residentId));

        Set<SpecialNeeds> needs = new HashSet<>();
        for (Integer id : needIds)
        {
            SpecialNeeds need = specialNeedRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Need bulunamadı: " + id));
            needs.add(need);
        }

        resident.setSpecialNeeds(needs);
        resident.setPriorityLevel(calculatePriority(resident));
        Resident saved = residentRepository.save(resident);

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "RESIDENT",
                String.valueOf(residentId),
                saved.getFullName() + " için özel ihtiyaçlar güncellendi — Öncelik: " + saved.getPriorityLevel().name()
        );

        Set<SpecialNeedsResponseDto> needDtos = saved.getSpecialNeeds().stream()
                .map(n -> new SpecialNeedsResponseDto(n.getNeedId(), n.getNeedName()))
                .collect(Collectors.toSet());

        return new ResidentResponseDto(
                saved.getResidentId(),
                saved.getFullName(),
                saved.getBirthDate(),
                saved.getGender(),
                saved.getIdentityNo(),
                needDtos,
                resident.getPriorityLevel()
        );
    }

    @Override
    @Transactional
    public List<ResidentResponseDto> getAllResidents()
    {
        return residentRepository.findAll().stream()
                .map(resident -> {
                    Set<SpecialNeedsResponseDto> needDtos = resident.getSpecialNeeds().stream()
                            .map(n -> new SpecialNeedsResponseDto(n.getNeedId(), n.getNeedName()))
                            .collect(Collectors.toSet());

                    return new ResidentResponseDto(
                            resident.getResidentId(),
                            resident.getFullName(),
                            resident.getBirthDate(),
                            resident.getGender(),
                            resident.getIdentityNo(),
                            needDtos,
                            resident.getPriorityLevel()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public PriorityLevel calculatePriority(Resident resident)
    {
        boolean hasCriticalNeed = resident.getSpecialNeeds() != null &&
                resident.getSpecialNeeds().stream()
                        .anyMatch(x -> x.getNeedName() != null &&
                                x.getNeedName().toLowerCase().contains("ilaç"));

        boolean hasAnyNeed = resident.getSpecialNeeds() != null && !resident.getSpecialNeeds().isEmpty();

        Integer age = null;
        if (resident.getBirthDate() != null)
        {
            age = java.time.Period
                    .between(resident.getBirthDate(), java.time.LocalDate.now())
                    .getYears();
        }

        if (hasCriticalNeed) return PriorityLevel.CRITICAL;
        if (age != null && age > 65 && hasAnyNeed) return PriorityLevel.HIGH;
        if (hasAnyNeed) return PriorityLevel.MEDIUM;
        return PriorityLevel.LOW;
    }
}