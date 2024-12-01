package com.example.reels.models;

public class logIn_model {
    String Phone , Pass;

    public logIn_model(String phone , String pass) {
        Pass = pass;
        Phone = phone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
