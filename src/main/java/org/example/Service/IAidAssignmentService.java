package org.example.Service;

import org.example.Dtos.AssignmentDto.AidAssignmentResponse;
import org.example.Dtos.AssignmentDto.AssignmentResult;
import org.example.Dtos.AssignmentDto.CreateAidAssignmentRequest;
import org.example.Dtos.AssignmentDto.UpdateAidAssignmentRequest;

import java.util.List;

public interface IAidAssignmentService
{
    AssignmentResult createAssignment(CreateAidAssignmentRequest request);

    AssignmentResult updateAssignment(UpdateAidAssignmentRequest request);

    AssignmentResult deleteAssignment(Integer assignmentId);

    List<AidAssignmentResponse> getAllAssignments();

    AidAssignmentResponse getAssignmentById(Integer assignmentId);

    List<AidAssignmentResponse> getAssignmentsByVolunteer(Integer volunteerId);

    List<AidAssignmentResponse> getAssignmentsByTeam(Integer teamId);

}
