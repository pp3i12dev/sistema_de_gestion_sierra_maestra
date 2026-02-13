package com.sca.service;

import com.sca.repository.BarrilRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BarrilReportService {

    @Autowired
    private BarrilRepository barrilRepo;

    // Total de barriles en la base
    public long totalBarriles() {
        return barrilRepo.count();
    }

    // Chequeo de stock bajo según umbral del usuario
    public boolean isStockBelowThreshold(int threshold) {
        long disponibles = barrilRepo.findAll().stream()
                .filter(b -> b.getEstado() != null && b.getEstado().equalsIgnoreCase("Disponible"))
                .count();
        return disponibles < threshold;
    }
}
