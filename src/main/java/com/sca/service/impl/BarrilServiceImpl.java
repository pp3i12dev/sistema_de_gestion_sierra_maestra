package com.sca.service.impl;

import java.io.ByteArrayOutputStream;
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
import org.springframework.http.MediaType;
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
        Respuesta respuesta = new Respuesta();
        try {
            List<Barril> lista = barrilRepository.findByEstado(estado);
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Barriles con estado: " + estado);
            respuesta.setData(lista);
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
    public ResponseEntity<byte[]> exportarBarrilesPorEstadoExcel(String estado) {
        List<Barril> lista = barrilRepository.findByEstado(estado);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Barriles_" + estado);

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Litros");
            header.createCell(2).setCellValue("Estado");
            header.createCell(3).setCellValue("Notas");
            header.createCell(4).setCellValue("Lote ID");

            // Filas
            int rowNum = 1;
            for (Barril b : lista) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getLitros() != null ? b.getLitros() : 0);
                row.createCell(2).setCellValue(b.getEstado() != null ? b.getEstado() : "");
                row.createCell(3).setCellValue(b.getNotas() != null ? b.getNotas() : "");
                row.createCell(4).setCellValue(b.getLote() != null ? b.getLote().getId() : 0);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Barriles_" + estado + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
}



}
