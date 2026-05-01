package org.example.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role
{

    @Id
    @GeneratedValue
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "role_id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private UUID roleId;

    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;

    private String description;
}