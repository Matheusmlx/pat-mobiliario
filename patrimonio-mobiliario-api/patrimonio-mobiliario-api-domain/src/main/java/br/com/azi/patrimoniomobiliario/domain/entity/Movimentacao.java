package br.com.azi.patrimoniomobiliario.domain.entity;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
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
public class Movimentacao {
    private Long id;
    private String codigo;
    private TipoMovimentacaoEnum tipo;
    private String motivoObservacao;
    private Situacao situacao;
    private String numeroProcesso;
    private String usuarioCriacao;
    private String usuarioFinalizacao;
    private LocalDateTime dataFinalizacao;
    private LocalDateTime dataDevolucao;
    private LocalDateTime dataEnvio;
    private String numeroTermoResponsabilidade;
    private UnidadeOrganizacional orgaoOrigem;
    private UnidadeOrganizacional orgaoDestino;
    private UnidadeOrganizacional setorOrigem;
    private UnidadeOrganizacional setorDestino;
    private NotaLancamentoContabil notaLancamentoContabil;
    private Responsavel responsavel;
    private LocalDateTime dataInicioProcessamento;
    private LocalDateTime dataFimProcessamento;
    private String erroProcessamento;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Situacao {
        AGUARDANDO_DEVOLUCAO,
        DEVOLVIDO_PARCIAL,
        DEVOLVIDO,
        EM_ELABORACAO,
        EM_PROCESSAMENTO,
        ERRO_PROCESSAMENTO,
        FINALIZADO
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class Filtro extends FiltroBase {
        private Long patrimonioId;
        private String conteudo;
        private Long usuarioId;
        private List<String> funcoes;
    }
}
