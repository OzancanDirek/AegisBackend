package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;
import org.example.Model.Adresses;
import org.example.Repository.AddressRepository;
import org.example.Repository.ResidentRepository;
import org.example.Service.IAdressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdressServiceImpl implements IAdressService
{
    public final AddressRepository addressRepository;
    public final ResidentRepository residentRepository;


    @Override
    public List<Adresses> getAllAdress()
    {
        return addressRepository.findAll();
    }

    @Override
    public Adresses createAdress(CreateAdressDto createAdressDto)
    {
        boolean exists = addressRepository
                .existsByCityAndDistrictAndStreet(
                        createAdressDto.getCity(),
                        createAdressDto.getDistrict(),
                        createAdressDto.getStreet()
                );
        if (exists)
        {
            throw new RuntimeException("zaten mevcut");
        }
        Adresses adresses = new Adresses();

        adresses.setCity(createAdressDto.getCity());
        adresses.setDistrict(createAdressDto.getDistrict());
        adresses.setNeighborhood(createAdressDto.getNeighborhood());
        adresses.setStreet(createAdressDto.getStreet());
        adresses.setBuildingNo(createAdressDto.getBuildingNo());
        adresses.setApartmentNo(createAdressDto.getApartmentNo());
        adresses.setLatitude(createAdressDto.getLatitude());
        adresses.setLongitude(createAdressDto.getLongitude());

        return addressRepository.save(adresses);

    }

    @Override
    public void deleteAdress(int adressId)
    {
        boolean used = residentRepository.existsByAddress_AddressId(adressId);

        if (used)
        {
            throw new RuntimeException("Bu adres bir resident tarafından kullanılıyor");
        }

        addressRepository.deleteById(adressId);
    }

    @Override
    public Adresses updateAdress(int adressId, UpdateAdressDto updateAdressDto)
    {
        Adresses adresses = addressRepository.findById(adressId)
                .orElseThrow(() -> new RuntimeException("Bu adres bulunamadı"));

        adresses.setCity(updateAdressDto.getCity());
        adresses.setDistrict(updateAdressDto.getDistrict());
        adresses.setNeighborhood(updateAdressDto.getNeighborhood());
        adresses.setStreet(updateAdressDto.getStreet());
        adresses.setBuildingNo(updateAdressDto.getBuildingNo());
        adresses.setApartmentNo(updateAdressDto.getApartmentNo());
        adresses.setLatitude(updateAdressDto.getLatitude());
        adresses.setLongitude(updateAdressDto.getLongitude());
        return addressRepository.save(adresses);
    }
}