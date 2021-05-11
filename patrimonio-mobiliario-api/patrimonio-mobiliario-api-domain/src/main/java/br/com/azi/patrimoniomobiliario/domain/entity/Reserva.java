package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private Long id;
    private String codigo;
    private Situacao situacao;
    private Preenchimento preenchimento;
    private LocalDateTime dataCriacao;
    private Long quantidadeReservada;
    private Long quantidadeRestante;
    private Long numeroInicio;
    private Long numeroFim;
    private List<ReservaIntervalo> intervalos;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Preenchimento {
        AUTOMATICO,
        MANUAL
    }

    public enum Situacao {
        EM_ELABORACAO,
        PARCIAL,
        FINALIZADO
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class Filtro extends FiltroBase {
        private Long numeroInicio;
        private Long numeroFim;
        private Long reservaId;
        private String conteudo;
        private Long usuarioId;
        private Long orgaoId;
        private List<String> funcoes;
    }
}
