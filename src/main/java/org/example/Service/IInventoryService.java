package org.example.Service;

import org.example.Dtos.InventoryDto.CreateInventoryItemDto;
import org.example.Dtos.InventoryDto.InventoryItemResponseDto;
import org.example.Dtos.InventoryDto.UpdateInventoryItemDto;

import java.util.List;

public interface IInventoryService
{
    InventoryItemResponseDto createItem(CreateInventoryItemDto createInventoryItemDto);

    InventoryItemResponseDto updateItem(Integer id, UpdateInventoryItemDto dto);

    void deleteItem(Integer id);

    List<InventoryItemResponseDto> getAllItems();

    List<InventoryItemResponseDto> getItemsByWarehouse(Integer warehouseId);

    List<InventoryItemResponseDto> getCriticalItems();
}
