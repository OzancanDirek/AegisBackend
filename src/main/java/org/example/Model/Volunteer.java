package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "volunteers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    private Integer volunteerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private Users user;
    @Column(name = "availability_status")
    private Boolean availabilityStatus = true;

    @Column(name = "transport_type", length = 50)
    private String transportType; // Örn: "Motosiklet", "4x4 Araç", "Yaya"

    @Column(name = "max_distance_km")
    private Integer maxDistanceKm;

    @ManyToMany
    @JoinTable(
            name = "volunteer_skills",
            joinColumns = @JoinColumn(name = "volunteer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Adresses address;
}