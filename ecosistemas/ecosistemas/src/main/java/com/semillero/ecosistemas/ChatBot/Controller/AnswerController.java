package com.semillero.ecosistemas.ChatBot.Controller;

import com.semillero.ecosistemas.ChatBot.Model.Answer;
import com.semillero.ecosistemas.ChatBot.Service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@Validated
@Tag(name = "Answer", description = "Listado de la entidad Answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    //Listado de Respuestas
    @Operation(summary = "Obtener lista de todas las Respuestas", description = "Devuelve el listado de todas las Respuestas.")
    @ApiResponse(responseCode = "200", description = "Listado de Respuestas obtenido exitosamente.")
    @GetMapping
    public List<Answer> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    //Devuelve una Respuesta por ID
    @Operation(summary = "Obtener una Respuesta por ID", description = "Devuelve una Respuesta guardada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/{id}")
    public Answer getAnswerById(@PathVariable Long id) {
        return answerService.getAnswerById(id);
    }

    //Devuelve la Respuesta mediante ID de pregunta
    @Operation(summary = "Obtener Respuesta por ID de pregunta", description = "Devuelve la Respuesta mediante ID de pregunta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @GetMapping("/question/{questionId}")
    public Answer getAnswerByQuestion(@PathVariable Long questionId) {
        return answerService.getAnswerByQuestionId(questionId);
    }

    //Crea nueva Respuesta
    @Operation(summary = "Crear nueva Respuesta", description = "Crea una nueva Respuesta.")
    @ApiResponse(responseCode = "201", description = "Respuesta creada exitosamente")
    @PostMapping
    public Answer createAnswer(@RequestBody Answer answer) {
        return answerService.saveAnswer(answer);
    }

    //Editar Respuesta por ID
    @Operation(summary = "Actualizar una Respuesta", description = "Actualiza una Respuesta por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados.")
    })
    @PutMapping("/{id}")
    public Answer updateAnswer(@PathVariable Long id, @RequestBody Answer answer) {
        answer.setId(id);
        return answerService.saveAnswer(answer);
    }

    //Elimina Respuesta por ID
    @Operation(summary = "Eliminar una Respuesta por ID", description = "Elimina la Respuesta mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "Respuesta no encontrada.")
    })
    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
    }
}