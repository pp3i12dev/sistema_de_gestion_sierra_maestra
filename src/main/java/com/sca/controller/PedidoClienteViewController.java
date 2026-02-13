package com.sca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos-clientes")
public class PedidoClienteViewController {

    @GetMapping("")
    public String index() {
        return "pedidos-clientes/index";
    }
}
