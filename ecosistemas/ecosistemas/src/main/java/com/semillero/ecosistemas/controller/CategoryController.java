package com.semillero.ecosistemas.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.semillero.ecosistemas.model.Category;
import com.semillero.ecosistemas.service.ICategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Validated
@Tag(name = "Category", description = "Listado de la entidad Category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    //Crear nueva Categoría
    @PreAuthorize("hasRole('ADMIN')") //Requiere Token
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Crear nueva Categoría", description = "Crea una nueva Categoría.")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody Category category){
        Category newCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(newCategory);
    }

    //Trae lista de Categorias
    @Operation(summary = "Obtener la lista de todas las Categorías", description = "Devuelve el listado de todas las Categorías.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de Categoría obtenido exitosamente."),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}