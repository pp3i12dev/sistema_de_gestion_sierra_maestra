package com.sca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/inicio")
    public String inicio() {
        // Página de selección entre Usuario o Cliente
        return "inicio";
    }

    @GetMapping("/home")
    public String home() {
        // busca el archivo home.html dentro de /src/main/resources/templates
        return "home";
    }

    @GetMapping("/")
    public String root() {
        // si alguien entra a http://localhost:8080/ lo redirige a la página de inicio
        return "redirect:/inicio";
    }
}