package br.com.fiap.quod_app;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TesteValidacaoDigital {

    static {
        System.load(new java.io.File("src/main/resources/libs/opencv_java4110.dll").getAbsolutePath());
    }

    // Calcula histograma de uma imagem em tons de cinza
    private static Mat calcularHistograma(Mat imagemCinza) {
        List<Mat> lista = new ArrayList<>();
        lista.add(imagemCinza);
        Mat hist = new Mat();
        Imgproc.calcHist(lista, new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(0, 256));
        Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX);
        return hist;
    }

    // Valida a digital comparando com a pasta de digitais de referência
    public static boolean validarDigitalPorComparacao(String caminhoImagemEntrada, String pastaReferencias) {
        Mat imagemEntrada = Imgcodecs.imread(caminhoImagemEntrada, Imgcodecs.IMREAD_GRAYSCALE);
        if (imagemEntrada.empty()) {
            System.out.println("Erro ao carregar a imagem de entrada.");
            return false;
        }

        Mat histEntrada = calcularHistograma(imagemEntrada);

        File pasta = new File(pastaReferencias);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Pasta de referências vazia ou não encontrada.");
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

                    if (correlacao > 0.90) {
                        System.out.println("Imagem corresponde a: " + arquivo.getName() + " | Correlação: " + correlacao);
                        return true;
                    }
                }
            }
        }

        // Exibir melhor resultado mesmo que não tenha atingido o limiar
        System.out.println("Imagem mais próxima correspondente: " + melhorArquivo + " | Correlação: " + melhorCorrelacao);
        return false;
    }


    public static void main(String[] args) {
        String imagem = "digital-internet.jpg";
        String imagemTeste = "src/main/resources/imagensTeste/"+ imagem;
        String pastaReferencias = "src/main/resources/digitalreferences";

        boolean resultado = validarDigitalPorComparacao(imagemTeste, pastaReferencias);
        System.out.println("Imagem " + (resultado ? "VALIDA" : "INVÁLIDA") + " como digital.");
    }
}
