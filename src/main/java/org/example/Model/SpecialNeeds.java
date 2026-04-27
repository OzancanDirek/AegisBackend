package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "special_needs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialNeeds
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "need_id")
    private Integer needId;

    @Column(name = "need_name", unique = true, nullable = false, length = 100)
    private String needName;

    @Column(name = "created_by_user_id")
    private Integer createdByUserId;
}