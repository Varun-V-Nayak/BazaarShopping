package com.example.final_UI_dev.repository;

import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findByEmailIgnoreCase(String email);


    Optional<Users> findByEmail(String email);

}
