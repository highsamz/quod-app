package br.com.fiap.quod_app.repository;

import br.com.fiap.quod_app.domain.ImagemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidacaoRepository extends MongoRepository<ImagemEntity, String> {
}