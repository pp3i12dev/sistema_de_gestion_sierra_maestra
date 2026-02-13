package com.sca.service;

import com.sca.dto.CervezaReportDTO;
import com.sca.model.Lote;
import com.sca.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CervezaReportService {

    @Autowired
    private LoteRepository loteRepo;

    // Lista todas las cervezas con su total de litros sumando todos los lotes
    public List<CervezaReportDTO> allCervezasWithLitros() {
        List<Lote> lotes = loteRepo.findAll();

        // Agrupar por nombre de cerveza
        Map<String, Long> litrosPorCerveza = lotes.stream()
                .filter(l -> l.getCerveza() != null)
                .collect(Collectors.groupingBy(
                        l -> l.getCerveza().getNombreCerveza(),
                        Collectors.summingLong(Lote::getCantidadLitros)
                ));

        return litrosPorCerveza.entrySet().stream()
                .map(e -> new CervezaReportDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    // Cerveza por debajo de un umbral
    public List<CervezaReportDTO> cervezasBelowThreshold(long threshold) {
        return allCervezasWithLitros().stream()
                .filter(c -> c.getTotalLitros() < threshold)
                .collect(Collectors.toList());
    }

    // Chequeo de stock total
    public boolean isStockBelowThreshold(long threshold) {
        long totalLitros = allCervezasWithLitros().stream()
                .mapToLong(CervezaReportDTO::getTotalLitros)
                .sum();
        return totalLitros < threshold;
    }
}