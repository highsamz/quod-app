package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.domain.TipoValidacao;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.repository.ValidacaoRepository;
import br.com.fiap.quod_app.utils.*;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidacaoService {

    @Autowired
    private ValidacaoRepository validacaoRepository;
    private static String pastaReferencias = "src/main/resources/digitalreferences";

    public ImagemEntity salvar(ImagemDto imagemDto) throws IOException {
        ImagemEntity imagemEntity = new ImagemEntity(imagemDto);
        imagemEntity.setDataHoraProcessamento(LocalDateTime.now());

        // Extrair metadados
        Map<String, String> metadados = ImagemMetadataUtil.extrairMetadados(imagemDto.imagem());
        metadados.forEach((chave, valor) ->
                System.out.println("Meta: " + chave + " = " + valor)
        );

        boolean fraudeDetectada = false;

        if (ValidacaoFraudeUtil.verificarFraudePorMetadados(metadados)) {
            fraudeDetectada = true;
            System.out.println("Fraude por metadados detectada.");
        }


        if (imagemDto.tipo().equals(TipoValidacao.FACIAL)) {
            boolean temRosto = ValidacaoRostoUtil.detectarRosto(imagemDto.imagem());
            if (!temRosto) {
                fraudeDetectada = true;
                System.out.println("Fraude por ausência de rosto detectada.");
            }
        } else if (imagemDto.tipo().equals(TipoValidacao.DIGITAL)) {
            if (!ValidacaoDigitalUtil.validarDigitalPorComparacao(imagemDto.imagem(), pastaReferencias)) {
                fraudeDetectada = true;
                System.out.println("Fraude por digital detectada.");
            }
        } else if (imagemDto.tipo().equals(TipoValidacao.DOCUMENTO)) {
            if (!ValidacaoDocumentoUtil.validarDocumentoPorComparacao(imagemDto.imagem(), pastaReferencias)) {
                fraudeDetectada = true;
                System.out.println("Fraude por documento detectada.");
            }
        }


        imagemEntity.setFraudeDetectada(fraudeDetectada);

        if (fraudeDetectada) {
            //notificarSistemaFraude(imagemDto); // chama API
        }

        return validacaoRepository.save(imagemEntity);
    }
}

