package com.example.final_UI_dev.controller;
import com.example.final_UI_dev.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/placeOrder/{userId}")
    public ResponseEntity<String> placeOrder(@PathVariable int userId) {
        try {
            Boolean orderResponse =  ordersService.placeOrder(userId);
            if(orderResponse==true){
                return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Empty Cart", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the orders using the service method
        List<Object> ordersPage =  ordersService.getOrdersByUser(userId, pageable);

        return ResponseEntity.ok(ordersPage);
    }
}
