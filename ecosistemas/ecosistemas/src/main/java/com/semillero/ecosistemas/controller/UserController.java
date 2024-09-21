package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.User;
import com.semillero.ecosistemas.service.IUserService;
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
@RequestMapping("/api/users")
@Tag(name = "Usuario", description = "Listado de operaciones de la entidad User")
public class UserController {

    @Autowired
    IUserService userService;

    //RETURN ALL THE USERS (ADMINS) / OBTENER TODOS LOS USUARIOS EN BBDD (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los Usuarios", description = "Retorna una lista con todos los usuarios en Base de Datos.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }

    //FIND ANY USER BY EMAIL / ENCONTRAR UN USUARIO POR EMAIL (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Encontrar User por Email", description = "Retorna al usuario con el Email ingresado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrada."),
    })
    @GetMapping("/find/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        Optional<User> optionalUser = userService.findUserByEmail(email);
        return optionalUser.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }
}