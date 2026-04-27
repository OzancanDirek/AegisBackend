package org.example.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.Dtos.ResidentDto.ResidentResponseDto;
import org.example.Dtos.SpecialNeedsDto.SpecialNeedsResponseDto;
import org.example.Model.Resident;
import org.example.Model.SpecialNeeds;
import org.example.Repository.ResidentRepository;
import org.example.Repository.SpecialNeedRepository;
import org.example.Service.IResidentService;
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
        Resident saved = residentRepository.save(resident);

        Set<SpecialNeedsResponseDto> needDtos = saved.getSpecialNeeds().stream()
                .map(n -> new SpecialNeedsResponseDto(n.getNeedId(), n.getNeedName()))
                .collect(Collectors.toSet());

        return new ResidentResponseDto(
                saved.getResidentId(),
                saved.getFullName(),
                saved.getBirthDate(),
                saved.getGender(),
                saved.getIdentityNo(),
                needDtos
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
                            needDtos
                    );
                })
                .collect(Collectors.toList());
    }
}