package com.sca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sca.model.Respuesta;
import com.sca.model.SueldoBasico;
import com.sca.service.impl.SueldoBasicoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@Api(tags = "SueldoBasico")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class SueldoBasicoController {

    @Autowired
    private SueldoBasicoServiceImpl sueldoBasicoServiceImpl;

    @PostMapping(value = "/addSueldoBasico", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega un SueldoBasico", notes = "Esta operación agrega un SueldoBasico a la base de datos")
    @ResponseBody
    public ResponseEntity<Object> addSueldoBasico(@RequestBody @Validated SueldoBasico sueldoBasico, BindingResult bindingResult) throws BindException {
        return sueldoBasicoServiceImpl.save(sueldoBasico, bindingResult);
    }

    // ENDPOINTS PARA EL FRONTEND

    @PostMapping(value = "/sueldos-basicos/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> saveSueldoBasico(@RequestBody @Validated SueldoBasico sueldoBasico, BindingResult bindingResult) throws BindException {
        return sueldoBasicoServiceImpl.save(sueldoBasico, bindingResult);
    }

    @PutMapping(value = "/sueldos-basicos/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Actualizar un SueldoBasico", notes = "Esta operación actualiza un SueldoBasico a la base de datos")
    public ResponseEntity<Object> updateSueldoBasico(@RequestBody @Validated SueldoBasico sueldoBasico, BindingResult bindingResult) throws BindException {
        return sueldoBasicoServiceImpl.update(sueldoBasico, bindingResult);
    }

    @GetMapping(value = "/sueldos-basicos/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Respuesta listSueldoBasicos() {
        return sueldoBasicoServiceImpl.findAll();
    }

    @GetMapping(value = "/sueldos-basicos/form-html")
    public String getFormSueldoBasico(Model model) {
        model.addAttribute("sueldo", new SueldoBasico());
        // No se agregan categorias ni porcentajes por ahora
        return "fragments :: form"; // Devuelve el fragmento HTML
    }

    @GetMapping(value = "/sueldos-basicos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Respuesta getSueldoBasicoById(@PathVariable Long id) {
        return sueldoBasicoServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deleteSueldo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Respuesta deleteSueldo(@PathVariable Long id) {
        return sueldoBasicoServiceImpl.delete(id);
    }

    @GetMapping(value = "/getAllSueldoBasicos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar SueldoBasicos", notes = "Esta operación devuelve todos los SueldoBasicos a la base de datos")
    @ResponseBody
    public Respuesta getAllSueldoBasicos() {
        return sueldoBasicoServiceImpl.findAll();
    }

    @GetMapping(value = "/getByIdSueldoBasico/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consultar SueldoBasico por id", notes = "Esta operación consulta un SueldoBasico por su identificador personal")
    @ResponseBody
    public Respuesta getByIdSueldoBasico(@PathVariable Long id) {
        return sueldoBasicoServiceImpl.findById(id);
    }

    @DeleteMapping(value = "/deleteSueldoBasico/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminar un SueldoBasico", notes = "Esta operación elimina un SueldoBasico de la base de datos")
    @ResponseBody
    public Respuesta deleteSueldoBasico(@PathVariable Long id) {
        return sueldoBasicoServiceImpl.delete(id);
    }
}