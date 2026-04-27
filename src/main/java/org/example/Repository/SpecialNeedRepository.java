package org.example.Repository;

import org.example.Model.SpecialNeeds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialNeedRepository extends JpaRepository<SpecialNeeds, Integer>
{
    Boolean existsByNeedName(String needName);
}
