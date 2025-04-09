package br.com._tech.api_caderno_lpo.service;

import br.com._tech.api_caderno_lpo.domain.Usuario;
import br.com._tech.api_caderno_lpo.dto.UsuarioRequestDTO;
import br.com._tech.api_caderno_lpo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @InjectMocks
  private UsuarioService usuarioService;

  @Captor
  private ArgumentCaptor<Usuario> usuarioCaptor;

  @BeforeEach
  void setUp() {
    // Initialize mocks and any other setup needed before each test
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void deveCriarUsuarioComSenhaCriptografada(){
    UsuarioRequestDTO request = new UsuarioRequestDTO(
        "Nome Teste",
        "teste@teste",
        "99999999999",
        "senha123"
    );

    when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
    when(usuarioRepository.save(ArgumentMatchers.any(Usuario.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    var response  = usuarioService.criarUsuario(request);

    assertEquals("Nome Teste", response.nome());
    assertEquals("teste@teste", response.email());

    verify(usuarioRepository).save(usuarioCaptor.capture());
    assertTrue(new BCryptPasswordEncoder().matches("senha123", usuarioCaptor.getValue().getSenhaHash()));
  }

  @Test
  void deveLancarExcecaoQuandoEmailJaCadastrado() {
    UsuarioRequestDTO request = new UsuarioRequestDTO(
        "Nome Teste",
        "teste@teste",
        "99999999999",
        "senha123"
    );

    when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

    try {
      usuarioService.criarUsuario(request);
    } catch (IllegalArgumentException e) {
      assertEquals("Email já cadastrado.", e.getMessage());
    }

    verify(usuarioRepository, Mockito.never()).save(ArgumentMatchers.any(Usuario.class));
  }

  @Test
  void deveEncontrarUsuarioPorId() {
    // Arrange
    UUID id = UUID.randomUUID();
    Usuario usuario = Usuario.builder()
        .id(id)
        .nome("Nome Teste")
        .email("teste@teste")
        .telefone("99999999999")
        .senhaHash("senha123")
        .build();
    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

    // Act
    var response = usuarioService.encontrarPorId(id);

    // Assert
    assertEquals("Nome Teste", response.nome());
    assertEquals("teste@teste", response.email());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    try {
      usuarioService.encontrarPorId(id);
    } catch (IllegalArgumentException e) {
      assertEquals("Usuário não encontrado.", e.getMessage());
    }
  }

  @Test
  void deveAtualizarUsuario() {
    // Arrange
    UUID id = UUID.randomUUID();
    Usuario usuarioExistente = Usuario.builder()
        .id(id)
        .nome("Nome Antigo")
        .email("antigo@teste")
        .telefone("99999999999")
        .senhaHash("senha123")
        .build();

    UsuarioRequestDTO request = new UsuarioRequestDTO(
        "Nome Atualizado",
        "atualizado@teste",
        "88888888888",
        "novaSenha123"
    );

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
    when(usuarioRepository.save(ArgumentMatchers.any(Usuario.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    var response = usuarioService.atualizarUsuario(id, request);

    // Assert
    assertEquals("Nome Atualizado", response.nome());
    assertEquals("atualizado@teste", response.email());

    verify(usuarioRepository).save(usuarioCaptor.capture());
  }

  @Test
  void deveLancarExcecaoQuandoEmailJaCadastradoNaAtualizacao() {
    // Arrange
    UUID id = UUID.randomUUID();
    Usuario usuarioExistente = Usuario.builder()
        .id(id)
        .nome("Nome Antigo")
        .email("antigo@teste")
        .telefone("99999999999")
        .senhaHash("senha123")
        .build();

    UsuarioRequestDTO request = new UsuarioRequestDTO(
        "Nome Atualizado",
        "antigo@teste",
        "88888888888",
        "novaSenha123"
    );

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);
    when(usuarioRepository.save(ArgumentMatchers.any(Usuario.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act & Assert
    try {
      usuarioService.atualizarUsuario(id, request);
    } catch (IllegalArgumentException e) {
      assertEquals("Email já cadastrado.", e.getMessage());
    }
  }

  @Test
  void deveDeletarUsuario() {
    // Arrange
    UUID id = UUID.randomUUID();
    Usuario usuario = Usuario.builder()
        .id(id)
        .nome("Nome Teste")
        .email("teste@teste")
        .telefone("99999999999")
        .senhaHash("senha123")
        .build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

    // Act
    usuarioService.deleteUsuario(id);

    // Assert
    verify(usuarioRepository).delete(usuario);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoEncontradoNaDelecao() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    try {
      usuarioService.deleteUsuario(id);
    } catch (IllegalArgumentException e) {
      assertEquals("Usuário não encontrado.", e.getMessage());
    }
  }

  @Test
  void deveListarTodosUsuarios() {
    // Arrange
    Usuario usuario1 = Usuario.builder()
        .id(UUID.randomUUID())
        .nome("Nome 1")
        .email("email1@teste")
        .telefone("99999999999")
        .senhaHash("senha123")
        .build();

    Usuario usuario2 = Usuario.builder()
        .id(UUID.randomUUID())
        .nome("Nome 2")
        .email("email2@teste")
        .telefone("88888888888")
        .senhaHash("senha456")
        .build();

    when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

    // Act
    var response = usuarioService.listarTodos();

    // Assert
    assertEquals(2, response.size());
    assertEquals("Nome 1", response.get(0).nome());
    assertEquals("Nome 2", response.get(1).nome());
  }

  @Test
  void deveRetornarListaVaziaQuandoNaoExistiremUsuarios() {
    // Arrange
    when(usuarioRepository.findAll()).thenReturn(List.of());

    // Act
    var response = usuarioService.listarTodos();

    // Assert
    assertEquals(0, response.size());
  }
}
