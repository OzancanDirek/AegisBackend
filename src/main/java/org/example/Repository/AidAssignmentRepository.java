package org.example.Repository;

import org.example.Model.AidAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AidAssignmentRepository extends JpaRepository<AidAssignment, Integer>
{

    List<AidAssignment> findByAssignedVolunteer_VolunteerId(Integer volunteerId);

    List<AidAssignment> findByAssignedTeam_TeamId(Integer teamId);
}