package com.sca.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sca.model.Madurador;
import com.sca.model.Respuesta;
import com.sca.service.impl.MaduradorServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Madurador")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class MaduradorController {

	// Logger log = LoggerFactory.getLogger(String.class);
	
	@Autowired
	MaduradorServiceImpl maduradorsServiceImpl;
	
	@PostMapping(value = "/addMadurador", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Agrega un Madurador", notes = "Esta operación agrega un Madurador a la base de datos")
	public ResponseEntity<Object> addMadurador(@RequestBody @Validated Madurador madurador, BindingResult bindingResult) throws BindException{
		return maduradorsServiceImpl.save(madurador,bindingResult);
	}
	
	@GetMapping(value = "/getAllMadurador", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Madurador", notes = "Esta operación devuelve todos los Madurador a la base de datos")
	public Respuesta getAllMadurador() {
		return maduradorsServiceImpl.findAll();
	}
	
	@GetMapping(value = "/getByIdMadurador/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Madurador por id", notes = "Esta operación consulta un Madurador por su identificador personal")
	public Respuesta getByIdMadurador(@PathParam("id") @PathVariable Long id) {
		return maduradorsServiceImpl.findById(id);
	}
	
	@DeleteMapping(value = "/deleteMadurador/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Eliminar un Maduradors", notes = "Esta operación elimina un Madurador de la base de datos")
	public Respuesta deleteMadurador(@PathParam("id") @PathVariable Long id) {
		return maduradorsServiceImpl.delete(id);
	}
	
	@PutMapping(value = "/updateMadurador", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Actualizar un Madurador", notes = "Esta operación actualiza un Madurador a la base de datos")
	public ResponseEntity<Object> updateMadurador(@RequestBody Madurador madurador, BindingResult bindingResult) throws BindException {
		return maduradorsServiceImpl.update(madurador, bindingResult);
	}

	@GetMapping(value = "/finByEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Consultar Maduradores por Estado", notes = "Esta operación devuelve todos los Maduradores filtrados por estado")
	public Respuesta finByEstado(@PathVariable String estado) {
    	return maduradorsServiceImpl.finByEstado(estado);
	}


	@GetMapping(value = "/maduradores/exportarExcel/{estado}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public ResponseEntity<byte[]> exportarMaduradoresPorEstadoExcel(@PathVariable String estado) {
    try {
        List<Madurador> maduradores = maduradorsServiceImpl.finByEstado(estado).getData() instanceof List<?> list ? 
            (List<Madurador>) list : List.of();

        // Crear Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Maduradores");

        // Cabecera
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Litros");
        header.createCell(2).setCellValue("Estado");
        header.createCell(3).setCellValue("Notas");
        header.createCell(4).setCellValue("Lote");

        // Filas
        int rowIdx = 1;
        for (Madurador m : maduradores) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(m.getId() != null ? m.getId() : 0);
            row.createCell(1).setCellValue(m.getLitros() != null ? m.getLitros() : 0);
            row.createCell(2).setCellValue(m.getEstado() != null ? m.getEstado() : "");
            row.createCell(3).setCellValue(m.getNotas() != null ? m.getNotas() : "");
            row.createCell(4).setCellValue(m.getLote() != null && m.getLote().getId() != null ? m.getLote().getId() : 0);
        }

        workbook.write(out);
        workbook.close();

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=maduradores_" + estado + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out.toByteArray());

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


}
