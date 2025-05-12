package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.domain.TipoValidacao;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.repository.ValidacaoRepository;
import br.com.fiap.quod_app.utils.ImagemMetadataUtil;
import br.com.fiap.quod_app.utils.ValidacaoFraudeUtil;
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

    public ImagemEntity salvar(ImagemDto imagemDto) throws IOException {
        ImagemEntity imagemEntity = new ImagemEntity(imagemDto);
        Map<String, String> metadados = ImagemMetadataUtil.extrairMetadados(imagemDto.imagem());
        boolean fraude = ValidacaoFraudeUtil.verificarFraudePorMetadados(metadados);

        // Log para debug
        metadados.forEach((chave, valor) ->
                System.out.println("Meta: " + chave + " = " + valor)
        );

        var temRosto = false;
        if (imagemDto.tipo().equals(TipoValidacao.FACIAL)) {
            temRosto = detectarRosto(imagemDto.imagem());
            imagemEntity.setFraudeDetectada(!temRosto);
        }

        imagemEntity.setDataHoraProcessamento(LocalDateTime.now());

        if (fraude) {
            // Enviar notificação (simulado por log ou HTTP)
            System.out.println("⚠️ Fraude detectada! Notificando sistema interno...");
        }
        return validacaoRepository.save(imagemEntity);
    }

    private boolean detectarRosto(MultipartFile imagem) throws IOException {
        File tempFile = File.createTempFile("imagem", ".png");
        imagem.transferTo(tempFile); // mais seguro
        Mat imagemMat = Imgcodecs.imread(tempFile.getAbsolutePath());
        if (imagemMat.empty()) {
            tempFile.delete();
            System.err.println("Erro ao carregar a imagem no OpenCV.");
            return false;
        }

        Mat imagemCinza = new Mat();
        Imgproc.cvtColor(imagemMat, imagemCinza, Imgproc.COLOR_BGR2GRAY);

        // Carrega o classificador Haarcascade via classpath
        InputStream xmlStream = getClass().getResourceAsStream("/classifiers/haarcascade_frontalface_default.xml");
        if (xmlStream == null) throw new IllegalStateException("Classificador XML não encontrado no classpath.");

        File tempXml = File.createTempFile("haarcascade", ".xml");
        tempXml.deleteOnExit();
        Files.copy(xmlStream, tempXml.toPath(), StandardCopyOption.REPLACE_EXISTING);

        CascadeClassifier detector = new CascadeClassifier(tempXml.getAbsolutePath());
        MatOfRect rostosDetectados = new MatOfRect();
        detector.detectMultiScale(imagemCinza, rostosDetectados);

        tempFile.delete();

        return rostosDetectados.toArray().length > 0;
    }

    public static Map<String, String> extrairMetadados(MultipartFile imagem) {
        Map<String, String> metadados = new HashMap<>();

        try (InputStream inputStream = imagem.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    metadados.put(tag.getTagName(), tag.getDescription());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler os metadados da imagem: " + e.getMessage(), e);
        }

        return metadados;
    }
}

