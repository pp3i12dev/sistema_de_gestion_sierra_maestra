package com.sca.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

import com.sca.model.Cerveza;
import com.sca.model.Respuesta;
import com.sca.repository.CervezaRepository;
import com.sca.service.CervezaService;

@Service
public class CervezaServiceImpl extends ResponseEntityExceptionHandler implements CervezaService{

Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	CervezaRepository cervezaRepository;

	Respuesta respuesta;

	String resp = "";

	//El ExceptionHandler me sirve para recuperar o en viar el status del error del pedido
	@ExceptionHandler(BindException.class)
	@Override
	public ResponseEntity<Object> save(Cerveza cerveza, BindingResult bindingResult) throws BindException {
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se agrego una Cerveza");
			respuesta.setData(cervezaRepository.save(cerveza));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo agregar la Cerveza");
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
			Cerveza cerveza = cervezaRepository.findById(id).get();
			cervezaRepository.deleteById(id);
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se elimino una Cerveza");
			respuesta.setData(cerveza);
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudo eliminar la Cerveza");
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
			respuesta.setDescripcion("Se muestran todos las Cervezas");
			respuesta.setData(cervezaRepository.findAll());
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar las Cerveza");
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
			respuesta.setDescripcion("Datos de la Cerveza");
			respuesta.setData(cervezaRepository.findById(id).orElse(null));
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar los datos del Cerveza");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
	}

	@Override
	public ResponseEntity<Object> update(Cerveza cerveza, BindingResult bindingResult) throws BindException {
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se modificaron los datos de la Cerveza");
			respuesta.setData(cervezaRepository.save(cerveza));
		} catch (Exception e) {
			respuesta.setCodigo(String.valueOf(HttpStatus.BAD_REQUEST.value()));
			respuesta.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
			respuesta.setDescripcion("No se pudo modificar la Cerveza");
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
	public Respuesta findByEstado(String estado) {
		respuesta = new Respuesta();
		try {
			respuesta.setCodigo("200");
			respuesta.setStatus("Ok");
			respuesta.setDescripcion("Se muestran las Cervezas por estado");
			respuesta.setData(cervezaRepository.findByEstado(estado));
		} catch (Exception e) {
			respuesta.setCodigo("400");
			respuesta.setStatus("Error");
			respuesta.setDescripcion("No se pudieron mostrar las Cervezas por estado");
			respuesta.setData(e.getMessage());
		}
		return respuesta;
	}

	@Override
public ByteArrayInputStream exportarPorEstadoAExcel(String estado) throws IOException {
    List<Cerveza> cervezas = cervezaRepository.findByEstado(estado);

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Sheet sheet = workbook.createSheet("Cervezas_" + estado);
        Row header = sheet.createRow(0);
        String[] columnas = {"ID", "Nombre", "Tipo", "Grado Alc.", "IBU", "Precio/L", "Estado", "Descripción"};
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }

        int fila = 1;
        for (Cerveza c : cervezas) {
            Row row = sheet.createRow(fila++);
            row.createCell(0).setCellValue(c.getId());
            row.createCell(1).setCellValue(c.getNombreCerveza());
            row.createCell(2).setCellValue(c.getTipoCerveza());
            row.createCell(3).setCellValue(c.getGradoAlcoholico());
            row.createCell(4).setCellValue(c.getAmargorIbu());
            row.createCell(5).setCellValue(c.getPrecioPorLitro());
            row.createCell(6).setCellValue(c.getEstado());
            row.createCell(7).setCellValue(c.getDescripcion());
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}


}
