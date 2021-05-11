package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IncorporarPatrimonioHelperTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);
    private final static String CODIGO_CONTA_CONTABIL_ALMOXARIFADO = "1234";

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private IncorporarPatrimonioHelper helper;

    @Test
    public void deveAtualizarAsInformacoesDosPatrimoniosComContaContabilAtualAlmoxarifado() {
        instanciarHelper(true);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getContaContabilAlmoxarifado())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveAtualizarAsInformacoesDosPatrimoniosComContaContabilAtualDeClassificacao() {
        instanciarHelper(false);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveAtribuirValorEntradaParaValorResidualQuandoContaContabilNaoDepreciavel() {
        instanciarHelper(false);

        DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        patrimonioAtualizado.setValorResidual(patrimonioAtualizado.getValorEntrada());

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveCalcularValorResidualDoPatrimonioQuandoContaContabilForDepreciavel() {
        instanciarHelper(false);

        DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();
        dadosCenarioSucesso.setItemIncorporacao(ItemIncorporacao.builder()
            .id(1L)
            .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
            .contaContabil(ContaContabil.builder().id(1L).build())
            .estadoConservacao(EstadoConservacao.builder().id(1L).build())
            .uriImagem("repo1:imagem1")
            .incorporacao(Incorporacao.builder().build())
            .configuracaoDepreciacao(
                ConfiguracaoDepreciacao
                    .builder()
                    .id(3L)
                    .depreciavel(true)
                    .percentualResidual(BigDecimal.valueOf(8.5))
                    .vidaUtil(10)
                    .build()
            )
            .build());

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(BigDecimal.valueOf(850, 2))
            .valorDepreciacaoMensal(BigDecimal.valueOf(915, 2))
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveCalcularValorDepreciacaoMensalDoPatrimonioQuandoContaContabilForDepreciavel() {
        instanciarHelper(false);

        DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();
        dadosCenarioSucesso.setItemIncorporacao(ItemIncorporacao.builder()
            .id(1L)
            .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
            .contaContabil(ContaContabil.builder().id(1L).build())
            .estadoConservacao(EstadoConservacao.builder().id(1L).build())
            .uriImagem("repo1:imagem1")
            .incorporacao(Incorporacao.builder().build())
            .configuracaoDepreciacao(
                ConfiguracaoDepreciacao
                    .builder()
                    .id(3L)
                    .depreciavel(true)
                    .percentualResidual(BigDecimal.valueOf(12))
                    .vidaUtil(10)
                    .build()
            )
            .build());

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(BigDecimal.valueOf(1200, 2))
            .valorDepreciacaoMensal(BigDecimal.valueOf(880, 2))
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonioAtualizado);
    }

    @Test
    public void deveGerarLancamentoContabilCreditoParaContaContabilAlmoxarifadoQuandoProcessarUmPatrimonio() {
        instanciarHelper(true);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getContaContabilAlmoxarifado())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        final LancamentoContabil lancamentoContabil = LancamentoContabil.builder()
            .tipoLancamento(LancamentoContabil.TipoLancamento.CREDITO)
            .dataLancamento(dadosCenarioSucesso.getIncorporacao().getDataRecebimento())
            .valor(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .tipoMovimentacao(LancamentoContabil.TipoMovimentacao.INCORPORACAO)
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabil(dadosCenarioSucesso.contaContabilAlmoxarifado)
            .patrimonio(dadosCenarioSucesso.getPatrimonio())
            .incorporacao(dadosCenarioSucesso.getIncorporacao())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(lancamentoContabilDataProvider, times(1)).salvar(lancamentoContabil);
    }

    @Test
    public void deveGerarLancamentoContabilCreditoParaContaContabilClassificacaoQuandoProcessarUmPatrimonio() {
        instanciarHelper(false);

        final DadosCenarioSucesso dadosCenarioSucesso = prepararCenarioProcessamentoPatrimonioSucesso();

        final Patrimonio patrimonioAtualizado = Patrimonio.builder()
            .id(dadosCenarioSucesso.getPatrimonio().getId())
            .valorAquisicao(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .itemIncorporacao(dadosCenarioSucesso.getPatrimonio().getItemIncorporacao())
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabilClassificacao(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .contaContabilAtual(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .situacao(Patrimonio.Situacao.ATIVO)
            .naturezaDespesa(dadosCenarioSucesso.getItemIncorporacao().getNaturezaDespesa())
            .estadoConservacao(dadosCenarioSucesso.getItemIncorporacao().getEstadoConservacao())
            .convenio(dadosCenarioSucesso.getIncorporacao().getConvenio())
            .projeto(dadosCenarioSucesso.getIncorporacao().getProjeto())
            .comodante(dadosCenarioSucesso.getIncorporacao().getComodante())
            .valorEntrada(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorLiquido(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .valorResidual(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .uriImagem(dadosCenarioSucesso.getItemIncorporacao().getUriImagem())
            .fundo(dadosCenarioSucesso.getIncorporacao().getFundo())
            .build();

        final LancamentoContabil lancamentoContabil = LancamentoContabil.builder()
            .tipoLancamento(LancamentoContabil.TipoLancamento.CREDITO)
            .dataLancamento(dadosCenarioSucesso.getIncorporacao().getDataRecebimento())
            .valor(dadosCenarioSucesso.getPatrimonio().getValorAquisicao())
            .tipoMovimentacao(LancamentoContabil.TipoMovimentacao.INCORPORACAO)
            .orgao(dadosCenarioSucesso.getIncorporacao().getOrgao())
            .setor(dadosCenarioSucesso.getIncorporacao().getSetor())
            .contaContabil(dadosCenarioSucesso.getItemIncorporacao().getContaContabil())
            .patrimonio(dadosCenarioSucesso.getPatrimonio())
            .incorporacao(dadosCenarioSucesso.getIncorporacao())
            .build();

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class))).thenReturn(patrimonioAtualizado);

        helper.incorporarPatrimonio(dadosCenarioSucesso.getPatrimonio(), dadosCenarioSucesso.getItemIncorporacao(),
            dadosCenarioSucesso.getIncorporacao(), dadosCenarioSucesso.getContaContabilAlmoxarifado());

        verify(lancamentoContabilDataProvider, times(1)).salvar(lancamentoContabil);
    }

    private DadosCenarioSucesso prepararCenarioProcessamentoPatrimonioSucesso() {
        final Long incorporacaoId = 1L;

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(incorporacaoId)
            .orgao(UnidadeOrganizacional.builder().id(1L).build())
            .setor(UnidadeOrganizacional.builder().id(2L).build())
            .convenio(Convenio.builder().id(1L).build())
            .projeto("Projeto 1")
            .comodante(Comodante.builder().id(1L).build())
            .fundo(UnidadeOrganizacional.builder().id(3L).build())
            .usuarioFinalizacao("Usuario")
            .dataFinalizacao(LOCAL_DATE.atStartOfDay())
            .dataRecebimento(LOCAL_DATE.atStartOfDay())
            .build();

        final ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder()
            .id(1L)
            .naturezaDespesa(NaturezaDespesa.builder().id(1L).build())
            .contaContabil(ContaContabil.builder().id(1L).build())
            .estadoConservacao(EstadoConservacao.builder().id(1L).build())
            .uriImagem("repo1:imagem1")
            .incorporacao(incorporacao)
            .configuracaoDepreciacao(
                ConfiguracaoDepreciacao
                    .builder()
                    .id(3L)
                    .depreciavel(false)
                    .build()
            )
            .build();

        final Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorAquisicao(new BigDecimal(100))
            .itemIncorporacao(itemIncorporacao)
            .build();

        final ContaContabil contaContabilAlmoxarifado = ContaContabil.builder()
            .id(2L)
            .codigo(CODIGO_CONTA_CONTABIL_ALMOXARIFADO)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        return DadosCenarioSucesso.builder()
            .incorporacao(incorporacao)
            .itemIncorporacao(itemIncorporacao)
            .patrimonio(patrimonio)
            .contaContabilAlmoxarifado(contaContabilAlmoxarifado)
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosCenarioSucesso {
        private Incorporacao incorporacao;
        private ItemIncorporacao itemIncorporacao;
        private Patrimonio patrimonio;
        private ContaContabil contaContabilAlmoxarifado;
    }

    private void instanciarHelper(Boolean patrimonioParaContaAlmoxarifado) {
        helper = new IncorporarPatrimonioHelper(
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            patrimonioParaContaAlmoxarifado
        );
    }
}
