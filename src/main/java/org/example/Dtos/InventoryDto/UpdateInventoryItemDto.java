package org.example.Dtos.InventoryDto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryItemDto
{
    private Integer itemId;
    private String itemName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal criticalThreshold;
    private LocalDate expiryDate;
}
