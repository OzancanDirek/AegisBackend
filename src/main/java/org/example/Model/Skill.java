package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer skillId;

    @Column(name = "skill_name", unique = true, nullable = false, length = 100)
    private String skillName; // Örn: "İlk Yardım", "Enkaz Arama", "Psikolojik Destek"
}