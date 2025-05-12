package br.com.fiap.quod_app.utils;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class OpenCVLoader {

    static {
        try {
            String dllRelativa = "libs/opencv_java470.dll";

            String caminhoDll = construirCaminhoAbsoluto(dllRelativa);
            System.load(caminhoDll);
            System.out.println("OpenCV carregado com sucesso: " + caminhoDll);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Erro ao carregar OpenCV: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar OpenCV", e);
        }
    }

    private static String construirCaminhoAbsoluto(String caminhoRelativo) {
        Path path = Paths.get(caminhoRelativo);
        return path.toAbsolutePath().toString();
    }
}