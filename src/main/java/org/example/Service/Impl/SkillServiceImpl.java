package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.SkillDtos.CreateSkillDto;
import org.example.Model.Skill;
import org.example.Repository.SkillRepository;
import org.example.Service.ISkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements ISkillService
{
    private final SkillRepository _skillRepository;

    @Override
    public Skill CreateSkill(CreateSkillDto createSkillDto)
    {
        Skill skill = new Skill();
        skill.setSkillName(createSkillDto.getSkillName());
        return _skillRepository.save(skill);
    }

    @Override
    public void deleteSkill(Integer id)
    {
        Skill skill = _skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill bulunamadı"));

        _skillRepository.delete(skill);
    }
}
