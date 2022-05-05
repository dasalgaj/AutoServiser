package com.example.testapp.listeners;

public interface LoginListener {
    void setEmail (String email);
    void setPassword (String password);

    String getEmail();
    String getLozinka();
}
