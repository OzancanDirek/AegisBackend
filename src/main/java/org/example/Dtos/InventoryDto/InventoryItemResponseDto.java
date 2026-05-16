package org.example.Dtos.InventoryDto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemResponseDto
{
    private Integer itemId;
    private Integer warehouseId;
    private String warehouseName;
    private String itemName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal criticalThreshold;
    private LocalDate expiryDate;
    private boolean isCritical; // quantity < criticalThreshold ise true
}