package com.sca.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sca.model.PedidoCliente;

public interface PedidoClienteRepository extends JpaRepository<PedidoCliente, Long> {
    List<PedidoCliente> findByNombreClienteContainingIgnoreCase(String nombreCliente);
    List<PedidoCliente> findByEmailClienteContainingIgnoreCase(String emailCliente);
    List<PedidoCliente> findByEstado(String estado);
    List<PedidoCliente> findByEstadoPago(String estadoPago);
}
