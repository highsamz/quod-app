package br.com.fiap.quod_app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ValidacaoFraudeUtil {

    public static boolean verificarFraudePorMetadados(Map<String, String> metadados) {
        // 1. Verifica se campos obrigatórios estão ausentes
        if (!metadados.containsKey("Date/Time") || !metadados.containsKey("Make") || !metadados.containsKey("Model")) {
            System.out.println("Metadados incompletos.");
            return true;
        }

        // 2. Valida a data da imagem
        String dataStr = metadados.get("Date/Time");
        if (dataStr != null && !dataStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                Date dataImagem = sdf.parse(dataStr);
                Date agora = new Date();

                // Data no futuro?
                if (dataImagem.after(agora)) {
                    System.out.println("Data da imagem está no futuro.");
                    return true;
                }

                // Muito antiga? (exemplo: mais de 5 anos)
                Calendar cincoAnosAtras = Calendar.getInstance();
                cincoAnosAtras.add(Calendar.YEAR, -5);
                if (dataImagem.before(cincoAnosAtras.getTime())) {
                    System.out.println("Imagem muito antiga.");
                    return true;
                }

            } catch (ParseException e) {
                System.out.println("Erro ao converter data: " + e.getMessage());
                return true;
            }
        }

        // 3. Verifica GPS (opcional)
        if (!metadados.containsKey("GPS Latitude") || !metadados.containsKey("GPS Longitude")) {
            System.out.println("Localização ausente.");
            // return true; // Ative se GPS for obrigatório
        }

        // 4. Verifica compressão ou orientação
        String orientacao = metadados.getOrDefault("Orientation", "unknown");
        if (orientacao.equalsIgnoreCase("0") || orientacao.equalsIgnoreCase("unknown")) {
            System.out.println("Orientação inválida.");
            return true;
        }

        return false; // Sem fraude detectada
    }
}

