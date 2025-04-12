package br.com._tech.api_caderno_lpo.controller;

import br.com._tech.api_caderno_lpo.dto.UsuarioResponseDTO;
import br.com._tech.api_caderno_lpo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Logger;


@Slf4j
@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {
  private final UsuarioService usuarioService;

  @GetMapping("/me")
  public ResponseEntity<UsuarioResponseDTO> getUsuarioLogado(Authentication authentication) {
    Logger.getLogger("UsuarioController").info("Usuário logado: " + authentication);
    String usuarioId = authentication.getName();
    UUID id = UUID.fromString(usuarioId);
    var usuario = usuarioService.encontrarPorId(id);
    return ResponseEntity.ok(usuario);
  }
}
