package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Province;
import com.semillero.ecosistemas.service.IProvinceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@Validated
@Tag(name = "Province", description = "Listado de la entidad Province")
public class ProvinceController {

    @Autowired
    private IProvinceService provinceService;

    //Muestra una provincia por ID
    @Operation(summary = "Obtener una Provincia por ID", description = "Devuelve una Provincia guardada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provincia obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Provincia no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/{provinceID}")
    public Province getProvinceById(@PathVariable Long provinceID){
        return provinceService.getProvinceById(provinceID);
    }

    //Lista de Provincias por ID de Pais
    @Operation(summary = "Obtener Listado de Provincias por ID de País", description = "Devuelve un listado de Provincias por ID de País.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/country/{countryID}")
    public List<Province> getProvincesByCountryId(@PathVariable Long countryID) {
        return provinceService.getProvincesByCountryId(countryID);
    }
}