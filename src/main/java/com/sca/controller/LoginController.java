package com.sca.controller;

import com.sca.dto.LoginRequest;
import com.sca.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*") // permite peticiones desde tu HTML
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/usuario")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequest request) {
        boolean valido = loginService.validarUsuario(request.getEmail(), request.getPassword());
        return valido
                ? ResponseEntity.ok("Login de usuario exitoso")
                : ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        boolean valido = loginService.validarAdmin(request.getLegajo(), request.getPassword());
        return valido
                ? ResponseEntity.ok("Login de administrador exitoso")
                : ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}
