package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.domain.TipoValidacao;
import br.com.fiap.quod_app.dto.FraudeRequest;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.repository.ValidacaoRepository;
import br.com.fiap.quod_app.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ValidacaoService {

    @Autowired
    private ValidacaoRepository validacaoRepository;
    @Autowired
    private AcionarEndpointService acionarEndpointService;


    private static final String pastaReferenciasDigital = "src/main/resources/digitalreferences";
    private static final String pastaReferenciasDocumentos = "src/main/resources/documentoreferences";

    public ImagemEntity salvar(ImagemDto imagemDto) throws IOException {
        ImagemEntity imagemEntity = new ImagemEntity(imagemDto);
        imagemEntity.setDataHoraProcessamento(LocalDateTime.now());

        // Extrair metadados
        Map<String, String> metadados = ImagemMetadataUtil.extrairMetadados(imagemDto.imagem());
        metadados.forEach((chave, valor) ->
                System.out.println("Meta: " + chave + " = " + valor)
        );

        boolean fraudeDetectada = false;


        if (imagemDto.tipo().equals(TipoValidacao.FACIAL)) {
            boolean temRosto = ValidacaoRostoUtil.detectarRosto(imagemDto.imagem());
            if (ValidacaoFraudeUtil.verificarFraudePorMetadados(metadados)) {
                fraudeDetectada = true;
                System.out.println("Fraude por metadados detectada.");
            }
            if (!temRosto) {
                fraudeDetectada = true;
                System.out.println("Fraude por ausência de rosto detectada.");

            }
        } else if (imagemDto.tipo().equals(TipoValidacao.DIGITAL)) {
            if (!ValidacaoDigitalUtil.validarDigitalPorComparacao(imagemDto.imagem(), pastaReferenciasDigital)) {
                fraudeDetectada = true;
                System.out.println("Fraude por digital detectada.");
            }
        } else if (imagemDto.tipo().equals(TipoValidacao.DOCUMENTO)) {
            if (!ValidacaoDocumentoUtil.validarDocumentoPorComparacao(imagemDto.imagem(), pastaReferenciasDocumentos)) {
                fraudeDetectada = true;
                System.out.println("Fraude por documento detectada.");
            }
        }


        imagemEntity.setFraudeDetectada(fraudeDetectada);

        if (fraudeDetectada) {
            FraudeRequest request = acionarEndpointService.construirFraudeRequest(
                    imagemDto.tipo().name().toLowerCase(),
                    "digital-fraud",
                    metadados
            );

            acionarEndpointService.notificarFraude(request).subscribe();
        }

        return validacaoRepository.save(imagemEntity);
    }
}

