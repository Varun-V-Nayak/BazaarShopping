package com.example.final_UI_dev.entity;

public class Token {
    public Token(String token) {
        this.token = token;
    }
    public Token(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
