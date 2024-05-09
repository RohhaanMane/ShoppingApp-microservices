package com.rohan.productservice.controller;

import com.rohan.productservice.dto.ProductRequest;
import com.rohan.productservice.dto.ProductResponse;
import com.rohan.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

//    public ProductController(ProductService productService){
//        this.productService = productService;
//    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);

    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}
