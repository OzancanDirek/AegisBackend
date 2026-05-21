package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.SkillDtos.CreateSkillDto;
import org.example.Model.Skill;
import org.example.Repository.SkillRepository;
import org.example.Service.ISkillService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements ISkillService
{
    private final SkillRepository _skillRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    @Override
    public Skill CreateSkill(CreateSkillDto createSkillDto)
    {
        Skill skill = new Skill();
        skill.setSkillName(createSkillDto.getSkillName());
        Skill saved = _skillRepository.save(skill);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "SKILL",
                String.valueOf(saved.getSkillId()),
                "\"" + saved.getSkillName() + "\" yeteneği oluşturuldu"
        );

        return saved;
    }

    @Override
    public void deleteSkill(Integer id)
    {
        Skill skill = _skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill bulunamadı"));

        auditService.log(
                currentUserEmail(),
                "DELETE",
                "SKILL",
                String.valueOf(id),
                "\"" + skill.getSkillName() + "\" yeteneği silindi"
        );

        _skillRepository.delete(skill);
    }
}