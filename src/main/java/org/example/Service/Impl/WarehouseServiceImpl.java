package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements IWarehouseService
{
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public ResultWarehouseDto createWarehouse(CreateWarehouseDto dto)
    {
        Users manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager bulunamadı"));

        Adresses address = null;

        if (dto.getAddressId() != null)
        {
            address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address bulunamadı"));
        }

        Warehouse warehouse = Warehouse.builder()
                .name(dto.getName())
                .capacityM3(dto.getCapacityM3())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .manager(manager)
                .address(address)
                .build();

        warehouseRepository.save(warehouse);

        return mapToDto(warehouse);
    }

    @Override
    public List<ResultWarehouseDto> getAll()
    {
        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(Integer id)
    {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse bulunamadı"));

        warehouseRepository.delete(warehouse);
    }

    @Override
    public ResultWarehouseDto updateWarehouse(Integer id, UpdateWarehouseDto updateWarehouseDto)
    {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse bulunamadı"));

        Users manager = userRepository.findById(updateWarehouseDto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager bulunamadı"));

        if (updateWarehouseDto.getName() != null)
            warehouse.setName(updateWarehouseDto.getName());

        if (updateWarehouseDto.getCapacityM3() != null)
            warehouse.setCapacityM3(updateWarehouseDto.getCapacityM3());

        if (updateWarehouseDto.getStatus() != null)
            warehouse.setStatus(updateWarehouseDto.getStatus());

        warehouse.setManager(manager);

        if (updateWarehouseDto.getAddressId() != null)
        {
            Adresses address = addressRepository.findById(updateWarehouseDto.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address bulunamadı"));

            warehouse.setAddress(address);
        }
        warehouseRepository.save(warehouse);
        return mapToDto(warehouse);
    }


    private ResultWarehouseDto mapToDto(Warehouse warehouse)
    {
        ResultWarehouseDto dto = new ResultWarehouseDto();

        dto.setWarehouseId(warehouse.getWarehouseId());
        dto.setName(warehouse.getName());
        dto.setStatus(warehouse.getStatus());
        dto.setCapacityM3(warehouse.getCapacityM3());

        // Manager bilgisi
        if (warehouse.getManager() != null)
        {
            dto.setManagerId(warehouse.getManager().getUserId());
            dto.setManagerName(warehouse.getManager().getName());
        }

        // Address bilgisi
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
        }

        return dto;
    }
}
