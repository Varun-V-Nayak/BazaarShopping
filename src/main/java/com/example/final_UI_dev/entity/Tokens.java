package com.example.final_UI_dev.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Tokens")
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Tokens( Users user, String token, Date creation) {
        this.user = user;
        this.token = token;
        this.creation = creation;
    }
    public Tokens(){

    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name="token")
    private String token;

    @Column(name="created_time")
    private Date creation;


}
