package com.example.final_UI_dev.repository;

import com.example.final_UI_dev.entity.Tokens;
import com.example.final_UI_dev.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Tokens,Integer> {
    @Query("SELECT t FROM Tokens t WHERE t.user = :user")
    Optional<Tokens> getTokenByUser(Users user);

    @Query("SELECT t.user.id FROM Tokens t WHERE t.token = :token")
    Integer findUserIdByToken(@Param("token") String token);
}
