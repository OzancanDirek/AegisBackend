package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.VolunteerDtos.CreateVolunteerDto;
import org.example.Dtos.VolunteerDtos.ResultVolunteerDto;
import org.example.Model.Volunteer;
import org.example.Service.IVolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/volunteer")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class VolunteerController
{
    private final IVolunteerService _volunteerService;

    @PostMapping
    public ResponseEntity<ResultVolunteerDto> createVolunteer(@RequestBody CreateVolunteerDto dto)
    {
        Volunteer saved = _volunteerService.createVolunteer(dto);
        ResultVolunteerDto resultDto = _volunteerService.getVolunteerById(saved.getVolunteerId());
        return ResponseEntity.ok(resultDto);
    }

    @GetMapping
    public ResponseEntity<List<ResultVolunteerDto>> getVolunteer()
    {
        List<ResultVolunteerDto> allVolunteers = _volunteerService.getAllVolunteers();
        return ResponseEntity.ok(allVolunteers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultVolunteerDto> getVolunteerById(@PathVariable Integer id)
    {
        ResultVolunteerDto resultDto = _volunteerService.getVolunteerById(id);
        return ResponseEntity.ok(resultDto);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable Integer id,
                                                   @RequestParam Boolean status)
    {
        _volunteerService.updateAvailability(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ResultVolunteerDto>> findAvailableVolunteers()
    {
        List<ResultVolunteerDto> volunteers = _volunteerService.findAvailableVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/skill")
    public ResponseEntity<List<ResultVolunteerDto>> findBySkill(@RequestParam String skillName)
    {
        List<ResultVolunteerDto> volunteers = _volunteerService.findBySkill(skillName);
        return ResponseEntity.ok(volunteers);
    }
}
