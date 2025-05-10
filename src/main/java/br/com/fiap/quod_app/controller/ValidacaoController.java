package br.com.fiap.quod_app.controller;

import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.service.ValidacaoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @PostMapping(value = "/validar", consumes = "multipart/form-data")
    ResponseEntity<?> validarImagem( @Parameter(description = "Tipo de validação") @RequestParam("tipo") String tipo,
                                            @Parameter(description = "Imagem para validação",
                                                    content = @Content(mediaType = "application/octet-stream",
                                                            schema = @Schema(type = "string", format = "binary")))
                                            @RequestParam("imagem") MultipartFile imagem) throws IOException {
        var arquivo = new ImagemDto(tipo,imagem);
        validacaoService.salvar(arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Processado com sucesso");
    }
}
