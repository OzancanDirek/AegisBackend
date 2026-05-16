package org.example.Repository;

import org.example.Model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryItem, Integer>
{
    List<InventoryItem> findByWarehouse_WarehouseId(Integer warehouseId);
}
