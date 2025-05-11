package br.com.fiap.quod_app.domain;

import br.com.fiap.quod_app.dto.ImagemDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "validations")
@Data// Gera getters, setters, toString, etc.
@AllArgsConstructor
@NoArgsConstructor
public class ImagemEntity {

    @Id
    private String id;

    private byte[] imagem;

    private TipoValidacao tipo;

    private boolean fraudeDetectada; // <-- Lombok gera setFraudeDetectada(boolean)

    private LocalDateTime dataHoraProcessamento;

    private Map<String, Object> metadados;

    private String referenciaNotificacao;

    public ImagemEntity(ImagemDto imagemDto) throws IOException {
        this.imagem = imagemDto.imagem().getBytes();
        this.tipo = imagemDto.tipo();
        this.dataHoraProcessamento = LocalDateTime.now();

    }

    public void setFraudeDetectada(boolean fraudeDetectada) {
        this.fraudeDetectada = fraudeDetectada;
    }

    public boolean isFraudeDetectada() {
        return this.fraudeDetectada;
    }

    public void setDataHoraProcessamento(LocalDateTime dataHoraProcessamento) {
        this.dataHoraProcessamento = dataHoraProcessamento;
    }

}





