package org.example.Model;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Building
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Integer buildingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Adresses address;

    @Column(name = "building_age")
    private Integer buildingAge;

    @Column(name = "floor_count")
    private Integer floorCount;

    @Column(name = "structure_type", length = 100)
    private String structureType; // Betonarme, Çelik, Yığma vb.

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(name = "occupancy_status")
    private Boolean occupancyStatus = true;

    @OneToMany(mappedBy = "building")
    private List<DamageReport> damageReports;

    public enum RiskLevel
    {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}