package org.example.Repository;

import org.example.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID>
{
    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.roleName = :roleName")
    List<Users> findAllByRoleName(@Param("roleName") String roleName);
}