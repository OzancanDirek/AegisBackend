package org.example.Service;

import org.example.Dtos.SkillDtos.CreateSkillDto;
import org.example.Model.Skill;
import org.springframework.web.bind.annotation.PathVariable;


public interface ISkillService
{
    public Skill CreateSkill(CreateSkillDto createSkillDto);
    public void deleteSkill(@PathVariable Integer id);

}
