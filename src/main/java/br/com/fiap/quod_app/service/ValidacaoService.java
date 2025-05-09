package br.com.fiap.quod_app.service;

import br.com.fiap.quod_app.domain.ImagemEntity;
import br.com.fiap.quod_app.dto.ImagemDto;
import br.com.fiap.quod_app.repository.ValidacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ValidacaoService {

    @Autowired
    private ValidacaoRepository validacaoRepository;

    public ImagemEntity salvar(ImagemDto imagemDto) throws IOException {
        return validacaoRepository.save(new ImagemEntity(imagemDto));
    }
}
