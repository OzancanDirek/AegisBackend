package org.example.Repository;

import org.example.Model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, Integer>
{
    boolean existsByAddress_AddressId(Integer addressId);
    Optional<Resident> findByUser_UserId(UUID userId);

}