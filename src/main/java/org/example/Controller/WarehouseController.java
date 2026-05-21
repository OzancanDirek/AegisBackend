package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Dtos.WarehouseDto.CreateWarehouseDto;
import org.example.Dtos.WarehouseDto.ResultWarehouseDto;
import org.example.Dtos.WarehouseDto.UpdateWarehouseDto;
import org.example.Service.IWarehouseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class WarehouseController
{
    private final IWarehouseService warehouseService;

    @PostMapping
    public ResultWarehouseDto createWarehouse(@RequestBody CreateWarehouseDto createWarehouseDto)
    {
        return warehouseService.createWarehouse(createWarehouseDto);
    }

    @GetMapping("getAllWarehouse")
    public List<ResultWarehouseDto> getAll()
    {
        return warehouseService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id)
    {
        warehouseService.delete(id);
    }

    @PutMapping("/{id}")
    public ResultWarehouseDto updateWarehouse(@PathVariable Integer id, @RequestBody UpdateWarehouseDto updateWarehouseDto)
    {
        return warehouseService.updateWarehouse(id, updateWarehouseDto);
    }

    @GetMapping("/warehouse-managers")
    public List<UserResponseDto> getWarehouseManagers()
    {
        return warehouseService.getWarehouseManagers();
    }
}
