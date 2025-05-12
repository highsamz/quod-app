package br.com.fiap.quod_app.dto;

import br.com.fiap.quod_app.domain.TipoValidacao;
import org.springframework.web.multipart.MultipartFile;



public record ImagemDto (
        TipoValidacao tipo,
        MultipartFile imagem
) {}


