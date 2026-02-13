package com.sca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sca.dto.BarrilDTO;
import com.sca.model.Barril;
import com.sca.model.Lote;
import com.sca.model.Respuesta;
import com.sca.service.impl.BarrilServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "Barril")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class BarrilController {

    @Autowired
    private BarrilServiceImpl barrilServiceImpl;

    @PostMapping(value = "/addBarril", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un Barril", notes = "Esta operación agrega un Barril a la base de datos")
    public ResponseEntity<Object> addBarril(@RequestBody @Validated Barril barril,
        BindingResult bindingResult) throws BindException {
        return barrilServiceImpl.save(barril, bindingResult);
    }

    @GetMapping(value = "/getAllBarril", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles", notes = "Devuelve todos los Barriles de la base de datos")
    public Respuesta getAllBarril() {
        return barrilServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdBarril/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barril por ID", notes = "Consulta un Barril por su identificador")
    public Respuesta getByIdBarril(@PathVariable Long id) {
        return barrilServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deleteBarril/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar Barril", notes = "Elimina un Barril de la base de datos")
    public Respuesta deleteBarril(@PathVariable Long id) {
        return barrilServiceImpl.delete(id);
    }

    @PutMapping(value = "/updateBarril", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualizar Barril", notes = "Actualiza un Barril en la base de datos")
    public ResponseEntity<Object> updateBarril(@RequestBody Barril barril,
        BindingResult bindingResult) throws BindException {
        return barrilServiceImpl.update(barril, bindingResult);
    }

    @GetMapping(value = "/findByEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles por Estado", notes = "Devuelve todos los Barriles filtrados por estado")
    public Respuesta findByEstado(@PathVariable String estado) {
        return barrilServiceImpl.findByEstado(estado);
    }

    @GetMapping(value = "/findByLote/{lote}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles por Lote", notes = "Devuelve todos los Barriles filtrados por lote")
    public Respuesta findByLote(@PathVariable Lote lote) {
        return barrilServiceImpl.findByLote(lote);
    }

    @GetMapping(value = "/getBarrilesByCerveza/{cervezaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles por Cerveza y Estado",
            notes = "Devuelve todos los barriles cargados de la cerveza seleccionada")
    public ResponseEntity<List<BarrilDTO>> getBarrilesByCerveza(@PathVariable Long cervezaId) {
        return ResponseEntity.ok(barrilServiceImpl.findByCervezaAndEstadoDTO(cervezaId, "Cargado"));
    }

    @GetMapping(value = "/getBarrilesDisponibles/{cervezaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles Disponibles", notes = "Devuelve los barriles disponibles por cerveza")
    public Respuesta getBarrilesDisponibles(@PathVariable Long cervezaId) {
        return barrilServiceImpl.findDisponiblesByCerveza(cervezaId);
    }

    @GetMapping(value = "/getBarrilesDisponiblesDTO/{cervezaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar Barriles disponibles (DTO) por Cerveza",
            notes = "Devuelve todos los barriles en estado 'Cargado' de la cerveza seleccionada en formato DTO")
    public ResponseEntity<List<BarrilDTO>> getBarrilesDisponiblesDTO(@PathVariable Long cervezaId) {
        return ResponseEntity.ok(barrilServiceImpl.findDisponiblesDTOByCerveza(cervezaId));
    }


    @GetMapping(value = "/barriles/buscarPorId")
    public String buscarPorId(@RequestParam Long id, org.springframework.ui.Model model) {
        Respuesta respuesta = barrilServiceImpl.findById(id);
        model.addAttribute("items", respuesta.getData() != null ? List.of(respuesta.getData()) : List.of());
        return "barril/fragments :: lista";
    }

    @GetMapping(value = "/barril/findByEstado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBarrilesByEstado(@PathVariable String estado) {
        Respuesta respuesta = barrilServiceImpl.findByEstado(estado);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/barriles/exportarExcel/{estado}")
    public ResponseEntity<byte[]> exportarBarrilesPorEstadoExcel(@PathVariable String estado) {
        return barrilServiceImpl.exportarBarrilesPorEstadoExcel(estado);
    }



}
