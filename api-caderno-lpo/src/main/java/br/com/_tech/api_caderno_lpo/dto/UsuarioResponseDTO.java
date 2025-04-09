package br.com._tech.api_caderno_lpo.dto;

import java.util.UUID;

public record UsuarioResponseDTO(
    UUID id,
    String nome,
    String email,
    String telefone
) {}
