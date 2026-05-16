package org.example.Dtos.WarehouseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultWarehouseDto
{
    public Integer warehouseId;

    private String name;

    private UUID managerId;
    public Integer addressId;
    public String addressText; // opsiyonel (şehir vs göstermek için)

    public String managerName;

    public BigDecimal capacityM3;

    public String status;
    private String city;
    private String district;
    private String neighborhood;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
