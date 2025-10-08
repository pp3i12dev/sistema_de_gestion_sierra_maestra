package com.sca.dto;

public class LoginRequest {
    private String email;
    private String legajo;
    private String password;

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
