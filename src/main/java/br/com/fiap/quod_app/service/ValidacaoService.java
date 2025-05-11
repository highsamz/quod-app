package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.domain.TipoValidacao;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.repository.ValidacaoRepository;
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

@Service
public class ValidacaoService {

    @Autowired
    private ValidacaoRepository validacaoRepository;

    static {
        try {
            System.out.println("Iniciando carregamento da DLL do OpenCV...");

            // Localiza a DLL no classpath
            InputStream dllStream = ValidacaoService.class.getResourceAsStream("/libs/opencv_java4110.dll");
            if (dllStream == null) {
                throw new IllegalStateException("DLL do OpenCV não encontrada no classpath.");
            }

            // Cria um arquivo temporário para a DLL
            File tempDll = File.createTempFile("opencv_java4110", ".dll");
            tempDll.deleteOnExit();

            // Copia o conteúdo da DLL para o arquivo temporário
            try (OutputStream out = new FileOutputStream(tempDll)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = dllStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Carrega a DLL
            System.load(tempDll.getAbsolutePath());
            System.out.println("OpenCV DLL carregada com sucesso: " + tempDll.getAbsolutePath());
        } catch (IOException | UnsatisfiedLinkError e) {
            System.err.println("Erro ao carregar a biblioteca nativa do OpenCV: " + e.getMessage());
            throw new RuntimeException("Falha ao carregar a DLL do OpenCV", e);
        }
    }


    public ImagemEntity salvar(ImagemDto imagemDto) throws IOException {

        System.out.println("Salvando imagem: " + imagemDto);

        ImagemEntity imagemEntity = new ImagemEntity(imagemDto);
        boolean temRosto = false;

        if (imagemDto.tipo() == TipoValidacao.FACIAL) {
            temRosto = detectarRosto(imagemDto.imagem());
            imagemEntity.setFraudeDetectada(!temRosto);
        }

        imagemEntity.setDataHoraProcessamento(LocalDateTime.now());
        return validacaoRepository.save(imagemEntity);
    }

    private boolean detectarRosto(MultipartFile imagem) throws IOException {
        System.out.println("Biblioteca OpenCV carregada: " + Core.NATIVE_LIBRARY_NAME);
        // Salva a imagem enviada em um arquivo temporário
        File tempFile = File.createTempFile("imagem", ".jpg");
        imagem.transferTo(tempFile); // mais seguro
        System.out.println("Caminho temporário: " + tempFile.getAbsolutePath());
        System.out.println("Existe? " + tempFile.exists());
        System.out.println("Tamanho: " + tempFile.length());




        Mat imagemMat = Imgcodecs.imread(tempFile.getAbsolutePath());
        if (imagemMat.empty()) {
            tempFile.delete();
            System.out.println("Erro ao carregar a imagem no OpenCV.");
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
}
