package org.example.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Dtos.AssignmentDto.AidAssignmentResponse;
import org.example.Dtos.AssignmentDto.AssignmentResult;
import org.example.Dtos.AssignmentDto.CreateAidAssignmentRequest;
import org.example.Dtos.AssignmentDto.UpdateAidAssignmentRequest;
import org.example.Service.IAidAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AidAssignmentController
{
    private final IAidAssignmentService aidAssignmentService;

    @GetMapping
    public ResponseEntity<List<AidAssignmentResponse>> getAllAssignments()
    {
        return ResponseEntity.ok(aidAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AidAssignmentResponse> getAssignmentById(@PathVariable Integer id)
    {
        AidAssignmentResponse response = aidAssignmentService.getAssignmentById(id);
        return response != null
                ? ResponseEntity.ok(response)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<AidAssignmentResponse>> getByVolunteer(@PathVariable Integer volunteerId)
    {
        return ResponseEntity.ok(aidAssignmentService.getAssignmentsByVolunteer(volunteerId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<AidAssignmentResponse>> getByTeam(@PathVariable Integer teamId)
    {
        return ResponseEntity.ok(aidAssignmentService.getAssignmentsByTeam(teamId));
    }

    @PostMapping
    public ResponseEntity<AssignmentResult> createAssignment(@Valid @RequestBody CreateAidAssignmentRequest request)
    {
        AssignmentResult result = aidAssignmentService.createAssignment(request);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @PutMapping
    public ResponseEntity<AssignmentResult> updateAssignment(@Valid @RequestBody UpdateAidAssignmentRequest request)
    {
        AssignmentResult result = aidAssignmentService.updateAssignment(request);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AssignmentResult> deleteAssignment(@PathVariable Integer id)
    {
        AssignmentResult result = aidAssignmentService.deleteAssignment(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }
}