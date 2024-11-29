package com.example.reels.models;

public class logIn_model {
    String Email , Pass;

    public logIn_model(String email, String pass) {
        Pass = pass;
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
