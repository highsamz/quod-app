package br.com.fiap.quod_app.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImagemMetadataUtil {

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

