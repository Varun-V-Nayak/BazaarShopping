package com.example.final_UI_dev.repository;
import com.example.final_UI_dev.entity.BillingAddress;

import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, Integer>  {
    List<BillingAddress> findByUser(Users user);

    BillingAddress findByUserId(int userId);
}
