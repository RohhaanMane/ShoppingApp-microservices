package com.rohan.order_service.service;

import com.rohan.order_service.dto.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
