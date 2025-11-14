package com.sca.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sca.dto.BarrilDTO;
import com.sca.model.Barril;
import com.sca.model.Lote;
import com.sca.model.Respuesta;
import com.sca.repository.BarrilRepository;
import com.sca.service.BarrilService;

@Service
public class BarrilServiceImpl extends ResponseEntityExceptionHandler implements BarrilService {

    private static final Logger log = LoggerFactory.getLogger(BarrilServiceImpl.class);

    @Autowired
    private BarrilRepository barrilRepository;

    private Respuesta respuesta;
    private String resp = "";

    // Guardar
    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Barril barril, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agregó un Barril");
            respuesta.setData(barrilRepository.save(barril));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Barril");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp += r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
            } else {
                respuesta.setData(e.getMessage());
            }
            return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
        }
        return new ResponseEntity<>(respuesta, null, HttpStatus.CREATED);
    }

    // Eliminar
    @Override
    public Respuesta delete(Long id) {
        respuesta = new Respuesta();
        try {
            Barril barril = barrilRepository.findById(id).orElse(null);
            if (barril != null) {
                barrilRepository.deleteById(id);
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Se eliminó un Barril");
                respuesta.setData(barril);
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("No encontrado");
                respuesta.setDescripcion("No existe el Barril con ID " + id);
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Barril");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Listar todos
    @Override
    public Respuesta findAll() {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se muestran todos los Barriles");
            respuesta.setData(barrilRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Barriles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Buscar por ID
    @Override
    public Respuesta findById(Long id) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos del Barril");
            respuesta.setData(barrilRepository.findById(id).orElse(null));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Barril");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Actualizar
    @Override
    public ResponseEntity<Object> update(Barril barril, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Barril");
            respuesta.setData(barrilRepository.save(barril));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Barril");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> resp += r.getDefaultMessage() + ";");
                respuesta.setData(resp);
                resp = "";
            } else {
                respuesta.setData(e.getMessage());
            }
            return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
        }
        return new ResponseEntity<>(respuesta, null, HttpStatus.CREATED);
    }

    // Buscar por Estado
    @Override
    public Respuesta findByEstado(String estado) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos de los Barriles por Estado");
            respuesta.setData(
                barrilRepository.findAll()
                                .stream()
                                .filter(n -> n.getEstado().equals(estado))
                                .collect(Collectors.toList())
            );
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos de los Barriles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Buscar por Lote
    @Override
    public Respuesta findByLote(Lote lote) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos de los Barriles por Lote");
            respuesta.setData(
                barrilRepository.findAll()
                                .stream()
                                .filter(n -> lote.equals(n.getLote()))
                                .collect(Collectors.toList())
            );
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron filtrar los Barriles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Buscar por Cerveza y Estado
    @Override
    public Respuesta findByCervezaAndEstado(Long cervezaId, String estado) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos de los Barriles por Cerveza y Estado");
            respuesta.setData(barrilRepository.findByLote_Cerveza_IdAndEstado(cervezaId, estado));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos de los Barriles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Buscar disponibles (estado Cargado) de una Cerveza
    @Override
    public Respuesta findDisponiblesByCerveza(Long cervezaId) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Barriles cargados de la cerveza seleccionada");
            respuesta.setData(barrilRepository.findByLote_Cerveza_IdAndEstado(cervezaId, "Cargado"));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("Error al buscar barriles disponibles");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    // Buscar disponibles (DTO) por Cerveza
    public List<BarrilDTO> findDisponiblesDTOByCerveza(Long cervezaId) {
        return barrilRepository.findByLote_Cerveza_IdAndEstado(cervezaId, "Cargado")
                .stream()
                .map(BarrilDTO::new)
                .collect(Collectors.toList());
    }

    // Buscar por Cerveza y Estado (DTO)
    public List<BarrilDTO> findByCervezaAndEstadoDTO(Long cervezaId, String estado) {
        return barrilRepository.findByLote_Cerveza_IdAndEstado(cervezaId, estado)
                .stream()
                .map(BarrilDTO::new)
                .collect(Collectors.toList());
    }

    // Marcar barriles como alquilados
    @Override
    public void marcarComoAlquilados(List<Long> ids) {
        ids.forEach(id -> {
            Barril b = barrilRepository.findById(id).orElse(null);
            if (b != null) {
                b.setEstado("Alquilado");
                barrilRepository.save(b);
            }
        });
    }

        // Marcar barriles como cargados (cuando se cancela/elimina pedido)
    @Override
    public void marcarComoCargados(List<Long> ids) {
        ids.forEach(id -> {
            Barril b = barrilRepository.findById(id).orElse(null);
            if (b != null) {
                b.setEstado("Cargado");
                barrilRepository.save(b);
            }
        });
    }

    @Override
    public Barril updateReservaBarril(Barril barril) {
        Barril existingBarril = barrilRepository.findById(barril.getId())
                .orElseThrow(() -> new RuntimeException("Barril no encontrado"));
        
        // Actualizar solo campos de reserva, mantener los demás
        existingBarril.setEstado(barril.getEstado());
        // usar los nombres camelCase definidos en el modelo Barril
        existingBarril.setSessionReserva(barril.getSessionReserva());
        existingBarril.setTimestampReserva(barril.getTimestampReserva());
        
        return barrilRepository.save(existingBarril);
    }

    @Override
    public ResponseEntity<byte[]> exportarBarrilesPorEstadoExcel(String estado) {
        try {
            List<Barril> barriles = barrilRepository.findByEstado(estado);

            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.createSheet("Barriles_" + estado);
                Row header = sheet.createRow(0);
                String[] columnas = {"ID", "Litros", "Estado", "Notas", "LoteId", "SessionReserva", "TimestampReserva"};
                for (int i = 0; i < columnas.length; i++) {
                    header.createCell(i).setCellValue(columnas[i]);
                }

                int fila = 1;
                for (Barril b : barriles) {
                    Row row = sheet.createRow(fila++);
                    row.createCell(0).setCellValue(b.getId() != null ? b.getId() : 0);
                    row.createCell(1).setCellValue(b.getLitros() != null ? b.getLitros() : 0);
                    row.createCell(2).setCellValue(b.getEstado() != null ? b.getEstado() : "");
                    row.createCell(3).setCellValue(b.getNotas() != null ? b.getNotas() : "");
                    row.createCell(4).setCellValue(b.getLote() != null && b.getLote().getId() != null ? b.getLote().getId() : 0);
                    row.createCell(5).setCellValue(b.getSessionReserva() != null ? b.getSessionReserva() : "");
                    row.createCell(6).setCellValue(b.getTimestampReserva() != null ? b.getTimestampReserva().toString() : "");
                }

                workbook.write(out);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=barriles_" + estado + ".xlsx");

                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(out.toByteArray());
            }

        } catch (IOException e) {
            log.error("Error generando Excel de barriles", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}



