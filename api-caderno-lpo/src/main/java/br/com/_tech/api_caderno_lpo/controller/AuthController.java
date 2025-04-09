package br.com._tech.api_caderno_lpo.controller;

import br.com._tech.api_caderno_lpo.dto.*;
import br.com._tech.api_caderno_lpo.service.AuthenticationService;
import br.com._tech.api_caderno_lpo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UsuarioService usuarioService;
  private final AuthenticationService authenticationService;

  @PostMapping("/registrarUsuario")
  public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
    UsuarioResponseDTO usuario = usuarioService.criarUsuario(dto);
    return ResponseEntity.ok(usuario);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
    LoginResponseDTO resposta = authenticationService.login(dto);
    return ResponseEntity.ok(resposta);
  }

  @PostMapping("/esqueciSenha")
  public ResponseEntity<?> esqueciSenha(@RequestBody @Valid EsqueciSenhaRequestDTO dto) {
    authenticationService.gerarTokenRedefinicaoSenha(dto.email());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/resetarSenha")
  public ResponseEntity<?> resetarSenha(@RequestBody @Valid ResetarSenhaRequestDTO dto) {
    authenticationService.resetarSenha(dto);
    return ResponseEntity.ok(Map.of("mensagem", "Senha atualizada com sucesso."));
  }
}
