package org.example.Dtos.InventoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryItemDto
{
    private Integer warehouseId;
    private String itemName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal criticalThreshold;
    private LocalDate expiryDate;
}
