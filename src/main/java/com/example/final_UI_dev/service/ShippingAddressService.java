package com.example.final_UI_dev.service;

import com.example.final_UI_dev.entity.ShippingAddress;
import com.example.final_UI_dev.entity.Users;
import com.example.final_UI_dev.repository.ShippingAddressRepository;
import com.example.final_UI_dev.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingAddressService {

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    public ShippingAddress updateByUser(int userId, ShippingAddress newAddress) throws Exception {
        ShippingAddress addressToUpdate = shippingAddressRepository.findByUserId(userId);
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
        return shippingAddressRepository.save(addressToUpdate);
    }
    @Autowired
    private UsersRepository usersRepository;
    public ShippingAddress save(int userId, ShippingAddress shippingAddress) {
        shippingAddress.setUser(usersRepository.findById(userId).orElse(null));
        shippingAddress.getUser().setId(userId);
        return shippingAddressRepository.save(shippingAddress);
    }

    public List<ShippingAddress> getAll() {
        return shippingAddressRepository.findAll();
    }


    public List<ShippingAddress> getByUser(Users user) {
        return shippingAddressRepository.findByUser(user);
    }

    public void deleteById(int id) {
        shippingAddressRepository.deleteById(id);
    }

}

