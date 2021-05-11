package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notificacao {
    private Long id;
    private Origem origem;
    private Long origemId;
    private String assunto;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private Boolean visualizada;
    private Usuario usuario;

    public enum Origem {
        INCORPORACAO,
        DISTRIBUICAO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        private Long usuario;
    }
}
