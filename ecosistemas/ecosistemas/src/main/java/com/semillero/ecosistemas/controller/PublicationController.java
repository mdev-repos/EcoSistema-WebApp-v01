package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.model.Publication;
import com.semillero.ecosistemas.service.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/publications")
@Validated
@Tag(name = "Publicacion", description = "Listado de operaciones de la entidad Publicacion")
public class PublicationController {
    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    // CREAR UNA NUEVA PUBLICACION
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva publicacion", description = "Realiza la creacion de una publicacion previamente validada")
    @ApiResponse(responseCode = "201", description = "Publicación creada exitosamente")
    @PostMapping
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Publication> createPublication(@Valid @ModelAttribute PublicationDTO publicationDTO, @RequestParam List<MultipartFile> files, @RequestHeader("Authorization") String token) {
        try {
            String finalToken = token.replace("Bearer ", "");
            Publication savedPublication = publicationService.savePublication(publicationDTO, files, finalToken);
            return new ResponseEntity<>(savedPublication, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ACTUALIZAR UNA PUBLICACION POR ID
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una publicación", description = "Actualiza una publicación recibiendo el ID de la publicación a modificar, la publicación nueva, y el listado de URLs de las imágenes a eliminar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publicación actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Publicación no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(@Valid @PathVariable Long id, @ModelAttribute PublicationDTO publicationDTO, @RequestParam(required = false) List<MultipartFile> files, @RequestHeader("Authorization") String token) {
        try {
            String finalToken = token.replace("Bearer ", "");
            Publication updatedPublication = publicationService.updatePublication(id, publicationDTO, files, finalToken);
            return new ResponseEntity<>(updatedPublication, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // OBTENER LAS PUBLICACIONES ACTIVAS Y NO ACTIVAS
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todas las publicaciones", description = "Devuelve el listado de todas las publicaciones (Activas y No Activas)")
    @ApiResponse(responseCode = "200", description = "Listado de publicaciones obtenido exitosamente.")
    @GetMapping
    public ResponseEntity<List<Publication>> getAllPublications() {
        try {
            List<Publication> listPublications = publicationService.getAllPublications();
            return new ResponseEntity<>(listPublications, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // OBTENER UNA PUBLICACION (SIN MODIFICAR VIEWS)
    @Operation(summary = "Obtener una publicacion por ID", description = "Devuelve una publicación guardada (Sin modificar las views).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publicación obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Publicación no encontrada.")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Publication> getOnePublicationById(@PathVariable @Valid Long id) {
        try {
            return new ResponseEntity<>(publicationService.getOnePublicationById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // OBTENER UNA PUBLICACION (INCREMENTA EN 1 LAS VIEWS SI ES USER O SUPPLIER Y NO INCREMENTA SI ES ADMIN)
    @Operation(summary = "Incrementar las views por ID", description = "Incrementa las views en 1 si es USER o SUPPLIER. No incrementa las views si es ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publicación obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Publicación no encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublication(@PathVariable @Valid Long id, @RequestHeader(value = "Authorization", required = false) String token) throws Exception {
        try {
            return new ResponseEntity<>(publicationService.getPublicationById(id, token), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // OBTENER LAS PUBLICACIONES ACTIVAS
    @Operation(summary = "Obtener las publicaciones activas", description = "Devuelve el listado de todas las publicaciones activas.")
    @ApiResponse(responseCode = "200", description = "Listado de publicaciones activas obtenido exitosamente.")
    @GetMapping("/active")
    public ResponseEntity<List<Publication>> getAllActivePublications() {
        try {
            List<Publication> listActivePublications = publicationService.getAllActivePublications();
            return new ResponseEntity<>(listActivePublications, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // CAMBIAR EL ESTADO DE UNA PUBLICACACION A 'DELETED' (OCULTO) O 'ACTIVA'
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una publicación mediante su ID", description = "Cambia el estado de una publicacion a oculta (borrado virtual)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publicación eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Publicación no encontrada.")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Publication> markAsDeleted(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(publicationService.markAsDeleted(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // OBTENER LAS 3 ULTIMAS PUBLICACIONES ACTIVAS ORDENADAS DESDE LA MAS RECIENTE HASTA LA MAS ANTIGUA
    @Operation(summary = "Obtener las últimas 3 publicaciones", description = "Devuelve las últimas 3 publicaciones activas ordenadas por fecha de mayor a menor")
    @ApiResponse(responseCode = "200", description = "Listado de publicaciones obtenido exitosamente.")
    @GetMapping("/last-three")
    public ResponseEntity<List<Publication>> getLastThreePublications() {
        try {
            List<Publication> publicationList = publicationService.getLastThreePublications();
            return new ResponseEntity<>(publicationList, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

