package br.com._tech.api_caderno_lpo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequestDTO(
    @NotBlank @Email String email
) {}
