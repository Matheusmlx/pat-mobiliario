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
@AllArgsConstructor
@NoArgsConstructor
public class ReservaIntervalo {
    private Long id;
    private Reserva reserva;
    private String codigo;
    private Situacao situacao;
    private Preenchimento preenchimento;
    private UnidadeOrganizacional orgao;
    private Long quantidadeReservada;
    private Long numeroInicio;
    private Long numeroFim;
    private String numeroTermo;
    private LocalDateTime dataFinalizacao;
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
        FINALIZADO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Filtro extends FiltroBase {
        private List<Long> orgaos;
        private Long reservaId;
    }
}
