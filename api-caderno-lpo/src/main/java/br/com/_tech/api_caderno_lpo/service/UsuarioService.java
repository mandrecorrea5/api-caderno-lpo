package br.com._tech.api_caderno_lpo.service;

import br.com._tech.api_caderno_lpo.domain.Usuario;
import br.com._tech.api_caderno_lpo.dto.UsuarioRequestDTO;
import br.com._tech.api_caderno_lpo.dto.UsuarioResponseDTO;
import br.com._tech.api_caderno_lpo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
    if(usuarioRepository.existsByEmail(request.email())){
      throw new IllegalArgumentException("Email já cadastrado.");
    }

    Usuario usuario = Usuario.builder()
        .nome(request.nome())
        .email(request.email())
        .telefone(request.telefone())
        .senhaHash(passwordEncoder.encode(request.senha()))
        .build();

    Usuario usuarioSalvo = usuarioRepository.save(usuario);
    return toResponseDTO(usuarioSalvo);
  }

  public UsuarioResponseDTO encontrarPorId(UUID id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    return toResponseDTO(usuario);
  }

  public List<UsuarioResponseDTO> listarTodos() {
    List<Usuario> usuarios = usuarioRepository.findAll();
    return usuarios.stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public UsuarioResponseDTO atualizarUsuario(UUID id, UsuarioRequestDTO dto){
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

    if(!usuario.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())){
      throw new IllegalArgumentException("Email já cadastrado.");
    }

    usuario.setNome(dto.nome());
    usuario.setEmail(dto.email());
    usuario.setTelefone(dto.telefone());
    usuario.setSenhaHash(dto.senha());

    Usuario usuarioAtualizado = usuarioRepository.save(usuario);

    return toResponseDTO(usuarioAtualizado);
  }

  public void deleteUsuario(UUID id){
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

    usuarioRepository.delete(usuario);
  }

  private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
    return new UsuarioResponseDTO(
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getTelefone()
    );
  }
}
