package org.example.Repository;

import org.example.Model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String>
{

    // Tüm duyurular but herkese acik
    @Query("SELECT a FROM Announcement a WHERE a.targetRole IS NULL OR a.targetRole = :role ORDER BY a.createdAt DESC")
    List<Announcement> findByTargetRoleOrNull(@Param("role") String role);

    // Son 5 duyuru
    @Query("SELECT a FROM Announcement a WHERE a.targetRole IS NULL OR a.targetRole = :role ORDER BY a.createdAt DESC LIMIT 5")
    List<Announcement> findTop5ByTargetRoleOrNull(@Param("role") String role);
}