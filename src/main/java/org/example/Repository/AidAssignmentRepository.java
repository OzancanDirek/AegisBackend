package org.example.Repository;

import org.example.Model.AidAssignment;
import org.example.Model.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AidAssignmentRepository extends JpaRepository<AidAssignment, Integer>
{

    List<AidAssignment> findByAssignedVolunteer_VolunteerId(Integer volunteerId);

    List<AidAssignment> findByAssignedTeam_TeamId(Integer teamId);

    List<AidAssignment> findByDeadlineBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, AssignmentStatus status);}