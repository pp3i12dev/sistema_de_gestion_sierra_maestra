package com.sca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestionCervezaController {

    @GetMapping("/gestion-cerveza")
    public String gestionCerveza() {
        // Renderiza src/main/resources/templates/gestion-cerveza/index.html
        return "gestion-cerveza/index";
    }
}
