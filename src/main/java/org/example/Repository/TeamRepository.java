package org.example.Repository;

import org.example.Model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, Integer>
{
    boolean existsByTeamName(String teamName);

    @Query("SELECT COUNT(t) > 0 FROM Team t JOIN t.members m WHERE m.userId = :userId")
    boolean existsUserInAnyTeam(@Param("userId") UUID userId);
}
