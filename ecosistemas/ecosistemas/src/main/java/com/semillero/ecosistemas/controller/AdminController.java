package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.service.IAdminService;
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
@RequestMapping("/api/admins")
@Tag(name = "Administrador", description = "Listado de operaciones de la entidad (Usuario) Admin")
public class AdminController {
    @Autowired
    IAdminService adminService;

    //SAVE AN ADMIN (ADMIN) / CREAR UN USUARIO ADMINISTRADOR (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo usuario Administrador", description = "Realiza la creacion de un nuevo usuario Administrador.")
    @ApiResponse(responseCode = "200", description = "Administrador creado exitosamente")
    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin) {
        Admin newAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.ok(newAdmin);
    }

    //RETURN ALL ADMINS (ADMIN) / OBTENER A TODOS LOS ADMINISTRADORES (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los usuarios Administradores", description = "Devuelve el listado de todos los susuarios de tipo Administrador.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    //FIND AN ADMIN BY EMAIL (ANY) / OBTENER ADMINISTRADOR POR EMAIL (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Obtener un Administrador por Email", description = "Devuelve al Administrador con el Email ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador obtenido exitosamente."),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado.")
    })
    @GetMapping("/find/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email){
        Optional<Admin> optionalAdmin = adminService.findAdminByEmail(email);
        return optionalAdmin.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    //DEACTIVATE AN ADMIN ACCOUNT (ADMIN) / DESACTIVAR CUENTA ADMINISTRADOR (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar una cuenta Administrador", description = "Permite desactivar una cuenta Administrador mediante ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador desactivado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado.")
    })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Admin> switchAdminState(@PathVariable Long id) {
        Optional<Admin> adminOptional = adminService.findAdminById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            adminService.deactivateAdmin(admin);
            adminService.saveAdmin(admin);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}