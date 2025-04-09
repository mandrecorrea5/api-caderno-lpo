package br.com._tech.api_caderno_lpo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @Column(nullable = false, length = 15)
  private String telefone;

  @Column(name = "senha_hash", nullable = false)
  private String senhaHash;

  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @PreUpdate
  public void preAtualizar(){
    this.atualizadoEm = LocalDateTime.now();
  }

  @PrePersist
  public void prePersistir(){
    this.criadoEm = LocalDateTime.now();
    this.atualizadoEm = LocalDateTime.now();
  }
}
