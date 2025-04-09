package br.com._tech.api_caderno_lpo.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ResetarSenhaRequestDTO(
    @NotBlank String token,
    @NotBlank String novaSenha
) {}
