package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoContabil {
    private Long id;
    private TipoLancamento tipoLancamento;
    private LocalDateTime dataLancamento;
    private BigDecimal valor;
    private TipoMovimentacao tipoMovimentacao;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private ContaContabil contaContabil;
    private Patrimonio patrimonio;
    private Incorporacao incorporacao;
    private Movimentacao movimentacao;

    public enum TipoLancamento {
        CREDITO,
        DEBITO
    }

    public enum TipoMovimentacao {
        INCORPORACAO,
        DISTRIBUICAO,
        ENTRE_SETORES,
        ENTRE_ESTOQUES,
        DEVOLUCAO_ALMOXARIFADO,
        TEMPORARIA_IDA,
        TEMPORARIA_VOLTA
    }
}
