package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.BillingAddress;
import com.example.final_UI_dev.entity.ShippingAddress;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.BillingAddressRepository;
import com.example.final_UI_dev.repository.ShippingAddressRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingAddressService {
    @Autowired
    private BillingAddressRepository billingAddressRepository;
    @Autowired
    private UsersRepository usersRepository;
    public BillingAddress updateByUser(int userId, BillingAddress newAddress) throws Exception {
        BillingAddress addressToUpdate = billingAddressRepository.findByUserId(userId);
        if (addressToUpdate == null) {
            throw new Exception("Shipping address not found for user ID: " + userId);
        }
        addressToUpdate.setFullName(newAddress.getFullName());
        addressToUpdate.setEmail(newAddress.getEmail());
        addressToUpdate.setAddressLine1(newAddress.getAddressLine1());
        addressToUpdate.setAddressLine2(newAddress.getAddressLine2());
        addressToUpdate.setPhone(newAddress.getPhone());
        addressToUpdate.setCompany(newAddress.getCompany());
        addressToUpdate.setZipCode(newAddress.getZipCode());
        addressToUpdate.setCountry(newAddress.getCountry());
        return billingAddressRepository.save(addressToUpdate);
    }
    public BillingAddress save(int userId, BillingAddress billingAddress) {
        Users users = usersRepository.findById(userId).orElse(null);
        billingAddress.setUser(users);
        return billingAddressRepository.save(billingAddress);
    }

    public List<BillingAddress> getAll() {
        return billingAddressRepository.findAll();
    }


    public List<BillingAddress> getByUser(Users user) {
        return billingAddressRepository.findByUser(user);
    }

    public void deleteById(int id) {
        billingAddressRepository.deleteById(id);
    }

    public BillingAddress getById(int id) {
        return billingAddressRepository.findById(id).orElse(null);
    }
}
