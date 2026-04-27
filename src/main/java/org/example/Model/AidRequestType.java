package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aid_request_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidRequestType
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName; //"Gıda", "İlaç", "Arama Kurtarma"

    private String category; //Lojistik", "Sağlık"

    @Column(name = "base_priority_score")
    private Integer basePriorityScore = 10; // Temel öncelik puanı
}