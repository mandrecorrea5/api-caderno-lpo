package br.com._tech.api_caderno_lpo.repository;

import br.com._tech.api_caderno_lpo.domain.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResetTokenRepository extends JpaRepository<ResetToken, UUID> {
  Optional<ResetToken> findByToken(String token);
}
