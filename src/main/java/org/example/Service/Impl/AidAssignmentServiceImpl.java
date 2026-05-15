package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AssignmentDto.AidAssignmentResponse;
import org.example.Dtos.AssignmentDto.AssignmentResult;
import org.example.Dtos.AssignmentDto.CreateAidAssignmentRequest;
import org.example.Dtos.AssignmentDto.UpdateAidAssignmentRequest;
import org.example.Model.AidAssignment;
import org.example.Model.AidRequest;
import org.example.Model.Team;
import org.example.Model.Volunteer;
import org.example.Model.enums.AssignmentStatus;
import org.example.Repository.AidAssignmentRepository;
import org.example.Repository.AidRequestRepository;
import org.example.Repository.TeamRepository;
import org.example.Repository.VolunteerRepository;
import org.example.Service.IAidAssignmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AidAssignmentServiceImpl implements IAidAssignmentService
{
    private final AidAssignmentRepository aidAssignmentRepository;
    private final AidRequestRepository aidRequestRepository;
    private final VolunteerRepository volunteerRepository;
    private final TeamRepository teamRepository;

    @Override
    public AssignmentResult createAssignment(CreateAidAssignmentRequest request)
    {
        if (request.getVolunteerId() == null && request.getTeamId() == null)
        {
            return AssignmentResult.builder()
                    .success(false)
                    .message("Gönüllü veya ekip seçilmesi zorunludur")
                    .build();
        }

        AidRequest aidRequest = aidRequestRepository.findById(request.getRequestId()).orElse(null);
        if (aidRequest == null)
        {
            return AssignmentResult.builder()
                    .success(false)
                    .message("Yardım talebi bulunamadi")
                    .build();
        }

        if (aidRequest.getStatus() == AidRequest.RequestStatus.ASSIGNED || aidRequest.getStatus() == AidRequest.RequestStatus.IN_PROGRESS)
        {
            return AssignmentResult.builder()
                    .success(false)
                    .message("Bu talep zaten atanmış durumda")
                    .build();
        }

        Volunteer volunteer = null;
        if (request.getVolunteerId() != null)
        {
            volunteer = volunteerRepository.findById(request.getVolunteerId()).orElse(null);
            if (volunteer == null)
            {
                return AssignmentResult.builder()
                        .success(false)
                        .message("Gönüllü bulunamadı")
                        .build();
            }
            if (Boolean.FALSE.equals(volunteer.getAvailabilityStatus()))
            {
                return AssignmentResult.builder()
                        .success(false)
                        .message("Seçilen gönüllü şu an müsait değil")
                        .build();
            }
        }

        Team team = null;
        if (request.getTeamId() != null)
        {
            team = teamRepository.findById(request.getTeamId()).orElse(null);
            if (team == null)
            {
                return AssignmentResult.builder()
                        .success(false)
                        .message("Ekip bulunamadı")
                        .build();
            }
        }

        AidAssignment assignment = AidAssignment.builder()
                .request(aidRequest)
                .assignedVolunteer(volunteer)
                .assignedTeam(team)
                .status(AssignmentStatus.PENDING)
                .notes(request.getNotes())
                .build();

        AidAssignment savedAssignment = aidAssignmentRepository.save(assignment);

        aidRequest.setStatus(AidRequest.RequestStatus.ASSIGNED);
        aidRequestRepository.save(aidRequest);

        return AssignmentResult.builder()
                .success(true)
                .message("Görev başarıyla oluşturuldu")
                .assignmentId(savedAssignment.getAssignmentId())
                .build();
    }

    @Override
    public AssignmentResult updateAssignment(UpdateAidAssignmentRequest request)
    {
        AidAssignment assignment = aidAssignmentRepository.findById(request.getAssignmentId()).orElse(null);
        if (assignment == null)
        {
            return AssignmentResult.builder()
                    .success(false)
                    .message("Görev bulunamadi")
                    .build();
        }

        if (request.getVolunteerId() != null)
        {
            Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId()).orElse(null);
            if (volunteer == null)
            {
                return AssignmentResult.builder()
                        .success(false)
                        .message("Gönüllü bulunamadı")
                        .build();
            }
            assignment.setAssignedVolunteer(volunteer);
        }

        if (request.getTeamId() != null)
        {
            Team team = teamRepository.findById(request.getTeamId()).orElse(null);
            if (team == null)
            {
                return AssignmentResult.builder()
                        .success(false)
                        .message("Ekip bulunamadı")
                        .build();
            }
            assignment.setAssignedTeam(team);
        }

        if (request.getStatus() != null)
        {
            assignment.setStatus(request.getStatus());
            if (request.getStatus() == AssignmentStatus.COMPLETED)
            {
                assignment.setCompletedAt(LocalDateTime.now());
            }
        }
        if (request.getNotes() != null)
        {
            assignment.setNotes(request.getNotes());
        }

        aidAssignmentRepository.save(assignment);

        return AssignmentResult.builder()
                .success(true)
                .message("Görev başarıyla güncellendi")
                .build();
    }

    @Override
    public AssignmentResult deleteAssignment(Integer assignmentId)
    {
        if (!aidAssignmentRepository.existsById(assignmentId))
        {
            return AssignmentResult.builder()
                    .success(false)
                    .message("Silinecek görev bulunamadı")
                    .build();
        }
        aidAssignmentRepository.deleteById(assignmentId);
        return AssignmentResult.builder()
                .success(true)
                .message("Görev başarıyla silindi")
                .build();
    }

    @Override
    public List<AidAssignmentResponse> getAllAssignments()
    {
        return aidAssignmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AidAssignmentResponse getAssignmentById(Integer assignmentId)
    {
        AidAssignment assignment = aidAssignmentRepository.findById(assignmentId)
                .orElse(null);
        if (assignment == null) return null;
        return toResponse(assignment);
    }

    @Override
    public List<AidAssignmentResponse> getAssignmentsByVolunteer(Integer volunteerId)
    {
        return aidAssignmentRepository.findByAssignedVolunteer_VolunteerId(volunteerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AidAssignmentResponse> getAssignmentsByTeam(Integer teamId)
    {
        return aidAssignmentRepository.findByAssignedTeam_TeamId(teamId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AidAssignmentResponse toResponse(AidAssignment a)
    {
        return AidAssignmentResponse.builder()
                .assignmentId(a.getAssignmentId())
                .requestId(a.getRequest() != null ? a.getRequest().getRequestId() : null)
                .requestType(a.getRequest() != null && a.getRequest().getRequestType() != null
                        ? a.getRequest().getRequestType().getTypeName() : null)
                .requestStatus(a.getRequest() != null ? a.getRequest().getStatus().name() : null)
                .householdName(a.getRequest() != null && a.getRequest().getHousehold() != null
                        ? a.getRequest().getHousehold().getHouseholdName() : null)
                .volunteerId(a.getAssignedVolunteer() != null
                        ? a.getAssignedVolunteer().getVolunteerId() : null)
                .volunteerName(a.getAssignedVolunteer() != null
                        ? a.getAssignedVolunteer().getUser().getName() + " " +
                        a.getAssignedVolunteer().getUser().getSurname() : null)
                .teamId(a.getAssignedTeam() != null
                        ? a.getAssignedTeam().getTeamId() : null)
                .teamName(a.getAssignedTeam() != null
                        ? a.getAssignedTeam().getTeamName() : null)
                .status(a.getStatus())
                .assignedAt(a.getAssignedAt())
                .completedAt(a.getCompletedAt())
                .notes(a.getNotes())
                .build();
    }
}
