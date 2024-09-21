package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Product;
import com.semillero.ecosistemas.service.DistanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/distance")
@Validated
@Tag(name = "Distance", description = "Devuelve los proveedores más cercanos según la ubicación actual del usuario")
public class DistanceController {

    @Autowired
    RestTemplate restTemplate;
    private final DistanceService distanceService;

    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    //Muestra los proveedores cercanos
    @Operation(summary = "Muestra los proveedores más cercanos de la zona", description = "Devuelve los proveedores más cercanos según la ubicación actual del usuario")
    @ApiResponse(responseCode = "201", description = "Info cargada exitosamente")
    @GetMapping("/calculate")
    public List<Product> calculateTop5ShortestDistances(@RequestParam double lat, @RequestParam double lon) {
        return distanceService.calculateTop5ShortestDistances(lat, lon);
    }
}