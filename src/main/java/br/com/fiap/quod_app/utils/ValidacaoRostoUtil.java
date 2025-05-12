package br.com.fiap.quod_app.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ValidacaoRostoUtil {

    public static boolean detectarRosto(MultipartFile imagem) throws IOException {
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
        InputStream xmlStream = ValidacaoRostoUtil.class.getResourceAsStream("/classifiers/haarcascade_frontalface_default.xml");
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
