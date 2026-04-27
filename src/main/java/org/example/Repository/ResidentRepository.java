package org.example.Repository;

import org.example.Model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Integer>
{
    boolean existsByAddress_AddressId(Integer addressId);
}