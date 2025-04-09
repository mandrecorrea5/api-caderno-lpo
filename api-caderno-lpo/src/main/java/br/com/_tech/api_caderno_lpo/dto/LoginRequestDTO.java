package br.com._tech.api_caderno_lpo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @Email String email,
    @NotBlank String senha
) {}
