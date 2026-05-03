package org.example.Repository;

import org.example.Model.AidRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<AidRequest, Integer>
{

    // Household'a göre talepler
    List<AidRequest> findByHousehold_HouseholdId(Integer householdId);

    // Statüye göre talepler
    List<AidRequest> findByStatus(AidRequest.RequestStatus status);

    // Statü ve aciliyet sıralaması (admin için)
    @Query("SELECT a FROM AidRequest a ORDER BY a.urgencyLevel DESC, a.createdAt ASC")
    List<AidRequest> findAllOrderByUrgency();

    // Belirli statüleri hariç tut
    @Query("SELECT a FROM AidRequest a WHERE a.status NOT IN :statuses ORDER BY a.urgencyLevel DESC")
    List<AidRequest> findByStatusNotIn(@Param("statuses") List<AidRequest.RequestStatus> statuses);
}
