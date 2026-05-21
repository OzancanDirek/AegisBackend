package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.InventoryDto.CreateInventoryItemDto;
import org.example.Dtos.InventoryDto.InventoryItemResponseDto;
import org.example.Dtos.InventoryDto.UpdateInventoryItemDto;
import org.example.Model.InventoryItem;
import org.example.Model.Warehouse;
import org.example.Repository.InventoryRepository;
import org.example.Repository.WarehouseRepository;
import org.example.Service.IInventoryService;
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements IInventoryService
{
    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    private InventoryItemResponseDto toDto(InventoryItem item)
    {
        boolean isCritical = item.getCriticalThreshold() != null &&
                item.getQuantity().compareTo(item.getCriticalThreshold()) < 0;

        return InventoryItemResponseDto.builder()
                .itemId(item.getItemId())
                .warehouseId(item.getWarehouse() != null ? item.getWarehouse().getWarehouseId() : null)
                .warehouseName(item.getWarehouse() != null ? item.getWarehouse().getName() : null)
                .itemName(item.getItemName())
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .criticalThreshold(item.getCriticalThreshold())
                .expiryDate(item.getExpiryDate())
                .isCritical(isCritical)
                .build();
    }

    @Override
    public InventoryItemResponseDto createItem(CreateInventoryItemDto dto)
    {
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Depo bulunamadı"));

        InventoryItem item = InventoryItem.builder()
                .warehouse(warehouse)
                .itemName(dto.getItemName())
                .category(dto.getCategory())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .criticalThreshold(dto.getCriticalThreshold())
                .expiryDate(dto.getExpiryDate())
                .build();

        InventoryItem saved = inventoryRepository.save(item);

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "INVENTORY_ITEM",
                String.valueOf(saved.getItemId()),
                warehouse.getName() + " deposuna \"" + saved.getItemName() + "\" eklendi"
        );

        return toDto(saved);
    }

    @Override
    public InventoryItemResponseDto updateItem(Integer id, UpdateInventoryItemDto dto)
    {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (dto.getItemName() != null) item.setItemName(dto.getItemName());
        if (dto.getCategory() != null) item.setCategory(dto.getCategory());
        if (dto.getQuantity() != null) item.setQuantity(dto.getQuantity());
        if (dto.getUnit() != null) item.setUnit(dto.getUnit());
        if (dto.getCriticalThreshold() != null) item.setCriticalThreshold(dto.getCriticalThreshold());
        if (dto.getExpiryDate() != null) item.setExpiryDate(dto.getExpiryDate());

        InventoryItem saved = inventoryRepository.save(item);

        auditService.log(
                currentUserEmail(),
                "UPDATE",
                "INVENTORY_ITEM",
                String.valueOf(id),
                "\"" + saved.getItemName() + "\" güncellendi — Miktar: " + saved.getQuantity() + " " + saved.getUnit()
        );

        return toDto(saved);
    }

    @Override
    public void deleteItem(Integer id)
    {
        inventoryRepository.findById(id).ifPresent(item ->
                auditService.log(
                        currentUserEmail(),
                        "DELETE",
                        "INVENTORY_ITEM",
                        String.valueOf(id),
                        "\"" + item.getItemName() + "\" silindi"
                )
        );

        inventoryRepository.deleteById(id);
    }

    @Override
    public List<InventoryItemResponseDto> getAllItems()
    {
        return inventoryRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemResponseDto> getItemsByWarehouse(Integer warehouseId)
    {
        return inventoryRepository.findByWarehouse_WarehouseId(warehouseId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemResponseDto> getCriticalItems()
    {
        return inventoryRepository.findAll().stream()
                .map(this::toDto)
                .filter(InventoryItemResponseDto::isCritical)
                .collect(Collectors.toList());
    }
}