package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volunteer_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerSkill
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level")
    private SkillLevel skillLevel = SkillLevel.BEGINNER;

    public enum SkillLevel
    {
        BEGINNER, INTERMEDIATE, EXPERT
    }
}