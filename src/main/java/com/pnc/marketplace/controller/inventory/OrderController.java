package com.pnc.marketplace.controller.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.Inventory.Order;
import com.pnc.marketplace.service.inventory.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;


    @GetMapping("/{email}")
    ResponseEntity<?> getOrderByUser(@PathVariable String email){

        List<Order> response = this.orderService.getAllOrderByUserEmail(email);

        if(response != null)
            return ResponseEntity.status(201).body(response);
        
        return null;
    }

    @GetMapping("/seller/{sellerId}")
    ResponseEntity<?> getOrderByUser(@PathVariable int sellerId){

        List<Order> response = this.orderService.getOrdersBySeller(sellerId);

        if(response != null)
            return ResponseEntity.status(201).body(response);
        
        return null;
    }
}
