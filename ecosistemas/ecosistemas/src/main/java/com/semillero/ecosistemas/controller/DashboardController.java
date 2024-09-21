package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.dto.DashboardDTO;
import com.semillero.ecosistemas.model.Publication;
import com.semillero.ecosistemas.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Validated
@Tag(name = "Dashboard", description = "Muestra toda la info de productos y proveedores agregados recientemente y con mas vistas")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    //Muestra Total de Productos y Proveedores
    @Operation(summary = "Trae info resumida de Productos y Proveedores", description = "Muestra Total de Nuevos productos creados, Proveedores por categoría y las 5 ultimas publicaciones subidas y las 5 mas vistas.")
    @ApiResponse(responseCode = "201", description = "Info cargada exitosamente")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardInfo(){
        Map<String, Object> dashboardInfo = dashboardService.getDashboardInfo();
        return new ResponseEntity<>(dashboardInfo, HttpStatus.OK);
    }

    //Muestra el total de Proveedores de cada Categoría.
    @Operation(summary = "Trae Total de Proveedores por Categoría", description = "Muestra el total de Proveedores de cada Categoría.")
    @ApiResponse(responseCode = "201", description = "Info cargada exitosamente")
    @GetMapping("/suppliers")
    public ResponseEntity<Map<String, Long>> getSupplierCountByCategory(){
        Map<String, Long> supplierCountByCategory = dashboardService.getSupplierCountByCategory();
        return new ResponseEntity<>(supplierCountByCategory, HttpStatus.OK);
    }

}