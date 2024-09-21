package com.semillero.ecosistemas.ChatBot.Controller;

import com.semillero.ecosistemas.ChatBot.Model.Question;
import com.semillero.ecosistemas.ChatBot.Service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Validated
@Tag(name = "Question", description = "Listado de la entidad Question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    //Listado de preguntas
    @Operation(summary = "Obtener lista de todas las Preguntas", description = "Devuelve el listado de todas las Preguntas.")
    @ApiResponse(responseCode = "200", description = "Listado de Preguntas obtenido exitosamente.")
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    //Devuelve una Pregunta guardada por ID
    @Operation(summary = "Obtener una Pregunta por ID", description = "Devuelve una Pregunta guardada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pregunta obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    //Devuelve el listado de todas las Preguntas de una Categoría específica
    @Operation(summary = "Obtener todas las Preguntas por ID de Categoría", description = "Devuelve el listado de todas las Preguntas de una Categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de Preguntas obtenido exitosamente."),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/category/{categoryId}")
    public List<Question> getQuestionsByCategory(@PathVariable Long categoryId) {
        return questionService.getQuestionsByCategory(categoryId);
    }

    //Crea nueva Pregunta
    @Operation(summary = "Crear nueva Pregunta", description = "Crea una nueva Pregunta.")
    @ApiResponse(responseCode = "201", description = "Pregunta creada exitosamente")
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    //Editar pregunta por ID
    @Operation(summary = "Actualizar una Pregunta", description = "Actualiza una Pregunta por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pregunta actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        return questionService.saveQuestion(question);
    }

    //Elimina Pregunta por ID
    @Operation(summary = "Eliminar una Pregunta por ID", description = "Elimina la Pregunta mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pregunta eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Pregunta no encontrada.")
    })
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}