package org.example.Model;

import jakarta.persistence.*;
import lombok.*;
import org.example.Model.AidRequest;
import org.example.Model.Team;
import org.example.Model.Volunteer;
import org.example.Model.enums.AssignmentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "aid_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidAssignment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Integer assignmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private AidRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_volunteer_id")
    private Volunteer assignedVolunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_team_id")
    private Team assignedTeam;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AssignmentStatus status = AssignmentStatus.PENDING;

    @PrePersist
    protected void onCreate()
    {
        this.assignedAt = LocalDateTime.now();
    }
}