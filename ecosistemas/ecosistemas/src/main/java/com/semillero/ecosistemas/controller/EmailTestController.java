package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.service.EmailSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@Validated
@Tag(name = "Email-Test", description = "Envía Correo notificando los productos agregados recientemente a los Admin & Supplier ")
public class EmailTestController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Operation(summary = "Envía mail a los Admin", description = "Envía correo a los admin notificando la ultima actualización de los productos.")
    @ApiResponse(responseCode = "201", description = "Correo creado exitosamente")
    @PostMapping("/admin")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email, @RequestParam String nombre) {
        try {
            emailSenderService.sendAdminEmail(email, nombre);
            return ResponseEntity.ok("Correo para los Admins enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo para los Admins.");
        }
    }

    @Operation(summary = "Envía mail a los Supplier", description = "Envía correo a los Supplier notificando los productos que se agregaron en la semana.")
    @ApiResponse(responseCode = "201", description = "Correo creado exitosamente")
    @PostMapping("/supplier")
    public ResponseEntity<String> sendSupplierEmail(@RequestParam String email, @RequestParam String nombre) {
        try {
            emailSenderService.sendSupplierEmail(email, nombre);
            return ResponseEntity.ok("Correo para los Usuarios enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar correo para los usuarios.");
        }
    }
}