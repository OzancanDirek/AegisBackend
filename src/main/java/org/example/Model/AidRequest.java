package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aid_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidRequest
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id")
    private HouseHold household;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private AidRequestType requestType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "urgency_level")
    private Integer urgencyLevel = 1; // 1-5 arası aciliyet

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }

    public enum RequestStatus
    {
        PENDING, VERIFIED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}