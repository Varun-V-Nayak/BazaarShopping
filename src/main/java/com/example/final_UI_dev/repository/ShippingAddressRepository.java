package com.example.final_UI_dev.repository;

import com.example.final_UI_dev.entity.ShippingAddress;
import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {

    List<ShippingAddress> findByUser(Users user);

    ShippingAddress findByUserId(int userId);
}

