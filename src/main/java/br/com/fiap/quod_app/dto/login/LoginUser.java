package br.com.fiap.quod_app.dto.login;

import br.com.fiap.quod_app.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginUser
        (
                @Schema
                @NotBlank
                String email,
                @Schema
                @NotBlank
                String senha,
                @Schema
                @NotBlank
                UserRole role) {
}

