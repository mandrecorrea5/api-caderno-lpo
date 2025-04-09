package br.com._tech.api_caderno_lpo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank String nome,
    @Email String email,
    @NotBlank String telefone,
    @Size(min = 6) String senha
) {}
