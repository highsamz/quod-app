package br.com.fiap.quod_app.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidacaoDocumentoUtil {

    private static Mat calcularHistograma(Mat imagemCinza) {
        List<Mat> lista = new ArrayList<>();
        lista.add(imagemCinza);
        Mat hist = new Mat();
        Imgproc.calcHist(lista, new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(0, 256));
        Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX);
        return hist;
    }

    public static boolean validarDocumentoPorComparacao(MultipartFile imagem, String pastaReferencias) {
        byte[] imagemBytes;
        try {
            imagemBytes = imagem.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler imagem", e);
        }

        Mat imagemEntrada = Imgcodecs.imdecode(new MatOfByte(imagemBytes), Imgcodecs.IMREAD_GRAYSCALE);
        if (imagemEntrada.empty()) {
            System.out.println("Erro ao carregar a imagem do documento.");
            return false;
        }

        Mat histEntrada = calcularHistograma(imagemEntrada);

        File pasta = new File(pastaReferencias);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Pasta de referências de documentos está vazia ou não encontrada.");
            return false;
        }

        double melhorCorrelacao = -1;
        String melhorArquivo = "";

        for (File arquivo : arquivos) {
            if (arquivo.isFile()) {
                Mat imagemRef = Imgcodecs.imread(arquivo.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                if (!imagemRef.empty()) {
                    Mat histRef = calcularHistograma(imagemRef);
                    double correlacao = Imgproc.compareHist(histEntrada, histRef, Imgproc.CV_COMP_CORREL);

                    if (correlacao > melhorCorrelacao) {
                        melhorCorrelacao = correlacao;
                        melhorArquivo = arquivo.getName();
                    }

                    if (correlacao > 0.50) {
                        System.out.println("Documento corresponde a: " + arquivo.getName() + " | Correlação: " + correlacao);
                        return true;
                    }
                }
            }
        }

        System.out.println("Documento mais próximo: " + melhorArquivo + " | Correlação: " + melhorCorrelacao);
        return false;
    }
}