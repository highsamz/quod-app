package br.com.fiap.quod_app.repository;

import br.com.fiap.quod_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByEmail(String email);
}

