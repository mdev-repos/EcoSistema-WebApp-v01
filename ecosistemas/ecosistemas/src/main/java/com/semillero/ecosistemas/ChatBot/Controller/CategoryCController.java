package com.semillero.ecosistemas.ChatBot.Controller;

import com.semillero.ecosistemas.ChatBot.Model.CategoryC;
import com.semillero.ecosistemas.ChatBot.Service.CategoryCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
@Validated
@Tag(name = "Categoría", description = "Listado de operaciones de la entidad Categoría")
public class CategoryCController {

    @Autowired
    private CategoryCService categoryCService;

    //Listado de Categoría
    @Operation(summary = "Obtener lista de todas las categorías", description = "Devuelve el listado de todas las Categorías.")
    @ApiResponse(responseCode = "200", description = "Listado de Categorías obtenido exitosamente.")
    @GetMapping
    public List<CategoryC> getAllCategories() {
        return categoryCService.getAllCategories();
    }

    //Trae Categoría por ID
    @Operation(summary = "Obtener una Categoría por ID", description = "Devuelve una Categoría guardada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/{id}")
    public CategoryC getCategoryById(@PathVariable Long id) {
        return categoryCService.getCategoryById(id);
    }

    //Crear Categoría
    @Operation(summary = "Crear nueva Categoría", description = "Crea una nueva Categoría.")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @PostMapping
    public CategoryC createCategory(@RequestBody CategoryC category) {
        return categoryCService.saveCategory(category);
    }

    //Actualiza Categoría por ID
    @Operation(summary = "Actualizar una Categoría", description = "Actualiza una Categoría recibiendo el ID de la Categoría a modificar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @PutMapping("/{id}")
    public CategoryC updateCategory(@PathVariable Long id, @RequestBody CategoryC category) {
        category.setId(id);
        return categoryCService.saveCategory(category);
    }

    //Elimina Categoría por ID
    @Operation(summary = "Eliminar una Categoría por ID", description = "Elimina la Categoría mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryCService.deleteCategory(id);
    }
}