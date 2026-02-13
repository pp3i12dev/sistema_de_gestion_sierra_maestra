package com.sca.service.impl;

import com.sca.service.CsvService;
import com.sca.service.PdfService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {

    private final CsvService csvService;

    public PdfServiceImpl(CsvService csvService) {
        this.csvService = csvService;
    }

    @Override
    public byte[] generatePdf(List<Map<String, Object>> data) throws Exception {
        // 1. Obtenemos CSV en bytes y lo convertimos a texto
        byte[] csvBytes = csvService.generateCsv(data);
        String csvText = new String(csvBytes, StandardCharsets.UTF_8);

        // 2. Partimos por líneas y luego por comas (csv controlado)
        String[] lines = csvText.split("\\r?\\n");
        if (lines.length == 0) {
            throw new IllegalArgumentException("CSV vacío");
        }

        // 3. Preparamos documento y tabla
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        int columns = lines[0].split(",", -1).length;
        PdfPTable table = new PdfPTable(columns);

        // 4. Rellenamos la tabla celda a celda
        for (String line : lines) {
            String[] cells = line.split(",", -1);
            for (String cell : cells) {
                table.addCell(cell);
            }
        }

        // 5. Añadimos la tabla al documento y cerramos
        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
