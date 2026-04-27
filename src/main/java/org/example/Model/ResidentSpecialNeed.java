package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resident_special_needs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResidentSpecialNeed
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    @ManyToOne
    @JoinColumn(name = "need_id")
    private SpecialNeeds specialNeed;

    @Column(name = "severity_level")
    private Integer severityLevel = 1; // 1-5 arası aciliyet
}