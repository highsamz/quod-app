package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.dto.FraudeRequest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AcionarEndpointService {

    private final WebClient webClient;

    public AcionarEndpointService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:3000")
                .build();
    }

    public Mono<Void> notificarFraude(FraudeRequest request) {
        return webClient.post()
                .uri("/api/notificacoes/fraude")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> System.err.println("Erro ao notificar fraude: " + e.getMessage()));
    }

    public FraudeRequest construirFraudeRequest(
            String tipoBiometria,
            String tipoFraude,
            Map<String, String> metadadosExtraidos
    ) {
        String transacaoId = UUID.randomUUID().toString();
        LocalDateTime dataCaptura = LocalDateTime.now();

        String fabricante = metadadosExtraidos.getOrDefault("Make", null);
        String modelo = metadadosExtraidos.getOrDefault("Model", null);
        String sistemaOperacional = metadadosExtraidos.getOrDefault("Software", null);

        String ipOrigem = metadadosExtraidos.getOrDefault("ipOrigem", null);
        Double latitude = parseDouble(metadadosExtraidos.get("latitude"));
        Double longitude = parseDouble(metadadosExtraidos.get("longitude"));

        return new FraudeRequest(
                transacaoId,
                tipoBiometria,
                tipoFraude,
                dataCaptura.toString(),
                new FraudeRequest.Dispositivo(fabricante, modelo, sistemaOperacional),
                List.of("sms", "email"),
                "sistema-de-monitoramento",
                new FraudeRequest.Metadados(latitude, longitude, ipOrigem)
        );
    }

    private Double parseDouble(String valor) {
        try {
            return valor != null ? Double.parseDouble(valor.replace(",", ".")) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}