package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid;


import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarMovimentacaoPorIdUseCaseTest {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Before
    public void inicializa() {
        movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);
    }

    @Test
    public void deveBuscarMovimentacaoPorId() {
        BuscarMovimentacaoPorIdUseCase useCase = new BuscarMovimentacaoPorIdUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarMovimentacaoPorIdOutputDataConverter()
        );

        BuscarMovimentacaoPorIdInputData inputData = new BuscarMovimentacaoPorIdInputData();
        inputData.setId(1L);

        LocalDateTime dataNotaLancamentoContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class))).thenReturn(true);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of( Movimentacao
                .builder()
                .id(1L)
                .codigo("0000001")
                .motivoObservacao("motivo")
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .setorDestino(UnidadeOrganizacional.builder().id(3L).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(4L).build())
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .notaLancamentoContabil(NotaLancamentoContabil
                    .builder()
                    .id(1L)
                    .numero("12345")
                    .dataLancamento(dataNotaLancamentoContabil)
                    .build())
                .numeroProcesso("22222")
                .build()));

        BuscarMovimentacaoPorIdOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("motivo", outputData.getMotivoObservacao());
        Assert.assertEquals(Long.valueOf(1), outputData.getOrgao());
        Assert.assertEquals(Long.valueOf(4), outputData.getSetorOrigem());
        Assert.assertEquals(Long.valueOf(3), outputData.getSetorDestino());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao().toString());
        Assert.assertEquals("12345", outputData.getNumeroNotaLancamentoContabil());
        Assert.assertEquals(DateValidate.formatarData(dataNotaLancamentoContabil), outputData.getDataNotaLancamentoContabil());
        Assert.assertEquals("22222", outputData.getNumeroProcesso());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdEhNulo() {
        BuscarMovimentacaoPorIdUseCase useCase = new BuscarMovimentacaoPorIdUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarMovimentacaoPorIdOutputDataConverter()
        );

        BuscarMovimentacaoPorIdInputData inputData = BuscarMovimentacaoPorIdInputData.builder().build();

        useCase.executar(inputData);
    }

//    @Test(expected = MovimentacaoNaoEncontradaException.class)
//    public void deveFalharQuandoIncorporacaoNaoEncontrada() {
//        BuscarMovimentacaoPorIdUseCase useCase = new BuscarMovimentacaoPorIdUseCaseImpl(
//            movimentacaoDataProvider,
//            new BuscarMovimentacaoPorIdOutputDataConverter()
//        );
//
//        BuscarMovimentacaoPorIdInputData inputData = BuscarMovimentacaoPorIdInputData
//            .builder()
//            .id(3L)
//            .build();
//
//        Mockito.when(movimentacaoDataProvider.existe(any(Long.class))).thenReturn(false);
//
//        useCase.executar(inputData);
//    }
}
