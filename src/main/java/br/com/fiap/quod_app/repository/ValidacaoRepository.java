package br.com.fiap.quod_app.repository;

import br.com.fiap.quod_app.domain.ImagemEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class ValidacaoRepository implements MongoRepository<ImagemEntity, String> {

    @Override
    public <S extends ImagemEntity> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends ImagemEntity> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends ImagemEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ImagemEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ImagemEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ImagemEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ImagemEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ImagemEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ImagemEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ImagemEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ImagemEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<ImagemEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<ImagemEntity> findAll() {
        return List.of();
    }

    @Override
    public List<ImagemEntity> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(ImagemEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ImagemEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ImagemEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ImagemEntity> findAll(Pageable pageable) {
        return null;
    }
}
