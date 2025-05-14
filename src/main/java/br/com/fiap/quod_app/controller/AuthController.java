package br.com.fiap.quod_app.controller;

import br.com.fiap.quod_app.domain.User;
import br.com.fiap.quod_app.dto.login.LoginUser;
import br.com.fiap.quod_app.service.security.TokenService;
import br.com.fiap.quod_app.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUser loginUser){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.email(), loginUser.senha()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = tokenService.createToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register (@RequestBody LoginUser loginUser){
        User newUser = userService.save(new User(loginUser));
        return newUser;
    }
}