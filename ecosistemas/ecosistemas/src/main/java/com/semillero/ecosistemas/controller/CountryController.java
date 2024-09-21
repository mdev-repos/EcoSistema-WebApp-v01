package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Country;
import com.semillero.ecosistemas.service.ICountryService;
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
@RequestMapping("/api/countries")
@Validated
@Tag(name = "Countries", description = "Listado de la entidad Country")
public class CountryController {

    @Autowired
    private ICountryService countryService;

    @Operation(summary = "Obtener Listado de Países con todas las Provincias", description = "Devuelve un listado de los Países con las provincias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }


    @Operation(summary = "Obtener un País por ID y sus Provincias", description = "Devuelve un País guardado y sus provincias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País obtenido exitosamente."),
            @ApiResponse(responseCode = "404", description = "País no encontrado."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }
}