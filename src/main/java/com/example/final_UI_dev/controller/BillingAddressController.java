package com.example.final_UI_dev.controller;
import com.example.final_UI_dev.entity.BillingAddress;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.service.BillingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/billing")
public class BillingAddressController {
    @Autowired
    private BillingAddressService billingAddressService;
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateByUser(@PathVariable int userId, @RequestBody BillingAddress newAddress) throws Exception {
        try {
            BillingAddress updatedAddress = billingAddressService.updateByUser(userId, newAddress);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> save(@PathVariable int userId,@RequestBody BillingAddress shippingAddress) {
        try {
            BillingAddress savedAddress = billingAddressService.save(userId, shippingAddress);
            return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        try {
            List<BillingAddress> addresses = billingAddressService.getAll();
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable int userId) {
        try {
            Users user = new Users();
            user.setId(userId);
            List<BillingAddress> addresses = billingAddressService.getByUser(user);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        try {
            billingAddressService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody BillingAddress updatedAddress) {
        try {
            BillingAddress address = billingAddressService.getById(id);
            if (address == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // update the fields of the existing address with the values from the updatedAddress object
            if (updatedAddress.getFullName() != null) {
                address.setFullName(updatedAddress.getFullName());
            }
            if (updatedAddress.getEmail() != null) {
                address.setEmail(updatedAddress.getEmail());
            }
            if (updatedAddress.getAddressLine1() != null) {
                address.setAddressLine1(updatedAddress.getAddressLine1());
            }
            if (updatedAddress.getAddressLine2() != null) {
                address.setAddressLine2(updatedAddress.getAddressLine2());
            }
            if (updatedAddress.getCountry() != null) {
                address.setCountry(updatedAddress.getCountry());
            }
            if (updatedAddress.getCompany() != null) {
                address.setCompany(updatedAddress.getCompany());
            }
            if (updatedAddress.getPhone() != null) {
                address.setPhone(updatedAddress.getPhone());
            }
            if (updatedAddress.getZipCode() != null) {
                address.setZipCode(updatedAddress.getZipCode());
            }
            BillingAddress savedAddress = billingAddressService.save(id, address);
            return new ResponseEntity<>(savedAddress, HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
