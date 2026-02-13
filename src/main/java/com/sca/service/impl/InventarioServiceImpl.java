package com.sca.service.impl;

import com.sca.service.InventarioService;
import com.sca.service.LoteService;
import com.sca.service.MaduradorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sca.model.Respuesta;
import com.sca.service.AccesorioService;
import com.sca.service.BarrilService;
import com.sca.service.CervezaService;
import com.sca.service.CsvService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InventarioServiceImpl extends ResponseEntityExceptionHandler implements InventarioService {

    private final LoteService lotesService;
    private final BarrilService barrilService;
    private final CervezaService cervezaService;
    private final AccesorioService accesorioService;
    private final MaduradorService maduradorService;
    private final CsvService csvService;

    public InventarioServiceImpl(LoteService lotesService, BarrilService barrilService, CervezaService cervezaService,
            AccesorioService accesorioService, MaduradorService maduradorService, CsvService csvService) {
        this.lotesService = lotesService;
        this.barrilService = barrilService;
        this.cervezaService = cervezaService;
        this.accesorioService = accesorioService;
        this.maduradorService = maduradorService;
        this.csvService = csvService;
    }

    private String byteToString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> toMapList(Object rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object item : (List<?>) rawData) {
            result.add(objectMapper.convertValue(item, Map.class));
        }
        return result;
    }

    @Override
    public byte[] invetarioCsv() {

        String csv = "";

        Respuesta loteResp = lotesService.findAll();
        List<Map<String, Object>> loteData = toMapList(loteResp.getData());
        byte[] loteCsv = csvService.generateCsv(loteData);

        Respuesta barrilResp = barrilService.findAll();
        List<Map<String, Object>> barrilData = toMapList(barrilResp.getData());
        byte[] barrilCsv = csvService.generateCsv(barrilData);

        Respuesta cervezaResp = cervezaService.findAll();
        List<Map<String, Object>> cervezaData = toMapList(cervezaResp.getData());
        byte[] cervezaCsv = csvService.generateCsv(cervezaData);

        Respuesta accesorioResp = accesorioService.findAll();
        List<Map<String, Object>> accesorioData = toMapList(accesorioResp.getData());
        byte[] accesorioCsv = csvService.generateCsv(accesorioData);

        Respuesta maduradorResp = maduradorService.findAll();
        List<Map<String, Object>> maduradorData = toMapList(maduradorResp.getData());
        byte[] maduradorCsv = csvService.generateCsv(maduradorData);

        csv += "LOTE" + "\n" + byteToString(loteCsv) + "\n" +
                "BARRIL" + "\n" + byteToString(barrilCsv) + "\n" +
                "CERVEZA" + "\n" + byteToString(cervezaCsv) + "\n" +
                "ACCESORIO" + "\n" + byteToString(accesorioCsv) + "\n" +
                "MADURADOR" + "\n" + byteToString(maduradorCsv);

        csv = csv.replace("id,cerveza,", "id,cerveza,,");

        csv = csv.replaceAll("(?m)(BARRIL\\s*\\R\\s*)id,litros,estado,notas(?!,lote)", "$1id,litros,estado,notas,lote");

        StringBuilder out = new StringBuilder();

        java.util.regex.Pattern headerPattern = java.util.regex.Pattern
                .compile("(?m)^(LOTE|BARRIL|CERVEZA|ACCESORIO|MADURADOR)\\s*$");
        java.util.regex.Matcher headerMatcher = headerPattern.matcher(csv);

        List<Integer> starts = new ArrayList<>();
        List<String> sections = new ArrayList<>();
        while (headerMatcher.find()) {
            sections.add(headerMatcher.group(1));
            starts.add(headerMatcher.start());
        }
        // si no hay secciones detectadas, mantenemos csv tal cual
        if (sections.isEmpty()) {
            csv = csv.trim();
        } else {
            starts.add(csv.length()); // límite final
            for (int i = 0; i < sections.size(); i++) {
                int s = starts.get(i);
                int e = starts.get(i + 1);
                String block = csv.substring(s, e).trim();
                // separar líneas del bloque
                String[] lines = block.split("\\R", -1);
                String section = lines.length > 0 ? lines[0].trim() : sections.get(i);
                out.append(section).append("\n");

                // función auxiliar para obtener la línea de cabecera y las filas
                String headerLine = lines.length > 1 ? lines[1].trim() : "";
                List<String> dataLines = new ArrayList<>();
                if (lines.length > 2) {
                    for (int j = 2; j < lines.length; j++) {
                        if (!lines[j].trim().isEmpty())
                            dataLines.add(lines[j].trim());
                    }
                }

                if ("LOTE".equals(section)) {
                    // limpiar header: quitar "barriles" si existe
                    String newHeader = headerLine.replaceAll(",\\s*barriles\\s*$", "").trim();
                    // mantener la coma extra si ya existía (no colapsamos todas)
                    out.append(newHeader).append("\n");
                    // procesar cada fila: reemplazar el mapa cerveza por id=..., nombreCerveza=...
                    java.util.regex.Pattern cervezaMap = java.util.regex.Pattern
                            .compile("\\{\\s*id=(\\d+)\\s*,\\s*nombreCerveza=([^,}]+)[^}]*\\}");
                    for (String dl : dataLines) {
                        // quitar posible lista de barriles al final
                        dl = dl.replaceAll(",?\\s*\\[\\{.*?\\}\\]\\s*$", "");
                        java.util.regex.Matcher m = cervezaMap.matcher(dl);
                        if (m.find()) {
                            String rep = "id=" + m.group(1) + ", nombreCerveza=" + m.group(2);
                            dl = m.replaceFirst(rep);
                        }
                        // quitar llaves sueltas si quedaran
                        dl = dl.replaceAll("\\{\\s*", "").replaceAll("\\s*\\}", "");
                        out.append(dl).append("\n");
                    }
                } else if ("BARRIL".equals(section)) {
                    // asegurar header con lote
                    String newHeader = headerLine;
                    if (!newHeader.contains("lote")) {
                        newHeader = newHeader.endsWith(",") ? newHeader + "lote" : newHeader + ",lote";
                    }
                    out.append(newHeader).append("\n");
                    // reemplazar el contenido de lote {id=..., ...} por id=#
                    java.util.regex.Pattern loteMap = java.util.regex.Pattern.compile("\\{\\s*id=(\\d+)[^}]*\\}");
                    for (String dl : dataLines) {
                        java.util.regex.Matcher m = loteMap.matcher(dl);
                        if (m.find()) {
                            dl = m.replaceAll("id=" + m.group(1));
                        }
                        out.append(dl).append("\n");
                    }
                } else if ("CERVEZA".equals(section) || "ACCESORIO".equals(section)) {
                    // dejamos exactamente igual (header + filas)
                    if (!headerLine.isEmpty())
                        out.append(headerLine).append("\n");
                    for (String dl : dataLines)
                        out.append(dl).append("\n");
                } else if ("MADURADOR".equals(section)) {
                    // asegurar header con columna cerveza al final
                    String newHeader = headerLine;
                    if (!newHeader.contains("cerveza")) {
                        newHeader = newHeader.endsWith(",") ? newHeader + "cerveza" : newHeader + ",cerveza";
                    }
                    out.append(newHeader).append("\n");
                    // patrón para extraer lote{id=..., cerveza={id=..., nombreCerveza=...}, ...}
                    java.util.regex.Pattern loteCompleto = java.util.regex.Pattern
                            .compile("\\{\\s*id=(\\d+)[^}]*cerveza=\\{([^}]*)\\}[^}]*\\}");
                    java.util.regex.Pattern cervezaInner = java.util.regex.Pattern
                            .compile("id=(\\d+)[^,}]*,\\s*nombreCerveza=([^,}]+)");
                    for (String dl : dataLines) {
                        java.util.regex.Matcher m = loteCompleto.matcher(dl);
                        if (m.find()) {
                            String loteId = m.group(1);
                            String cervezaBlock = m.group(2);
                            java.util.regex.Matcher mc = cervezaInner.matcher(cervezaBlock);
                            String cerSummary = "";
                            if (mc.find()) {
                                cerSummary = "cerveza={id=" + mc.group(1) + ", nombreCerveza=" + mc.group(2) + "}";
                            }
                            // reemplazar la porción completa del lote por id=#
                            dl = m.replaceFirst("id=" + loteId);
                            // añadir al final la columna cerveza (si no está ya)
                            dl = dl + ", " + cerSummary;
                        } else {
                            // si no matchea, intentar extraer un id simple dentro de llaves
                            java.util.regex.Matcher simpleLote = java.util.regex.Pattern
                                    .compile("\\{\\s*id=(\\d+)[^}]*\\}").matcher(dl);
                            if (simpleLote.find()) {
                                dl = simpleLote.replaceAll("id=" + simpleLote.group(1));
                            }
                            // y no añadimos cerveza si no se pudo extraer
                        }
                        out.append(dl).append("\n");
                    }
                } else {
                    // bloque desconocido: lo dejamos
                    if (!headerLine.isEmpty())
                        out.append(headerLine).append("\n");
                    for (String dl : dataLines)
                        out.append(dl).append("\n");
                }

                // separar secciones con una línea en blanco
                out.append("\n");
            }
            csv = out.toString().trim();
            // limpiar dobles saltos de línea innecesarios
            csv = csv.replaceAll("(?m)\\n{3,}", "\n\n");
        }

        return csv.getBytes(StandardCharsets.UTF_8);
    }
}
