package org.example.Service;

import org.example.Dtos.WarehouseDto.CreateWarehouseDto;
import org.example.Dtos.WarehouseDto.ResultWarehouseDto;
import org.example.Dtos.WarehouseDto.UpdateWarehouseDto;
import org.example.Model.Users;

import java.util.List;

public interface IWarehouseService
{
    ResultWarehouseDto createWarehouse(CreateWarehouseDto createWarehouseDto);

    List<ResultWarehouseDto> getAll();

    void delete(Integer id);

    ResultWarehouseDto updateWarehouse(Integer id, UpdateWarehouseDto updateWarehouseDto);
    List<Users> getWarehouseManagers();
}
