package org.example.Repository;

import org.example.Model.AidRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AidRequestRepository extends JpaRepository<AidRequest, Integer>
{
}