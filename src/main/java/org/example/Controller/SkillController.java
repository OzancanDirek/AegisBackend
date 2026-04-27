package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.SkillDtos.CreateSkillDto;
import org.example.Model.Skill;
import org.example.Repository.SkillRepository;
import org.example.Service.ISkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skill")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class SkillController
{
    private final SkillRepository _skillRepository;
    private final ISkillService _skillService;

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills()
    {
        return ResponseEntity.ok(_skillRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody CreateSkillDto createSkillDto)
    {
        Skill createdSkill = _skillService.CreateSkill(createSkillDto);
        return ResponseEntity.ok(createdSkill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Integer id)
    {
        _skillService.deleteSkill(id);
        return ResponseEntity.ok("Skill başarıyla silindi");
    }
}