package org.example.Repository;

import org.example.Model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer>
{
    Optional<Volunteer> findByVolunteerId(Integer id);
    Optional<Volunteer> findByUser_UserId(UUID userId);
}
