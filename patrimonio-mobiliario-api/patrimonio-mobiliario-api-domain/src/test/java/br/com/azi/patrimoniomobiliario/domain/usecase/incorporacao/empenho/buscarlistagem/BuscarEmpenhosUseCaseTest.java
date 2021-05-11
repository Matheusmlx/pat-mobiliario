package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarEmpenhosUseCaseTest {

    private EmpenhoDataProvider empenhoDataProvider;

    @Before
    public void setUp() {
        empenhoDataProvider = Mockito.mock(EmpenhoDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIncorporacaoForNula() {
        BuscarEmpenhosUseCase useCase = new BuscarEmpenhosUseCaseImpl(
            empenhoDataProvider,
            new BuscarEmpenhosOutputDataConverter(),
            new BuscarEmpenhosFiltroConverter()
        );

        BuscarEmpenhosInputData inputData = BuscarEmpenhosInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarListagemEmpenhos() {
        BuscarEmpenhosUseCase useCase = new BuscarEmpenhosUseCaseImpl(
            empenhoDataProvider,
            new BuscarEmpenhosOutputDataConverter(),
            new BuscarEmpenhosFiltroConverter()
        );

        BuscarEmpenhosInputData inputData = new BuscarEmpenhosInputData();
        inputData.setIncorporacaoId(2L);

        LocalDateTime data = LocalDateTime.now().withMonth(5).withDayOfMonth(10);

        Mockito.when(empenhoDataProvider.buscarPorIncorporacao(any(Empenho.Filtro.class)))
            .thenReturn(List.of(
                    Empenho
                        .builder()
                        .id(2L)
                        .numeroEmpenho("83438438")
                        .dataEmpenho(data)
                        .valorEmpenho(new BigDecimal("68468486"))
                        .incorporacaoId(2L)
                        .build()
                ));

        BuscarEmpenhosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(0).getId());
        Assert.assertEquals("83438438", outputData.getItems().get(0).getNumeroEmpenho());
        Assert.assertEquals(new BigDecimal("68468486"), outputData.getItems().get(0).getValorEmpenho());
        Assert.assertEquals(data, outputData.getItems().get(0).getDataEmpenho());
        Assert.assertEquals(Long.valueOf(1), outputData.getTotalElements());
    }
}
