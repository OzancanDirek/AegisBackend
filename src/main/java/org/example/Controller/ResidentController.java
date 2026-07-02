package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.ResidentDto.ResidentResponseDto;
import org.example.Service.IResidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
@RequiredArgsConstructor
public class ResidentController
{

    private final IResidentService residentService;

    @PostMapping("/{residentId}/special-needs")
    public ResponseEntity<ResidentResponseDto> addSpecialNeeds(@PathVariable Integer residentId, @RequestBody List<Integer> needIds)
    {
        return ResponseEntity.ok(residentService.addSpecialNeeds(residentId, needIds));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResidentResponseDto>> getAllResidents()
    {
        return ResponseEntity.ok(residentService.getAllResidents());
    }
}