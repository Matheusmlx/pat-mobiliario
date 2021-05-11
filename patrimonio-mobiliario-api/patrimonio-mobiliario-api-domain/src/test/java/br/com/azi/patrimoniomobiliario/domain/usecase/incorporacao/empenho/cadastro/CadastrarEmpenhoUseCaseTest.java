package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.converter.CadastrarEmpenhoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception.QuantidadeExcedidaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarEmpenhoUseCaseTest {

    @Mock
    private EmpenhoDataProvider empenhoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    private CadastrarEmpenhoUseCase useCase;

    @Before
    public void setUp() {
        useCase = new CadastrarEmpenhoUseCaseImpl(
            empenhoDataProvider,
            incorporacaoDataProvider,
            new CadastrarEmpenhoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIncorporacaoForNula() {
        CadastrarEmpenhoInputData inputData = new CadastrarEmpenhoInputData();
        inputData.setNumeroEmpenho("13818313");
        inputData.setDataEmpenho(new Date());
        inputData.setValorEmpenho(new BigDecimal("12500.50"));

        useCase.executar(inputData);
    }

    @Test
    public void deveCadastrarEmpenho() {
        LocalDateTime dataEmpenho = LocalDateTime.of(2020, Month.SEPTEMBER, 20, 10, 10, 10);

        CadastrarEmpenhoInputData inputData = CadastrarEmpenhoInputData
            .builder()
            .numeroEmpenho("13818313")
            .dataEmpenho(Date.from(dataEmpenho.atZone(ZoneId.systemDefault()).toInstant()))
            .valorEmpenho(new BigDecimal("5000"))
            .incorporacaoId(2L)
            .build();

        when(empenhoDataProvider.retornaQuantidadePorIncorporacaoId(any(Long.class)))
            .thenReturn(9L);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build()));

        when(empenhoDataProvider.salvar(any(Empenho.class)))
            .thenReturn(
                Empenho
                    .builder()
                    .id(1L)
                    .numeroEmpenho("13818313")
                    .valorEmpenho(new BigDecimal("1500.5"))
                    .dataEmpenho(dataEmpenho)
                    .incorporacaoId(2L)
                    .build()
            );

        CadastrarEmpenhoOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("13818313", outputData.getNumeroEmpenho());
        assertEquals(dataEmpenho, outputData.getDataEmpenho());
        assertEquals(new BigDecimal("1500.5"), outputData.getValorEmpenho());
        assertEquals(Long.valueOf(2), outputData.getIncorporacaoId());
    }

    @Test(expected = QuantidadeExcedidaException.class)
    public void deveFalharQuandoQuantidadeEmpenhoMaiorOuIgualADez() {
        CadastrarEmpenhoInputData inputData = CadastrarEmpenhoInputData
            .builder()
            .numeroEmpenho("13818313")
            .dataEmpenho(new Date())
            .valorEmpenho(new BigDecimal("5000"))
            .incorporacaoId(2L)
            .build();

        when(empenhoDataProvider.retornaQuantidadePorIncorporacaoId(any(Long.class)))
            .thenReturn(10L);

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmElaboracaoOuComErroProcessamento() {

        CadastrarEmpenhoInputData inputData = CadastrarEmpenhoInputData.builder().incorporacaoId(1L).build();

        when(empenhoDataProvider.retornaQuantidadePorIncorporacaoId(any(Long.class)))
            .thenReturn(5L);

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
