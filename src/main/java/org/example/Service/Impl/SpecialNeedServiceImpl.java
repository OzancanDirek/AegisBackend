package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.SpecialNeedsDto.CreateSpeicalNeeds;
import org.example.Model.SpecialNeeds;
import org.example.Repository.SpecialNeedRepository;
import org.example.Service.ISpecialNeedService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialNeedServiceImpl implements ISpecialNeedService
{
    private final SpecialNeedRepository specialNeedRepository;

    @Override
    public SpecialNeeds createSpecialNeed(CreateSpeicalNeeds createSpeicalNeeds)
    {
        if (specialNeedRepository.existsByNeedName(createSpeicalNeeds.needName))
        {
            throw new RuntimeException("Bu ihtiyaç zaten oluşturulmuş");
        }
        SpecialNeeds need = new SpecialNeeds();
        need.setNeedName(createSpeicalNeeds.needName);

        return specialNeedRepository.save(need);
    }
}
