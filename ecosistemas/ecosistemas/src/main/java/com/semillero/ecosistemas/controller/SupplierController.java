package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Supplier;
import com.semillero.ecosistemas.service.ISupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Proveedor", description = "Listado de operaciones de la entidad (Usuario) Proveedor")
public class SupplierController {
    @Autowired
    ISupplierService supplierService;

    //SAVE A SUPPLIER (REGISTRY ENDPOINT) / CREAR UN PROVEEDOR - FORMULARIO DE REGISTRO (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Crear nuevo usuario Proveedor", description = "Realiza la creacion de un nuevo usuario Proveedor.")
    @ApiResponse(responseCode = "200", description = "Proveedor creado exitosamente")
    @PostMapping
    public ResponseEntity<Supplier> saveSupplier(@RequestBody Supplier supplier) {
        Supplier newSupplier = supplierService.saveSupplier(supplier);
        return ResponseEntity.ok(newSupplier);
    }

    //RETURN ALL SUPPLIERS (ADMIN) / OBTENER TODOS LOS PROVEEDORES (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los usuarios Proveedores", description = "Devuelve el listado de todos los susuarios de tipo Proveedor.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }

    //FIND A SUPPLIER BY EMAIL / OBTENER PROVEEDOR POR EMAIL (CUALQUIERA SIN LOGUEARSE)
    @Operation(summary = "Obtener un Proveedor por Email", description = "Devuelve al Proveedor con el Email ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor obtenido exitosamente."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.")
    })
    @GetMapping("/find/{email}")
    public ResponseEntity<Supplier> getSupplierByEmail(@PathVariable String email){
        Optional<Supplier> optionalSupplier = supplierService.findSupplierByEmail(email);
        return optionalSupplier.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    //DEACTIVATE A SUPPLIER ACCOUNT (ADMIN) / DESACTIVAR CUENTA DE PROVEEDOR (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar una cuenta Proveedor", description = "Permite desactivar una cuenta Proveedor mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor desactivado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.")
    })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Supplier> deactivateSupplier(@PathVariable Long id) {
        Optional<Supplier> supplierOptional = supplierService.findSupplierById(id);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            supplierService.deactivateSupplier(supplier);
            supplierService.saveSupplier(supplier);
            return ResponseEntity.ok(supplier);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}