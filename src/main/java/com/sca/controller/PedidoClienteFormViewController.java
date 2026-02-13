package com.sca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos-clientes") // Base path para pedidos de clientes
public class PedidoClienteFormViewController {

    @GetMapping("/formulario")
    public String showPedidoClienteFormPage() {
        return "pedidos-clientes/formulario";
    }
}
