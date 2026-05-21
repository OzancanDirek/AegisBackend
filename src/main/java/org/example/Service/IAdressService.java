package org.example.Service;

import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.ResultAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;

import java.util.List;

public interface IAdressService
{
    List<ResultAdressDto> getAllAdress();

    ResultAdressDto createAdress(CreateAdressDto createAdressDto);

    ResultAdressDto updateAdress(int adressId, UpdateAdressDto updateAdressDto);

    void deleteAdress(int adressId);
}
