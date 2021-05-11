package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@AllArgsConstructor
public class IncorporarPatrimonioHelper {

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final Boolean patrimonioParaContaAlmoxarifado;

    public void incorporarPatrimonio(Patrimonio patrimonio, ItemIncorporacao itemIncorporacao, Incorporacao incorporacao, ContaContabil contaAlmoxarifado) {
        atualizarInformacoesPatrimonio(patrimonio, itemIncorporacao, incorporacao);
        atualizarContaContabilAtual(patrimonio, itemIncorporacao.getContaContabil(), contaAlmoxarifado);
        final Patrimonio patrimonioAtualizado = salvarPatrimonioAtualizado(patrimonio);

        gerarLancamentoContabil(patrimonioAtualizado, incorporacao);
    }

    private void atualizarInformacoesPatrimonio(Patrimonio patrimonio, final ItemIncorporacao itemIncorporacao, final Incorporacao incorporacao) {
        patrimonio.setOrgao(incorporacao.getOrgao());
        patrimonio.setSetor(incorporacao.getSetor());
        patrimonio.setContaContabilClassificacao(itemIncorporacao.getContaContabil());
        patrimonio.setSituacao(Patrimonio.Situacao.ATIVO);
        patrimonio.setNaturezaDespesa(itemIncorporacao.getNaturezaDespesa());
        patrimonio.setEstadoConservacao(itemIncorporacao.getEstadoConservacao());
        patrimonio.setConvenio(incorporacao.getConvenio());
        patrimonio.setFundo(incorporacao.getFundo());
        patrimonio.setProjeto(incorporacao.getProjeto());
        patrimonio.setComodante((incorporacao.getComodante()));
        patrimonio.setValorEntrada(patrimonio.getValorAquisicao());
        patrimonio.setValorLiquido(patrimonio.getValorAquisicao());
        patrimonio.setUriImagem(itemIncorporacao.getUriImagem());
        patrimonio.setValorResidual(calcularValorResidual(patrimonio, itemIncorporacao));
        if (Boolean.TRUE.equals(itemIncorporacao.getConfiguracaoDepreciacao().getDepreciavel())) {
            patrimonio.setValorDepreciacaoMensal(calcularValorDepreciacaoMensal(patrimonio, itemIncorporacao));
        }
    }

    private void atualizarContaContabilAtual(Patrimonio patrimonio, ContaContabil contaClassificacao, ContaContabil contaAlmoxarifado) {
        if (Boolean.TRUE.equals(patrimonioParaContaAlmoxarifado)) {
            patrimonio.setContaContabilAtual(contaAlmoxarifado);
        } else {
            patrimonio.setContaContabilAtual(contaClassificacao);
        }
    }

    private Patrimonio salvarPatrimonioAtualizado(Patrimonio patrimonio) {
        return patrimonioDataProvider.atualizar(patrimonio);
    }

    private void gerarLancamentoContabil(Patrimonio patrimonio, final Incorporacao incorporacao) {
        lancamentoContabilDataProvider.salvar(
            LancamentoContabil.builder()
                .tipoLancamento(LancamentoContabil.TipoLancamento.CREDITO)
                .dataLancamento(incorporacao.getDataRecebimento())
                .valor(patrimonio.getValorAquisicao())
                .tipoMovimentacao(LancamentoContabil.TipoMovimentacao.INCORPORACAO)
                .orgao(patrimonio.getOrgao())
                .setor(patrimonio.getSetor())
                .contaContabil(patrimonio.getContaContabilAtual())
                .patrimonio(patrimonio)
                .incorporacao(incorporacao)
                .build()
        );
    }

    private BigDecimal calcularValorResidual(Patrimonio patrimonio, ItemIncorporacao itemIncorporacao) {
        if (Boolean.TRUE.equals(itemIncorporacao.getConfiguracaoDepreciacao().getDepreciavel())) {
            BigDecimal percentualResidual = itemIncorporacao.getConfiguracaoDepreciacao().getPercentualResidual();
            return patrimonio.getValorEntrada()
                .multiply(percentualResidual, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN)
                .divide(BigDecimal.valueOf(100), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return patrimonio.getValorEntrada();
    }

    private BigDecimal calcularValorDepreciacaoMensal(Patrimonio patrimonio, ItemIncorporacao itemIncorporacao) {
        Integer mesesDeVidaUtil = itemIncorporacao.getConfiguracaoDepreciacao().getVidaUtil();
        BigDecimal valorDepreciavel = patrimonio.getValorAquisicao().subtract(patrimonio.getValorResidual(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        return valorDepreciavel.divide(new BigDecimal(mesesDeVidaUtil), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

}
