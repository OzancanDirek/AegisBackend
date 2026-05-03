package org.example.Repository;

import org.example.Model.AidRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AidRequestTypeRepository extends JpaRepository<AidRequestType, Integer>
{
    List<AidRequestType> findByCategory(String category);
}
