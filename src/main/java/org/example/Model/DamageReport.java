package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "damage_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DamageReport
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id")
    private Users reportedBy;

    @Column(name = "damage_score")
    private Integer damageScore; // 1-10 arası

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "report_date", updatable = false)
    private LocalDateTime reportDate;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @PrePersist
    protected void onCreate()
    {
        this.reportDate = LocalDateTime.now();
    }
}