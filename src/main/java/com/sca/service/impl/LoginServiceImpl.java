package com.sca.service.impl;

import com.sca.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean validarUsuario(String email, String password) {
        // Simulación (acá luego conectás al endpoint real o repositorio)
        return "cliente@mail.com".equalsIgnoreCase(email) && "1234".equals(password);
    }

    @Override
    public boolean validarAdmin(String legajo, String password) {
        return "A001".equalsIgnoreCase(legajo) && "admin123".equals(password);
    }
}
