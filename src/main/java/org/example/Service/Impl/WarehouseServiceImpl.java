package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Dtos.WarehouseDto.CreateWarehouseDto;
import org.example.Dtos.WarehouseDto.ResultWarehouseDto;
import org.example.Dtos.WarehouseDto.UpdateWarehouseDto;
import org.example.Model.Adresses;
import org.example.Model.Users;
import org.example.Model.Warehouse;
import org.example.Repository.AddressRepository;
import org.example.Repository.UserRepository;
import org.example.Repository.WarehouseRepository;
import org.example.Service.IWarehouseService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class WarehouseServiceImpl implements IWarehouseService
{
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    @Override
    public ResultWarehouseDto createWarehouse(CreateWarehouseDto dto)
    {
        Users manager = null;
        if (dto.getManagerId() != null)
            manager = userRepository.findById(dto.getManagerId()).orElse(null);

        Adresses address = null;
        if (dto.getAddressId() != null)
        {
            address = addressRepository.findById(dto.getAddressId()).orElse(null);
        }
        else if (dto.getCity() != null)
        {
            address = Adresses.builder()
                    .city(dto.getCity())
                    .district(dto.getDistrict() != null ? dto.getDistrict() : "")
                    .neighborhood(dto.getNeighborhood() != null ? dto.getNeighborhood() : "")
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .build();
            address = addressRepository.save(address);
        }

        Warehouse warehouse = Warehouse.builder()
                .name(dto.getName())
                .capacityM3(dto.getCapacityM3())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .manager(manager)
                .address(address)
                .build();

        Warehouse saved = warehouseRepository.save(warehouse);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "WAREHOUSE",
                String.valueOf(saved.getWarehouseId()),
                "\"" + saved.getName() + "\" deposu oluşturuldu" +
                        (address != null ? " — " + address.getCity() + "/" + address.getDistrict() : "")
        );

        return mapToDto(saved);
    }

    @Override
    public List<ResultWarehouseDto> getAll()
    {
        return warehouseRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public void delete(Integer id)
    {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse bulunamadı"));

        auditService.log(
                currentUserEmail(),
                "DELETE",
                "WAREHOUSE",
                String.valueOf(id),
                "\"" + warehouse.getName() + "\" deposu silindi"
        );

        warehouseRepository.delete(warehouse);
    }

    @Override
    public ResultWarehouseDto updateWarehouse(Integer id, UpdateWarehouseDto dto)
    {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse bulunamadı"));

        if (dto.getName() != null) warehouse.setName(dto.getName());
        if (dto.getCapacityM3() != null) warehouse.setCapacityM3(dto.getCapacityM3());
        if (dto.getStatus() != null) warehouse.setStatus(dto.getStatus());

        if (dto.getManagerId() != null)
            userRepository.findById(dto.getManagerId()).ifPresent(warehouse::setManager);

        updateAddress(warehouse, dto);

        warehouseRepository.save(warehouse);

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "WAREHOUSE",
                String.valueOf(id),
                "\"" + warehouse.getName() + "\" deposu güncellendi — Durum: " + warehouse.getStatus()
        );

        return mapToDto(warehouse);
    }

    private void updateAddress(Warehouse warehouse, UpdateWarehouseDto dto)
    {
        if (dto.getAddressId() != null)
        {
            addressRepository.findById(dto.getAddressId()).ifPresent(warehouse::setAddress);
            return;
        }

        Adresses address = warehouse.getAddress() != null
                ? warehouse.getAddress()
                : new Adresses();

        Optional.ofNullable(dto.getCity()).ifPresent(address::setCity);
        Optional.ofNullable(dto.getDistrict()).ifPresent(address::setDistrict);
        Optional.ofNullable(dto.getNeighborhood()).ifPresent(address::setNeighborhood);
        Optional.ofNullable(dto.getLatitude()).ifPresent(address::setLatitude);
        Optional.ofNullable(dto.getLongitude()).ifPresent(address::setLongitude);

        warehouse.setAddress(addressRepository.save(address));
    }

    private ResultWarehouseDto mapToDto(Warehouse warehouse)
    {
        ResultWarehouseDto dto = new ResultWarehouseDto();
        dto.setWarehouseId(warehouse.getWarehouseId());
        dto.setName(warehouse.getName());
        dto.setStatus(warehouse.getStatus());
        dto.setCapacityM3(warehouse.getCapacityM3());

        if (warehouse.getManager() != null)
        {
            dto.setManagerId(warehouse.getManager().getUserId());
            dto.setManagerName(warehouse.getManager().getName());
        }

        if (warehouse.getAddress() != null)
        {
            dto.setAddressId(warehouse.getAddress().getAddressId());
            dto.setCity(warehouse.getAddress().getCity());
            dto.setDistrict(warehouse.getAddress().getDistrict());
            dto.setNeighborhood(warehouse.getAddress().getNeighborhood());
            dto.setAddressText(
                    warehouse.getAddress().getCity() + " / " +
                            warehouse.getAddress().getDistrict() + " / " +
                            warehouse.getAddress().getNeighborhood()
            );
            dto.setLatitude(warehouse.getAddress().getLatitude());
            dto.setLongitude(warehouse.getAddress().getLongitude());
        }

        return dto;
    }

    public List<UserResponseDto> getWarehouseManagers()
    {
        return userRepository.findByRoleNameWareHouseManager("WAREHOUSE_MANAGER")
                .stream()
                .map(u -> UserResponseDto.builder()
                        .userId(u.getUserId())
                        .name(u.getName())
                        .surname(u.getSurname())
                        .email(u.getEmail())
                        .build())
                .toList();
    }
}