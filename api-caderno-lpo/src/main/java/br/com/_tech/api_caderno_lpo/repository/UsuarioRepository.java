package br.com._tech.api_caderno_lpo.repository;

import br.com._tech.api_caderno_lpo.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
  Optional<Usuario> findByEmail(String email);

  boolean existsByEmail(String email);
}
