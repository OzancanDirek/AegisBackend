package org.example.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "role_id", updatable = false, nullable = false)
    private String roleId;

    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;

    private String description;

    @PrePersist
    protected void onCreate()
    {
        if (this.roleId == null)
        {
            this.roleId = java.util.UUID.randomUUID().toString();
        }
    }
}