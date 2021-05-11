package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.converter.EditarEmpenhoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.EmpenhoNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarEmpenhoUseCaseTest {

    @Mock
    private EmpenhoDataProvider empenhoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    private EditarEmpenhoUseCase useCase;

    @Before
    public void setUp() {
        useCase = new EditarEmpenhoUseCaseImpl(
            empenhoDataProvider,
            incorporacaoDataProvider,
            new EditarEmpenhoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        EditarEmpenhoInputData inputData = EditarEmpenhoInputData
            .builder()
            .numeroEmpenho("3435453")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdDaIncorporacaoForNulo() {
        EditarEmpenhoInputData inputData = EditarEmpenhoInputData
            .builder()
            .id(1L)
            .numeroEmpenho("3435453")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = EmpenhoNaoExisteException.class)
    public void deveFalharQuandoEmpenhoNaoExistir() {
        EditarEmpenhoInputData inputData = EditarEmpenhoInputData
            .builder()
            .id(2L)
            .numeroEmpenho("3435453")
            .dataEmpenho(new Date())
            .valorEmpenho(new BigDecimal("5131.50"))
            .incorporacaoId(3L)
            .build();

        when(empenhoDataProvider.existePorId(any(Long.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveAtualizarEmpenho() {
        Date data = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());

        EditarEmpenhoInputData inputData = EditarEmpenhoInputData
            .builder()
            .id(2L)
            .numeroEmpenho("3435453")
            .dataEmpenho(data)
            .valorEmpenho(new BigDecimal("5131.50"))
            .incorporacaoId(3L)
            .build();

        when(empenhoDataProvider.existePorId(any(Long.class)))
            .thenReturn(true);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().id(1L).situacao(Incorporacao.Situacao.EM_ELABORACAO).build()));

        when(empenhoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Empenho
                .builder()
                .id(2L)
                .numeroEmpenho("123456")
                .dataEmpenho(LocalDateTime.now())
                .valorEmpenho(new BigDecimal("7984.75"))
                .incorporacaoId(3L)
                .build()));

        when(empenhoDataProvider.salvar(any(Empenho.class)))
            .thenReturn(Empenho
                .builder()
                .id(2L)
                .numeroEmpenho("3435453")
                .dataEmpenho(localDateTime)
                .valorEmpenho(new BigDecimal("7984.75"))
                .incorporacaoId(3L)
                .build());

        EditarEmpenhoOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("3435453", outputData.getNumeroEmpenho());
        assertEquals(new BigDecimal("7984.75"), outputData.getValorEmpenho());
        assertEquals(Long.valueOf(3L), outputData.getIncorporacaoId());
        assertEquals(localDateTime, outputData.getDataEmpenho());
    }

    @Test
    public void deveAtualizarEmpenhoSemData() {
        EditarEmpenhoInputData inputData = new EditarEmpenhoInputData();
        inputData.setId(2L);
        inputData.setNumeroEmpenho("3435453");
        inputData.setValorEmpenho(new BigDecimal("5131.50"));
        inputData.setIncorporacaoId(3L);

        when(empenhoDataProvider.existePorId(any(Long.class)))
            .thenReturn(true);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().id(1L).situacao(Incorporacao.Situacao.EM_ELABORACAO).build()));

        when(empenhoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Empenho
                .builder()
                .id(2L)
                .numeroEmpenho("123456")
                .dataEmpenho(LocalDateTime.now())
                .valorEmpenho(new BigDecimal("7984.75"))
                .incorporacaoId(3L)
                .build()));

        when(empenhoDataProvider.salvar(any(Empenho.class)))
            .thenReturn(Empenho
                .builder()
                .id(2L)
                .numeroEmpenho("3435453")
                .valorEmpenho(new BigDecimal("7984.75"))
                .incorporacaoId(3L)
                .build());

        EditarEmpenhoOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("3435453", outputData.getNumeroEmpenho());
        assertEquals(new BigDecimal("7984.75"), outputData.getValorEmpenho());
        assertEquals(Long.valueOf(3L), outputData.getIncorporacaoId());
        assertEquals(null, outputData.getDataEmpenho());
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmElaboracaoOuComErroProcessamento() {
        Date data = new Date();

        EditarEmpenhoInputData inputData = new EditarEmpenhoInputData();
        inputData.setId(2L);
        inputData.setNumeroEmpenho("3435453");
        inputData.setDataEmpenho(data);
        inputData.setValorEmpenho(new BigDecimal("5131.50"));
        inputData.setIncorporacaoId(3L);

        when(empenhoDataProvider.existePorId(any(Long.class)))
            .thenReturn(true);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .numProcesso("123456")
                .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
                .build()));

        useCase.executar(inputData);
    }
}
