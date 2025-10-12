package com.sca.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
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

import com.sca.model.Lote;
import com.sca.model.Respuesta;
import com.sca.repository.LoteRepository;
import com.sca.service.LoteService;

@Service
public class LoteServiceImpl extends ResponseEntityExceptionHandler implements LoteService{

Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	LoteRepository loteRepository;

	Respuesta respuesta;

	String resp = "";

	//El ExceptionHandler me sirve para recuperar o en viar el status del error del pedido
	@ExceptionHandler(BindException.class)
	@Override
	public ResponseEntity<Object> save(Lote lote, BindingResult bindingResult) throws BindException {
		// debug
		try {
			System.out.println("[DEBUG] LoteServiceImpl.save invoked with lote=" + lote + " bindingErrors=" + (bindingResult != null ? bindingResult.getErrorCount() : 0));
			if (bindingResult != null && bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(err -> System.out.println("[DEBUG] binding error: " + err.getDefaultMessage()));
			}
		} catch (Exception e) { System.out.println("[DEBUG] printing lote failed " + e.getMessage()); }
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se agrego una Lote");
			respuesta.setData(loteRepository.save(lote));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo agregar el Lote");
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
		System.out.println(id);
		try {
			Lote lote = loteRepository.findById(id).get();
			loteRepository.deleteById(id);
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se elimino un Lote");
			respuesta.setData(lote);
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudo eliminar el Lote");
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
			respuesta.setDescripcion("Se muestran todos los Lotes");
			respuesta.setData(loteRepository.findAll());
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los Lotes");
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
			respuesta.setDescripcion("Datos del Lote");
			// Return the entity or null so templates can safely access properties like ${lote.id}
			respuesta.setData(loteRepository.findById(id).orElse(null));
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los datos del Lote");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
	}

	@Override
	public ResponseEntity<Object> update(Lote lote, BindingResult bindingResult) throws BindException {
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se modificaron los datos del Lote");
			respuesta.setData(loteRepository.save(lote));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo modificar la Lote");
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


	public Respuesta findLotesPorEstado(String estado) {
		Respuesta respuesta = new Respuesta();
		try {
			List<Lote> lotes = loteRepository.findAll()
				.stream()
				.filter(l -> estado.equalsIgnoreCase(l.getEstado()))
				.collect(Collectors.toList());

			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Lotes por estado");
			respuesta.setData(lotes);
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los lotes por estado");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
}


public ByteArrayInputStream exportarPorEstadoAExcel(String estado) throws IOException {
    List<Lote> lotes = loteRepository.findByEstado(estado);

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Sheet sheet = workbook.createSheet("Lotes_" + estado);

        // Encabezado
        Row header = sheet.createRow(0);
        String[] columnas = {"ID", "Cerveza", "Cantidad (L)", "Estado", "Notas", "Fecha Carga", "Fecha Madurador", "Fecha Vencimiento"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        // Datos
        int rowIdx = 1;
        for (Lote lote : lotes) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(lote.getId());
            row.createCell(1).setCellValue(lote.getCerveza() != null ? lote.getCerveza().getNombreCerveza() : "-");
            row.createCell(2).setCellValue(lote.getCantidadLitros() != null ? lote.getCantidadLitros() : 0);
            row.createCell(3).setCellValue(lote.getEstado());
            row.createCell(4).setCellValue(lote.getNotas() != null ? lote.getNotas() : "-");
            row.createCell(5).setCellValue(lote.getFechaCarga() != null ? lote.getFechaCarga().toString() : "-");
            row.createCell(6).setCellValue(lote.getFechaCargaMadurador() != null ? lote.getFechaCargaMadurador().toString() : "-");
            row.createCell(7).setCellValue(lote.getFechaVencimiento() != null ? lote.getFechaVencimiento().toString() : "-");
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}


}
