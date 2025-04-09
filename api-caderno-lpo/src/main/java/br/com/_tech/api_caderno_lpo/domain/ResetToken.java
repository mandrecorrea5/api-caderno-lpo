package br.com._tech.api_caderno_lpo.domain;

import br.com._tech.api_caderno_lpo.enums.ResetTokenStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetToken {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(nullable = false, unique = true, length = 100)
  private String token;

  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @Column(nullable = false)
  private LocalDateTime expiracao;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  private ResetTokenStatus status;

  @PrePersist
  public void prePersistir(){
    this.criadoEm = LocalDateTime.now();
    this.status = ResetTokenStatus.ATIVO;
  }
}
