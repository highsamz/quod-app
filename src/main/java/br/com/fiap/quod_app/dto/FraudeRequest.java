package br.com.fiap.quod_app.dto;

import java.util.List;

public record FraudeRequest(
        String transacaoId,
        String tipoBiometria,
        String tipoFraude,
        String dataCaptura,
        Dispositivo dispositivo,
        List<String> canalNotificacao,
        String notificadoPor,
        Metadados metadados
) {
    public record Dispositivo(
            String fabricante,
            String modelo,
            String sistemaOperacional
    ) {}

    public record Metadados(
            Double latitude,
            Double longitude,
            String ipOrigem
    ) {}
}


