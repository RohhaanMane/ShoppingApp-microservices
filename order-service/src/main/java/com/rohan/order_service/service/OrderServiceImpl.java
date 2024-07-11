package com.rohan.order_service.service;

import com.rohan.order_service.dto.InventoryResponse;
import com.rohan.order_service.dto.OrderLineItemsDto;
import com.rohan.order_service.dto.OrderRequest;
import com.rohan.order_service.model.Order;
import com.rohan.order_service.model.OrderLineItems;
import com.rohan.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call Inventory Service, and place order if product is in
        // stock
        InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
                .allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

//    private final OrderRepository orderRepository;
//    private final WebClient.Builder webClientBuilder;
//
//    @Override
//    public void placeOrder(OrderRequest orderRequest) {
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//
//        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//
//        order.setOrderLineItemsList(orderLineItems);
//
//        List<String> skuCodes = order.getOrderLineItemsList()
//                .stream()
//                .map(OrderLineItems::getSkuCode)
//                .toList();
//
//        // Call inverntory service, and place order if product present in Stock
//        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()    // get method will requset another service
//                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                .retrieve()                         // to be able to retrieve the response
//                .bodyToMono(InventoryResponse[].class)          // for the web Client we need to define type of response we're returning from inventory service. Mono if object in reactive programming like Optional<?>
//                .block();                           // By Default webClint makes Asynchronous request
//        // to make synchrounous request with web client, use block()
//
//        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
//                .allMatch(InventoryResponse::isInStock);
//
//        if (allProductsInStock) {
//            orderRepository.save(order);
//        } else {
//            throw new IllegalArgumentException("Product is not in stock, please try again later");
//        }
//    }
//
//    private OrderLineItems mapToDto(OderLineItemsDto orderOderLineItemsDto) {
//        OrderLineItems orderLineItems = new OrderLineItems();
//        orderLineItems.setPrice(orderOderLineItemsDto.getPrice());
//        orderLineItems.setQuantity(orderOderLineItemsDto.getQuantity());
//        orderLineItems.setSkuCode(orderOderLineItemsDto.getSkuCode());
//        return orderLineItems;
//    }
}

