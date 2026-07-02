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
import org.example.Service.IAuditService;
import org.example.Service.IEmailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AidAssignmentServiceImpl implements IAidAssignmentService
{
    private final AidAssignmentRepository aidAssignmentRepository;
    private final AidRequestRepository aidRequestRepository;
    private final VolunteerRepository volunteerRepository;
    private final TeamRepository teamRepository;
    private final IAuditService auditService;
    private final IEmailService emailService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    @Override
    public AssignmentResult createAssignment(CreateAidAssignmentRequest request)
    {
        if (request.getVolunteerId() == null && request.getTeamId() == null)
            return AssignmentResult.builder().success(false).message("Gönüllü veya ekip seçilmesi zorunludur").build();

        AidRequest aidRequest = aidRequestRepository.findById(request.getRequestId()).orElse(null);
        if (aidRequest == null)
            return AssignmentResult.builder().success(false).message("Yardım talebi bulunamadi").build();

        if (aidRequest.getStatus() == AidRequest.RequestStatus.ASSIGNED || aidRequest.getStatus() == AidRequest.RequestStatus.IN_PROGRESS)
            return AssignmentResult.builder().success(false).message("Bu talep zaten atanmış durumda").build();

        Volunteer volunteer = null;
        if (request.getVolunteerId() != null)
        {
            volunteer = volunteerRepository.findById(request.getVolunteerId()).orElse(null);
            if (volunteer == null)
                return AssignmentResult.builder().success(false).message("Gönüllü bulunamadı").build();
            if (Boolean.FALSE.equals(volunteer.getAvailabilityStatus()))
                return AssignmentResult.builder().success(false).message("Seçilen gönüllü şu an müsait değil").build();
        }

        Team team = null;
        if (request.getTeamId() != null)
        {
            team = teamRepository.findById(request.getTeamId()).orElse(null);
            if (team == null)
                return AssignmentResult.builder().success(false).message("Ekip bulunamadı").build();
        }

        AidAssignment assignment = AidAssignment.builder()
                .request(aidRequest)
                .assignedVolunteer(volunteer)
                .assignedTeam(team)
                .status(AssignmentStatus.PENDING)
                .notes(request.getNotes())
                .deadline(request.getDeadline())
                .build();

        AidAssignment saved = aidAssignmentRepository.save(assignment);

        aidRequest.setStatus(AidRequest.RequestStatus.ASSIGNED);
        aidRequestRepository.save(aidRequest);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "ASSIGNMENT",
                String.valueOf(saved.getAssignmentId()),
                "Talep #" + request.getRequestId() + " için görev oluşturuldu"
        );
        try
        {
            if (volunteer != null && volunteer.getUser() != null && volunteer.getUser().getEmail() != null)
            {
                emailService.sendTaskAssignedEmail(
                        volunteer.getUser().getEmail(),
                        volunteer.getUser().getName(),
                        "Talep #" + request.getRequestId() + (request.getNotes() != null ? " — " + request.getNotes() : "")
                );
            }
        }
        catch (Exception e)
        {
            System.out.println("Mail gönderilemedi: " + e.getMessage());
        }

        return AssignmentResult.builder().success(true).message("Görev başarıyla oluşturuldu").assignmentId(saved.getAssignmentId()).build();
    }

    @Override
    public AssignmentResult updateAssignment(UpdateAidAssignmentRequest request)
    {
        AidAssignment assignment = aidAssignmentRepository.findById(request.getAssignmentId()).orElse(null);
        if (assignment == null)
            return AssignmentResult.builder().success(false).message("Görev bulunamadi").build();

        if (request.getVolunteerId() != null)
        {
            Volunteer volunteer = volunteerRepository.findById(request.getVolunteerId()).orElse(null);
            if (volunteer == null)
                return AssignmentResult.builder().success(false).message("Gönüllü bulunamadı").build();
            assignment.setAssignedVolunteer(volunteer);
        }

        if (request.getTeamId() != null)
        {
            Team team = teamRepository.findById(request.getTeamId()).orElse(null);
            if (team == null)
                return AssignmentResult.builder().success(false).message("Ekip bulunamadı").build();
            assignment.setAssignedTeam(team);
        }

        if (request.getStatus() != null)
        {
            assignment.setStatus(request.getStatus());
            if (request.getStatus() == AssignmentStatus.COMPLETED)
                assignment.setCompletedAt(LocalDateTime.now());
        }

        if (request.getDeadline() != null)
        {
            assignment.setDeadline(request.getDeadline());
        }

        if (request.getNotes() != null)
            assignment.setNotes(request.getNotes());

        aidAssignmentRepository.save(assignment);

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "ASSIGNMENT",
                String.valueOf(request.getAssignmentId()),
                "Görev durumu: " + (request.getStatus() != null ? request.getStatus().name() : "güncellendi")
        );

        return AssignmentResult.builder().success(true).message("Görev başarıyla güncellendi").build();
    }

    @Override
    public AssignmentResult deleteAssignment(Integer assignmentId)
    {
        if (!aidAssignmentRepository.existsById(assignmentId))
            return AssignmentResult.builder().success(false).message("Silinecek görev bulunamadı").build();

        aidAssignmentRepository.deleteById(assignmentId);

        auditService.log(
                currentUserEmail(),
                "DELETE",
                "ASSIGNMENT",
                String.valueOf(assignmentId),
                "Görev silindi"
        );

        return AssignmentResult.builder().success(true).message("Görev başarıyla silindi").build();
    }

    @Override
    public List<AidAssignmentResponse> getAllAssignments()
    {
        return aidAssignmentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public AidAssignmentResponse getAssignmentById(Integer assignmentId)
    {
        AidAssignment assignment = aidAssignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) return null;
        return toResponse(assignment);
    }

    @Override
    public List<AidAssignmentResponse> getAssignmentsByVolunteer(Integer volunteerId)
    {
        return aidAssignmentRepository.findByAssignedVolunteer_VolunteerId(volunteerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<AidAssignmentResponse> getAssignmentsByTeam(Integer teamId)
    {
        return aidAssignmentRepository.findByAssignedTeam_TeamId(teamId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private AidAssignmentResponse toResponse(AidAssignment a)
    {
        return AidAssignmentResponse.builder()
                .assignmentId(a.getAssignmentId())
                .requestId(a.getRequest() != null ? a.getRequest().getRequestId() : null)
                .requestType(a.getRequest() != null && a.getRequest().getRequestType() != null ? a.getRequest().getRequestType().getTypeName() : null)
                .requestStatus(a.getRequest() != null ? a.getRequest().getStatus().name() : null)
                .householdName(a.getRequest() != null && a.getRequest().getHousehold() != null ? a.getRequest().getHousehold().getHouseholdName() : null)
                .volunteerId(a.getAssignedVolunteer() != null ? a.getAssignedVolunteer().getVolunteerId() : null)
                .volunteerName(a.getAssignedVolunteer() != null ? a.getAssignedVolunteer().getUser().getName() + " " + a.getAssignedVolunteer().getUser().getSurname() : null)
                .teamId(a.getAssignedTeam() != null ? a.getAssignedTeam().getTeamId() : null)
                .teamName(a.getAssignedTeam() != null ? a.getAssignedTeam().getTeamName() : null)
                .status(a.getStatus())
                .assignedAt(a.getAssignedAt())
                .completedAt(a.getCompletedAt())
                .notes(a.getNotes())
                .deadline(a.getDeadline())
                .build();
    }
}