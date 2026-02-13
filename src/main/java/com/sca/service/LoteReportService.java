package com.sca.service;

import com.sca.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteReportService {

    @Autowired
    private LoteRepository loteRepo;

    // Total de lotes en la base (o cantidad total, según tu criterio)
    public long totalLotes() {
        return loteRepo.count();
    }

    // Total de lotes por estado "Terminado"
    public long totalLotesTerminados() {
        return loteRepo.findAll().stream()
                .filter(l -> "Terminado".equalsIgnoreCase(l.getEstado()))
                .count();
    }

    // Chequeo de stock bajo según umbral del usuario
    public boolean isStockBelowThreshold(long threshold) {
        long terminados = totalLotesTerminados(); // solo terminados
        return terminados < threshold;
    }

    // Método para notificación (por ahora log)
    public void checkAndNotify(long threshold) {
        if (isStockBelowThreshold(threshold)) {
            System.out.println("⚠️ Stock de lotes terminados por debajo del umbral: " + threshold);
        }
    }
}