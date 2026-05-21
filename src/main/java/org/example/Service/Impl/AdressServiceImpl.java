package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.ResultAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;
import org.example.Model.Adresses;
import org.example.Repository.AddressRepository;
import org.example.Repository.ResidentRepository;
import org.example.Service.IAdressService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdressServiceImpl implements IAdressService
{
    public final AddressRepository addressRepository;
    public final ResidentRepository residentRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    private ResultAdressDto mapToDto(Adresses a)
    {
        ResultAdressDto dto = new ResultAdressDto();
        dto.addressId = a.getAddressId();
        dto.city = a.getCity();
        dto.district = a.getDistrict();
        dto.neighborhood = a.getNeighborhood();
        dto.street = a.getStreet();
        dto.buildingNo = a.getBuildingNo();
        dto.apartmentNo = a.getApartmentNo();
        dto.latitude = a.getLatitude();
        dto.longitude = a.getLongitude();
        return dto;
    }

    @Override
    public List<ResultAdressDto> getAllAdress()
    {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ResultAdressDto createAdress(CreateAdressDto dto)
    {
        boolean exists = addressRepository.existsByCityAndDistrictAndStreet(
                dto.getCity(), dto.getDistrict(), dto.getStreet());
        if (exists)
            throw new RuntimeException("Adres zaten mevcut");

        Adresses adresses = new Adresses();
        adresses.setCity(dto.getCity());
        adresses.setDistrict(dto.getDistrict());
        adresses.setNeighborhood(dto.getNeighborhood());
        adresses.setStreet(dto.getStreet());
        adresses.setBuildingNo(dto.getBuildingNo());
        adresses.setApartmentNo(dto.getApartmentNo());
        adresses.setLatitude(dto.getLatitude());
        adresses.setLongitude(dto.getLongitude());

        Adresses saved = addressRepository.save(adresses);

        auditService.log(
                currentUserEmail(), "CREATE", "ADDRESS",
                String.valueOf(saved.getAddressId()),
                saved.getCity() + " / " + saved.getDistrict() + " / " + saved.getNeighborhood()
        );

        return mapToDto(saved);
    }

    @Override
    public void deleteAdress(int adressId)
    {
        boolean used = residentRepository.existsByAddress_AddressId(adressId);
        if (used)
            throw new RuntimeException("Bu adres bir resident tarafından kullanılıyor");

        auditService.log(currentUserEmail(), "DELETE", "ADDRESS", String.valueOf(adressId), "Adres silindi");

        addressRepository.deleteById(adressId);
    }

    @Override
    public ResultAdressDto updateAdress(int adressId, UpdateAdressDto dto)
    {
        Adresses adresses = addressRepository.findById(adressId)
                .orElseThrow(() -> new RuntimeException("Bu adres bulunamadı"));

        adresses.setCity(dto.getCity());
        adresses.setDistrict(dto.getDistrict());
        adresses.setNeighborhood(dto.getNeighborhood());
        adresses.setStreet(dto.getStreet());
        adresses.setBuildingNo(dto.getBuildingNo());
        adresses.setApartmentNo(dto.getApartmentNo());
        adresses.setLatitude(dto.getLatitude());
        adresses.setLongitude(dto.getLongitude());

        Adresses saved = addressRepository.save(adresses);

        auditService.log(
                currentUserEmail(), "UPDATE", "ADDRESS",
                String.valueOf(adressId),
                saved.getCity() + " / " + saved.getDistrict() + " / " + saved.getNeighborhood()
        );

        return mapToDto(saved);
    }
}