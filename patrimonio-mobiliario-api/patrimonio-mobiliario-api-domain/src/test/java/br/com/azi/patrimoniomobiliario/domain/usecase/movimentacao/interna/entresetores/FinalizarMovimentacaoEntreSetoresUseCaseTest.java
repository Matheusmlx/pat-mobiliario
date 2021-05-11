package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.FinalizarMovimentacaoEntreSetoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.converter.FinalizarMovimentacaoEntreSetoresOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SetorTipoAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SituacaoMovimentacaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.TipoDeMovimentacaoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarMovimentacaoEntreSetoresUseCaseTest {

    private final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 18);

    private final Clock fixedClock =  Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 18, 0, 0, 0);

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @InjectMocks
    private FinalizarMovimentacaoEntreSetoresOutputDataConverter outputDataConverter;

    private FinalizarMovimentacaoEntreSetoresUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new FinalizarMovimentacaoEntreSetoresUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            fixedClock,
            outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoMovimentacaoIdForNulo() {
        useCase.executar(new FinalizarMovimentacaoEntreSetoresInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoForEncontradaPorId() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build());
    }

    @Test(expected = SituacaoMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstiverEmElaboracao() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                .builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build());
    }

    @Test(expected = TipoDeMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoForDoTipoDefinitiva() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(1L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoInativoException.class)
    public void deveFalharQuandoOrgaoOrigemEstiverInativo() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .orgaoOrigem(UnidadeOrganizacional.builder()
                    .id(1L)
                    .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharQuandoSetorOrigemEstiverInativo() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .orgaoOrigem(UnidadeOrganizacional.builder()
                    .id(1L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setorOrigem(UnidadeOrganizacional.builder()
                    .id(2L)
                    .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharQuandoSetorDestinoEstiverInativo() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .orgaoOrigem(UnidadeOrganizacional.builder()
                    .id(1L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setorOrigem(UnidadeOrganizacional.builder()
                    .id(2L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setorDestino(UnidadeOrganizacional.builder()
                    .id(3L)
                    .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorTipoAlmoxarifadoException.class)
    public void deveFalharQuandoSetorOrigemForAlmoxarifado() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .orgaoOrigem(UnidadeOrganizacional.builder()
                    .id(1L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setorOrigem(UnidadeOrganizacional.builder()
                    .id(2L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(Boolean.TRUE)
                    .build())
                .setorDestino(UnidadeOrganizacional.builder()
                    .id(3L)
                    .almoxarifado(Boolean.FALSE)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorTipoAlmoxarifadoException.class)
    public void deveFalharQuandoSetorDestinoForAlmoxarifado() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .orgaoOrigem(UnidadeOrganizacional.builder()
                    .id(1L)
                    .almoxarifado(Boolean.FALSE)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build())
                .setorOrigem(UnidadeOrganizacional.builder()
                    .id(2L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(Boolean.FALSE)
                    .build())
                .setorDestino(UnidadeOrganizacional.builder()
                    .id(3L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(Boolean.TRUE)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void deveTrocarSituacaoParaFinalizadoEAtualizarDataEUsuarioFinalizacaoQuandoFinalizarMovimentacaoDefinitiva() {
        final String usuarioFinalizacao = "Usuario";

        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        Movimentacao movimentacaoElaboracao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .build();

        Movimentacao movimentacaoFinalizada = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .usuarioFinalizacao(usuarioFinalizacao)
            .dataFinalizacao(dataFinalizacao)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacaoElaboracao));

        Mockito.when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        FinalizarMovimentacaoEntreSetoresOutputData outputData = useCase.executar(inputData);

        Assert.assertNotNull(outputData);
        Assert.assertEquals(Long.valueOf(1L), outputData.getId());

        Mockito.verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
    }



    @Test
    public void deveAtualizarSetorDosPatrimoniosQuandoFinalizarMovimentacao() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = new FinalizarMovimentacaoEntreSetoresInputData(1L);

        Movimentacao movimentacaoEmElaboracao = Movimentacao.builder()
            .id(1L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .build();

        Movimentacao movimentacaoFinalizada = Movimentacao.builder()
            .id(1L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(dataFinalizacao)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .almoxarifado(Boolean.FALSE)
                .build())
            .build();

        ContaContabil contaContabilClassificao = ContaContabil.builder().id(1L).build();

        Patrimonio patrimonioAntesFinalizacao = Patrimonio.builder()
            .id(1L)
            .contaContabilAtual(contaContabilClassificao)
            .contaContabilClassificacao(contaContabilClassificao)
            .build();

        Patrimonio patrimonioAposFinalizacao = Patrimonio.builder()
            .id(1L)
            .setor(movimentacaoFinalizada.getSetorDestino())
            .contaContabilAtual(contaContabilClassificao)
            .contaContabilClassificacao(contaContabilClassificao)
            .build();

        MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(movimentacaoFinalizada)
            .patrimonio(patrimonioAntesFinalizacao)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoEmElaboracao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoFinalizada);
        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(Collections.singletonList(movimentacaoItem));
        when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(Collections.singletonList(patrimonioAntesFinalizacao));

        useCase.executar(inputData);

        verify(patrimonioDataProvider, times(1))
            .atualizarTodos(Collections.singletonList(patrimonioAposFinalizacao));
    }

    @Test
    public void deveGerarLancamentoContabilDebitoProSetorOrigemECreditoNoSetorDestinoQuandoFinalizarMovimentacao() {
        FinalizarMovimentacaoEntreSetoresInputData inputData = FinalizarMovimentacaoEntreSetoresInputData.builder()
            .id(1L)
            .build();

        ContaContabil contaContabil = ContaContabil.builder().id(1L).build();

        Patrimonio patrimonio =  Patrimonio.builder()
            .id(1L)
            .valorAquisicao(BigDecimal.valueOf(1500))
            .valorLiquido(BigDecimal.valueOf(1500))
            .contaContabilAtual(contaContabil)
            .build();
        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(100L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(105L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(false).build();
        UnidadeOrganizacional setorDestino =  UnidadeOrganizacional.builder().id(106L).situacao(UnidadeOrganizacional.Situacao.ATIVO) .almoxarifado(false).build();

        Movimentacao movimentacaoEmElaboracao = Movimentacao.builder()
            .id(1L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        Movimentacao movimentacaoFinalizada = Movimentacao.builder()
            .id(1L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(dataFinalizacao)
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(movimentacaoFinalizada)
            .patrimonio(patrimonio)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoEmElaboracao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoFinalizada);
        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(Collections.singletonList(movimentacaoItem));
        when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(Collections.singletonList(patrimonio));

        useCase.executar(inputData);

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoOrigem(),
            movimentacaoFinalizada.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.ENTRE_SETORES,
            LancamentoContabil.TipoLancamento.DEBITO);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoDestino(),
            movimentacaoFinalizada.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.ENTRE_SETORES,
            LancamentoContabil.TipoLancamento.CREDITO);
    }


}
