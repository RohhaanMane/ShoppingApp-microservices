package com.rohan.order_service.service;

import com.rohan.order_service.dto.OderLineItemsDto;
import com.rohan.order_service.dto.OrderRequest;
import com.rohan.order_service.model.Order;
import com.rohan.order_service.model.OrderLineItems;
import com.rohan.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OderLineItemsDto orderOderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderOderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderOderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderOderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}

