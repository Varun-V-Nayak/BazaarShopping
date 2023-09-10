package com.example.final_UI_dev.entity;
public class LoginResponse {
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public LoginResponse(String token,int id) {
        this.token = token;
        this.id=id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
