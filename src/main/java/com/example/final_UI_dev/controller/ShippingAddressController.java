package com.example.final_UI_dev.controller;

import com.example.final_UI_dev.entity.ShippingAddress;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.service.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/shipping")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;
    @PutMapping("/user/{userId}")
    public ResponseEntity<ShippingAddress> updateByUser(@PathVariable int userId, @RequestBody ShippingAddress newAddress) throws Exception {
        ShippingAddress updatedAddress = shippingAddressService.updateByUser(userId, newAddress);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<ShippingAddress> save(@PathVariable int userId,@RequestBody ShippingAddress shippingAddress) {
        ShippingAddress savedAddress = shippingAddressService.save(userId,shippingAddress);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ShippingAddress>> getAll() {
        List<ShippingAddress> addresses = shippingAddressService.getAll();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShippingAddress>> getByUser(@PathVariable int userId) {
        Users user = new Users();
        user.setId(userId);
        List<ShippingAddress> addresses = shippingAddressService.getByUser(user);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        shippingAddressService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

