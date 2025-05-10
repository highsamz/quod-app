package br.com.fiap.quod_app.domain;

import br.com.fiap.quod_app.dto.ImagemDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "validations")
@Data
@Getter
@Setter
@AllArgsConstructor
public class ImagemEntity {

    @Id
    private String id;

    private byte[] imagem; // Binário da imagem capturada (face, digital ou documento)

    private String tipo; // "facial", "digital", "documento"

    private boolean fraudeDetectada;

    private LocalDateTime dataHoraProcessamento;

    private Map<String, Object> metadados; // Ex: qualidade, fabricante, localização etc.

    private String referenciaNotificacao;

    public ImagemEntity (ImagemDto imagemDto) throws IOException {
        this.imagem = imagemDto.imagem().getBytes();
        this.tipo = imagemDto.tipo();
    }
}
