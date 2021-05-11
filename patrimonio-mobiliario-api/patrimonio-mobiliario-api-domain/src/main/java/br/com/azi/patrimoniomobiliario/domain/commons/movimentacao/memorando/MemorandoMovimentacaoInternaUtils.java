package br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class MemorandoMovimentacaoInternaUtils {

    public String formatarContaContabil(ContaContabil contaContabil) {
        return contaContabil.getCodigo()+" - "+contaContabil.getDescricao();
    }

    public String formatarTituloMemorando(TipoMovimentacaoEnum tipoMovimentacao) {
        String titulo = null;
        switch (tipoMovimentacao) {
            case DISTRIBUICAO:
            case DEVOLUCAO_ALMOXARIFADO:
                titulo = tipoMovimentacao.getValor();
                break;
            case ENTRE_SETORES:
            case TEMPORARIA:
            case ENTRE_ESTOQUES:
                titulo = "Movimentação "+tipoMovimentacao.getValor();
                break;
            default:
                break;
        }
        return titulo;
    }


    public BigDecimal somarValorEntradaTotalPorOrgao(List<MemorandoMovimentacaoInterna.ContaContabil> contaContabeis) {
        BigDecimal valorEntradaTotalNoOrgao = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (MemorandoMovimentacaoInterna.ContaContabil contaContabil: contaContabeis) {
            valorEntradaTotalNoOrgao = valorEntradaTotalNoOrgao.add(contaContabil.getValorEntrada(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorEntradaTotalNoOrgao;
    }

    public BigDecimal somarDepreciadoAcumuladoTotalPorOrgao(List<MemorandoMovimentacaoInterna.ContaContabil> contaContabeis) {
        BigDecimal valorEntradaTotalNoOrgao = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (MemorandoMovimentacaoInterna.ContaContabil contaContabil: contaContabeis) {
            valorEntradaTotalNoOrgao = valorEntradaTotalNoOrgao.add(contaContabil.getValorDepreciadoAcumulado(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorEntradaTotalNoOrgao;
    }

    public BigDecimal somarValorLiquidoTotalPorOrgao(List<MemorandoMovimentacaoInterna.ContaContabil> contaContabeis) {
        BigDecimal valorEntradaTotalNoOrgao = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (MemorandoMovimentacaoInterna.ContaContabil contaContabil: contaContabeis) {
            valorEntradaTotalNoOrgao = valorEntradaTotalNoOrgao.add(contaContabil.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorEntradaTotalNoOrgao;
    }

    public String formatarDataFinalizacaoMovimentacao(Movimentacao movimentacao) {
        return DateUtils.formatarData(movimentacao.getDataFinalizacao());
    }

    public String formatarDataEnvioMovimentacao(Movimentacao movimentacao) {
        return DateUtils.formatarData(movimentacao.getDataEnvio());
    }

    public String formatarEsperadaDeDevolucao(Movimentacao movimentacao) {
        return DateUtils.formatarData(movimentacao.getDataDevolucao());
    }
}
