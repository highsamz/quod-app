package br.com.fiap.quod_app.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImagemDto (
        String tipo, // "facial", "digital" ou "documento"
        MultipartFile imagem
) {}
