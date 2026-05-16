package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.InventoryDto.CreateInventoryItemDto;
import org.example.Dtos.InventoryDto.InventoryItemResponseDto;
import org.example.Dtos.InventoryDto.UpdateInventoryItemDto;
import org.example.Service.IInventoryService;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class InventoryController
{
    private final IInventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryItemResponseDto> create(@RequestBody CreateInventoryItemDto dto)
    {
        return ResponseEntity.ok(inventoryService.createItem(dto));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<InventoryItemResponseDto> update(
            @PathVariable Integer itemId,
            @RequestBody UpdateInventoryItemDto dto)
    {
        return ResponseEntity.ok(inventoryService.updateItem(itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Integer itemId)
    {
        inventoryService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDto>> getAll()
    {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryItemResponseDto>> getByWarehouse(@PathVariable Integer warehouseId)
    {
        return ResponseEntity.ok(inventoryService.getItemsByWarehouse(warehouseId));
    }

    @GetMapping("/critical")
    public ResponseEntity<List<InventoryItemResponseDto>> getCritical()
    {
        return ResponseEntity.ok(inventoryService.getCriticalItems());
    }
}
