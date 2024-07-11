package com.rohan.inventory_service.service;

import com.rohan.inventory_service.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);
}
