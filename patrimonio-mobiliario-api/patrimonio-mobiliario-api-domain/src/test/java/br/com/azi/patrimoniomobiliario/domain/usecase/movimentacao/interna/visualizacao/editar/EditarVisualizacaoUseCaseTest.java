package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.converter.EditarVisualizacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarVisualizacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Captor
    private ArgumentCaptor<Movimentacao> argumentCaptorMovimentacao;

    private EditarVisualizacaoUseCase useCase;

    private static final Long idMovimentacao = 1L;
    private static final LocalDateTime dataNotaContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
    private static final Movimentacao movimentacaoSalvaDefault = Movimentacao.builder()
        .id(idMovimentacao)
        .situacao(Movimentacao.Situacao.EM_ELABORACAO)
        .build();

    @Before
    public void setupTest() {
        useCase = new EditarVisualizacaoUseCaseImpl(
            movimentacaoDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarVisualizacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoForNulo() {
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder().build();
        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarMovimentacaoPorId() {
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(false);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).existe(idMovimentacao);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherONumeroENaoTiverNumeroEDataDaNotaPreenchidos() {
        final String numeroNotaLancamentoContabil = "2020NL123456";
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .numeroNotaLancamentoContabil(numeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .numero(numeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .numero(numeroNotaLancamentoContabil)
            .build();

        criarMocksTestePreenchimentoCampos();
        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(numeroNotaLancamentoContabil);
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherADataENaoTiverNumeroEDataDaNotaPreenchidos() {
        final Date dataNotaLancamentoContabil = DateUtils.asDate(dataNotaContabil);
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .dataNotaLancamentoContabil(dataNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .dataLancamento(dataNotaContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .dataLancamento(dataNotaContabil)
            .build();

        criarMocksTestePreenchimentoCampos();
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveAtualizarANotaLancamentoContabilExistenteQuandoAtualizarNumeroOuData() {
        final Long idNotaLancamentoContabil = 1L;
        final String numeroNotaLancamentoContabil = "2020NL123456";
        final String novoNumeroNotaLancamentoContabil = "2020NL123445";
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .numeroNotaLancamentoContabil(novoNumeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .id(idNotaLancamentoContabil)
            .numero(novoNumeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(idNotaLancamentoContabil)
            .numero(novoNumeroNotaLancamentoContabil)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(idMovimentacao)
                    .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(idNotaLancamentoContabil)
                        .numero("2020NL123456")
                        .build())
                    .build()));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);
        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(novoNumeroNotaLancamentoContabil);
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveRemoverANotaLancamentoContabilQuandoOsCamposNumeroEDataForemNulos() {
        final Long idNotaLancamentoContabil = 1L;
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(idMovimentacao)
                    .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(idNotaLancamentoContabil)
                        .numero("2020NL123456")
                        .build())
                    .build()));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(notaLancamentoContabilDataProvider, times(1)).remover(idNotaLancamentoContabil);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNull(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil());
    }

    @Test
    public void naoDeveCriarNotaLancamentoContabilQuandoOsCamposForemNulos() {
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData.builder()
            .id(idMovimentacao)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verifyZeroInteractions(notaLancamentoContabilDataProvider);
    }

    private void criarMocksTestePreenchimentoCampos() {
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);
    }
}
