package br.com._tech.api_caderno_lpo.service;

import br.com._tech.api_caderno_lpo.business.JwtSecuruty;
import br.com._tech.api_caderno_lpo.domain.ResetToken;
import br.com._tech.api_caderno_lpo.domain.Usuario;
import br.com._tech.api_caderno_lpo.dto.LoginRequestDTO;
import br.com._tech.api_caderno_lpo.dto.LoginResponseDTO;
import br.com._tech.api_caderno_lpo.dto.ResetarSenhaRequestDTO;
import br.com._tech.api_caderno_lpo.dto.UsuarioResponseDTO;
import br.com._tech.api_caderno_lpo.enums.ResetTokenStatus;
import br.com._tech.api_caderno_lpo.repository.ResetTokenRepository;
import br.com._tech.api_caderno_lpo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UsuarioRepository usuarioRepository;
  private final ResetTokenRepository resetTokenRepository;
  private final JwtSecuruty jwtSecuruty;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public LoginResponseDTO login(LoginRequestDTO dto){
    Usuario usuario = usuarioRepository.findByEmail(dto.email())
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

    boolean senhaValida = passwordEncoder.matches(dto.senha(), usuario.getSenhaHash());

    if(!senhaValida){
      throw new RuntimeException("Email ou senha inválidos.");
    }

    String token = jwtSecuruty.gerarToken(usuario.getId(), usuario.getEmail());

    return new LoginResponseDTO(token);
  }

  public void gerarTokenRedefinicaoSenha(String email){
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

    String token = UUID.randomUUID().toString();

    ResetToken resetToken = new ResetToken();
    resetToken.setUsuario(usuario);
    resetToken.setToken(token);

    System.out.println("Token de redefinição de senha gerado: " + resetToken.getToken());
  }

  public void resetarSenha(ResetarSenhaRequestDTO dto) {
    ResetToken resetToken = resetTokenRepository.findByToken(dto.token())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    resetToken.setStatus(ResetTokenStatus.USADO);

    Usuario usuario = resetToken.getUsuario();
    String novaSenhaHash = passwordEncoder.encode(dto.novaSenha());

    usuario.setSenhaHash(novaSenhaHash);
    usuarioRepository.save(usuario);

    resetTokenRepository.save(resetToken);
  }
}
