package org.example.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "item_name", nullable = false, length = 150)
    private String itemName;

    @Column(length = 100)
    private String category; // Örn: "Gıda", "Tıbbi Malzeme", "Barınma"

    @Column(precision = 12, scale = 2)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(length = 20)
    private String unit; // Örn: "Adet", "KG", "Litre"

    @Column(name = "critical_threshold", precision = 10, scale = 2)
    private BigDecimal criticalThreshold; // Stok bu seviyenin altına düşerse uyarı verilir

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}