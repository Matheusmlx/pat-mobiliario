package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.IncorporacaoComPatrimoniosEmOutrasMovimentacoesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.ReabrirDuranteMesCorrenteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception.ReabrirIncorporacaoNaoFinalizadaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReabrirIncorporacaoUseCaseTest {

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private ReabrirIncorporacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new ReabrirIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
                configuracaoDepreciacaoDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFaltaIdIncorporacao() {
        ReabrirIncorporacaoInputData inputData = new ReabrirIncorporacaoInputData();

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoIncorporacaoNaoEncontrada() {
        ReabrirIncorporacaoInputData inputData = ReabrirIncorporacaoInputData
            .builder()
            .incorporacaoId(5L)
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = ReabrirDuranteMesCorrenteException.class)
    public void deveFalharQuandoTentarReabrirForaMesCorrente() {
        ReabrirIncorporacaoInputData inputData = ReabrirIncorporacaoInputData
            .builder()
            .incorporacaoId(5L)
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao.builder()
                    .codigo("153153")
                    .reconhecimento(Reconhecimento.builder().id(3L).build())
                    .dataFinalizacao(LocalDateTime.now().withMonth(5).withYear(2020))
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoComPatrimoniosEmOutrasMovimentacoesException.class)
    public void deveFalharQuandoIncorporacaoTiverPatrimoniosEmOutrasMovimentacoes() {
        ReabrirIncorporacaoInputData inputData = ReabrirIncorporacaoInputData
            .builder()
            .incorporacaoId(5L)
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao.builder()
                    .id(1L)
                    .situacao(Incorporacao.Situacao.FINALIZADO)
                    .dataFinalizacao(LocalDateTime.now())
                    .build()
            ));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(any(Long.class)))
            .thenReturn(3L);

        useCase.executar(inputData);
    }

    @Test
    public void deveReabrirIncorporacao() {
        ReabrirIncorporacaoInputData inputData = ReabrirIncorporacaoInputData
            .builder()
            .incorporacaoId(5L)
            .build();

        Incorporacao incorporacao = Incorporacao.builder()
            .id(5L)
            .codigo("153153")
            .situacao(Incorporacao.Situacao.FINALIZADO)
            .reconhecimento(Reconhecimento.builder().id(3L).build())
            .dataFinalizacao(LocalDateTime.now())
            .build();

        Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(5L)
            .codigo("153153")
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .reconhecimento(Reconhecimento.builder().id(3L).build())
            .dataFinalizacao(incorporacao.getDataFinalizacao())
            .build();

        Patrimonio patrimonio1 = Patrimonio
            .builder()
            .id(10L)
            .valorAquisicao(BigDecimal.valueOf(500L))
            .situacao(Patrimonio.Situacao.ATIVO)
            .contaContabilAtual(ContaContabil.builder().id(3L).build())
            .build();

        Patrimonio patrimonio2 = Patrimonio
            .builder()
            .id(11L)
            .valorAquisicao(BigDecimal.valueOf(250L))
            .situacao(Patrimonio.Situacao.ATIVO)
            .contaContabilAtual(ContaContabil.builder().id(3L).build())
            .build();

        Patrimonio patrimonio3 = Patrimonio
            .builder()
            .id(12L)
            .valorAquisicao(BigDecimal.valueOf(250.10))
            .situacao(Patrimonio.Situacao.ATIVO)
            .contaContabilAtual(ContaContabil.builder().id(3L).build())
            .build();

        Movimentacao movimentacao = Movimentacao
            .builder()
            .id(35L)
            .build();

        MovimentacaoItem movimentacaoItem1 = MovimentacaoItem
            .builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio1)
            .build();

        MovimentacaoItem movimentacaoItem2 = MovimentacaoItem
            .builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio2)
            .build();

        MovimentacaoItem movimentacaoItem3 = MovimentacaoItem
            .builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio3)
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                incorporacao
            ));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(
                List.of(
                    ItemIncorporacao
                        .builder()
                        .id(2L)
                        .descricao("Item incorporação 1")
                        .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                        .valorTotal(BigDecimal.valueOf(500L))
                        .configuracaoDepreciacao(ConfiguracaoDepreciacao.builder().id(2L).build())
                        .build(),
                    ItemIncorporacao
                        .builder()
                        .id(3L)
                        .descricao("Item incorporação 2")
                        .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                        .configuracaoDepreciacao(ConfiguracaoDepreciacao.builder().id(2L).build())
                        .valorTotal(BigDecimal.valueOf(500.10))
                        .build()));

        when(patrimonioDataProvider.buscarPatrimoniosPorItemIncorporacaoId(2L))
            .thenReturn(
                List.of(patrimonio1));

        when(patrimonioDataProvider.buscarPatrimoniosPorItemIncorporacaoId(3L))
            .thenReturn(
                List.of(patrimonio2, patrimonio3));

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).buscarPorId(5L);
        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoAtualizada);
        verify(itemIncorporacaoDataProvider, times(1)).buscarItensPorIncorporacaoId(5L);

        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosPorItemIncorporacaoId(2L);
        verify(lancamentoContabilDataProvider, times(1)).removerPorPatrimonio(patrimonio1);
        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio1);

        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosPorItemIncorporacaoId(3L);
        verify(lancamentoContabilDataProvider, times(1)).removerPorPatrimonio(patrimonio2);
        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio2);

        verify(lancamentoContabilDataProvider, times(1)).removerPorPatrimonio(patrimonio3);
        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio3);

        verify(configuracaoDepreciacaoDataProvider, times(2)).excluir(any(Long.class));
    }

    @Test(expected = ReabrirIncorporacaoNaoFinalizadaException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverFinalizada() {

        ReabrirIncorporacaoInputData inputData = new ReabrirIncorporacaoInputData(3L);

        when(incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId()))
            .thenReturn(Optional.of(Incorporacao.builder().id(3L).situacao(Incorporacao.Situacao.EM_PROCESSAMENTO).dataFinalizacao(LocalDateTime.now()).build()));

        useCase.executar(inputData);
    }
}
