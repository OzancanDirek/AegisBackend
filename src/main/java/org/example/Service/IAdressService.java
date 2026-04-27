package org.example.Service;

import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;
import org.example.Model.Adresses;

import java.util.List;

public interface IAdressService
{
    public List<Adresses> getAllAdress();

    public Adresses createAdress(CreateAdressDto createAdressDto);

    public void deleteAdress(int adressId);

    public Adresses updateAdress(int adressId, UpdateAdressDto updateAdressDto);
}
