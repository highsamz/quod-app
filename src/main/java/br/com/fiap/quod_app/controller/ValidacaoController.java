package br.com.fiap.quod_app.controller;


import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.domain.TipoValidacao;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.service.ValidacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ValidacaoController {

    @Autowired
    ValidacaoService validacaoService;

    @Operation(
            summary = "Valida uma imagem facial",
            description = "Detecta se há um rosto na imagem e classifica como 'fraude' ou 'válida'.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Imagem processada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImagemEntity.class)))
            }
    )
    @PostMapping(value = "/validar", consumes = "multipart/form-data")
    public ResponseEntity<?> validarImagem(@RequestParam("tipo") TipoValidacao tipo,
                                           @RequestParam("imagem") MultipartFile imagem) throws IOException {

        // Criação do DTO
        var arquivo = new ImagemDto(tipo, imagem);
        ImagemEntity imagemEntity = validacaoService.salvar(arquivo);

        // Verifica se a imagem foi marcada como fraude ou válida
        if (imagemEntity.isFraudeDetectada()) {
            // Se a fraude foi detectada, retornamos "Fraude detectada"
            return ResponseEntity.status(HttpStatus.CREATED).body("Fraude detectada. Nenhum rosto encontrado.");
        } else {
            // Se o rosto foi detectado, retornamos "Imagem válida"
            return ResponseEntity.status(HttpStatus.CREATED).body("Imagem válida. Rosto detectado.");
        }
    }
}
