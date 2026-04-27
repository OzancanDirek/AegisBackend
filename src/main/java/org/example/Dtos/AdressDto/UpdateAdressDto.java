package org.example.Dtos.AdressDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdressDto
{
    public Integer addressId;

    public String city;
    public String district;
    public String neighborhood;
    public String street;

    public String buildingNo;
    public String apartmentNo;

    public BigDecimal latitude;
    public BigDecimal longitude;
}
