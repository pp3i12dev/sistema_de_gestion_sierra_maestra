package com.sca.service;

public interface LoginService {
    boolean validarUsuario(String email, String password);
    boolean validarAdmin(String legajo, String password);
}
