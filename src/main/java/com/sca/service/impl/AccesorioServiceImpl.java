package com.sca.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

import com.sca.model.Accesorio;
import com.sca.model.Respuesta;
import com.sca.repository.AccesorioRepository;
import com.sca.service.AccesorioService;

@Service
public class AccesorioServiceImpl extends ResponseEntityExceptionHandler implements AccesorioService {

    Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    private AccesorioRepository accesorioRepository;

    private Respuesta respuesta;
    private String resp = "";

    @ExceptionHandler(BindException.class)
    @Override
    public ResponseEntity<Object> save(Accesorio accesorio, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se agrego un Accesorio");
            respuesta.setData(accesorioRepository.save(accesorio));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo agregar el Accesorio");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
                respuesta.setData(resp);
                resp = "";
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            } else {
                respuesta.setData(e.getMessage());
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            }
        }
        return new ResponseEntity<Object>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public Respuesta delete(Long id) {
        respuesta = new Respuesta();
        try {
            Accesorio accesorio = accesorioRepository.findById(id).orElse(null);
            if (accesorio != null) {
                accesorioRepository.deleteById(id);
                respuesta.setCodigo("200");
                respuesta.setStatus("Ok");
                respuesta.setDescripcion("Se eliminó un Accesorio");
                respuesta.setData(accesorio);
            } else {
                respuesta.setCodigo("404");
                respuesta.setStatus("Not Found");
                respuesta.setDescripcion("Accesorio no encontrado");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudo eliminar el Accesorio");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findAll() {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se muestran todos los Accesorios");
            respuesta.setData(accesorioRepository.findAll());
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los Accesorios");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public Respuesta findById(Long id) {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos del Accesorio");
            respuesta.setData(accesorioRepository.findById(id).orElse(null));
        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos del Accesorio");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }

    @Override
    public ResponseEntity<Object> update(Accesorio accesorio, BindingResult bindingResult) throws BindException {
        respuesta = new Respuesta();
        try {
            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Se modificaron los datos del Accesorio");
            respuesta.setData(accesorioRepository.save(accesorio));
        } catch (Exception e) {
            respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            respuesta.setDescripcion("No se pudo modificar el Accesorio");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(r -> {
                    resp = resp + r.getDefaultMessage() + ";";
                });
                respuesta.setData(resp);
                resp = "";
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            } else {
                respuesta.setData(e.getMessage());
                return handleExceptionInternal(e, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
            }
        }
        return new ResponseEntity<Object>(respuesta, null, HttpStatus.CREATED);
    }

    @Override
    public Respuesta findAccesorioPorEstado(String estado) {
        Respuesta respuesta = new Respuesta();
        try {
            List<Accesorio> accesorios = accesorioRepository.findAll()
                .stream()
                .filter(a -> a.getEstado() != null && a.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());

            respuesta.setCodigo("200");
            respuesta.setStatus("Ok");
            respuesta.setDescripcion("Datos de los Accesorios por Estado");
            respuesta.setData(accesorios);

        } catch (Exception e) {
            respuesta.setCodigo("400");
            respuesta.setStatus("Error");
            respuesta.setDescripcion("No se pudieron mostrar los datos de los Accesorios");
            respuesta.setData(e.getMessage());
        }
        return respuesta;
    }



    @Override
    public void marcarComoAlquilados(List<Long> ids) {
        ids.forEach(id -> {
            var a = accesorioRepository.findById(id).orElse(null);
            if (a != null) {
                a.setEstado("Alquilado");
                accesorioRepository.save(a);
            }
        });
    }

    @Override
    public void marcarComoDisponibles(List<Long> ids) {
    ids.forEach(id -> {
        var a = accesorioRepository.findById(id).orElse(null);
        if (a != null) {
            a.setEstado("Disponible");
            accesorioRepository.save(a);
        }
    });
    }

@Override
public Respuesta findDisponiblesByNombre(String nombre) {
    respuesta = new Respuesta();
    try {
        respuesta.setCodigo("200");
        respuesta.setStatus("Ok");
        respuesta.setDescripcion("Accesorios disponibles por nombre");
        respuesta.setData(
            accesorioRepository.findAll()
                .stream()
                .filter(a -> "Disponible".equalsIgnoreCase(a.getEstado()) 
                          && nombre.equalsIgnoreCase(a.getNombre()))
                .collect(Collectors.toList())
        );
    } catch (Exception e) {
        respuesta.setCodigo("400");
        respuesta.setStatus("Error");
        respuesta.setDescripcion("No se pudieron obtener los accesorios disponibles");
        respuesta.setData(e.getMessage());
    }
    return respuesta;
}


@Override
public ResponseEntity<byte[]> exportarAccesoriosExcel(String estado) {
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Accesorios");

        // Fila de encabezado
        Row header = sheet.createRow(0);
        String[] columnas = {"ID", "Nombre", "Estado", "Notas"};
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }

        // Filtrar por estado si se pasa
        List<Accesorio> accesorios;
        if (estado != null && !estado.isEmpty()) {
            accesorios = accesorioRepository.findAll()
                            .stream()
                            .filter(a -> estado.equalsIgnoreCase(a.getEstado()))
                            .collect(Collectors.toList());
        } else {
            accesorios = accesorioRepository.findAll();
        }

        // Llenar datos
        int rowNum = 1;
        for (Accesorio a : accesorios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(a.getId());
            row.createCell(1).setCellValue(a.getNombre());
            row.createCell(2).setCellValue(a.getEstado());
            row.createCell(3).setCellValue(a.getNotas() != null ? a.getNotas() : "");
        }

        for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=accesorios.xlsx");

        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}




}
