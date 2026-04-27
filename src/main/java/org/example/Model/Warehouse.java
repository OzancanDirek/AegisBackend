package org.example.Model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Adresses address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Users manager; // Depodan sorumlu yetkili

    @Column(name = "capacity_m3", precision = 10, scale = 2)
    private BigDecimal capacityM3;

    @Column(length = 50)
    private String status = "ACTIVE";

    // Depodaki tüm ürünleri listelemek için One-to-Many
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<InventoryItem> items;
}