package com.sca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sca.service.impl.ClienteServiceImpl;

@Controller
public class ClienteVistaController {

    @Autowired
    ClienteServiceImpl clientesServiceImpl;

    // ✅ Mostrar formulario al abrir el enlace del correo
    @GetMapping("/clientes/reset-password")
    public String mostrarFormularioReset(@RequestParam("token") String token, Model model) {
        ResponseEntity<?> validacion = clientesServiceImpl.validarTokenResetPassword(token);

        if (validacion.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("error", validacion.getBody());
        }
        return "clientes/reset-password"; // siempre usa el mismo HTML
    }

    // ✅ Procesar el formulario
    @PostMapping("/clientes/reset-password")
    public String confirmarResetPassword(
            @RequestParam("token") String token,
            @RequestParam("nuevaContrasenia") String nuevaContrasenia,
            Model model) {

        ResponseEntity<?> resultado = clientesServiceImpl.aplicarResetPassword(token, nuevaContrasenia);

        if (resultado.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("mensaje", "Contraseña actualizada correctamente");
        } else {
            model.addAttribute("error", resultado.getBody());
        }
        return "clientes/reset-password"; // mismo HTML, cambia el contenido
    }
}
