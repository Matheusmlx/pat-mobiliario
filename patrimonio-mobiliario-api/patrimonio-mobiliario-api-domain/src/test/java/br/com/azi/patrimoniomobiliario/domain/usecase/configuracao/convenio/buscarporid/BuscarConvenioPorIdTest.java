package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.converter.BuscarConvenioPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.exception.ConvenioNaoEncontradoException;
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
public class BuscarConvenioPorIdTest {

    private ConvenioDataProvider convenioDataProvider;

    @Before
    public void inicializa() {
        convenioDataProvider = Mockito.mock(ConvenioDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId(){
        BuscarConvenioPorIdUseCaseImpl usecase = new BuscarConvenioPorIdUseCaseImpl(
            convenioDataProvider,
            new BuscarConvenioPorIdOutputDataConverter()
        );

        BuscarConvenioPorIdInputData inputData = BuscarConvenioPorIdInputData
            .builder()
            .build();

        usecase.executar(inputData);
    }

    @Test(expected = ConvenioNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarId(){
        BuscarConvenioPorIdUseCaseImpl usecase = new BuscarConvenioPorIdUseCaseImpl(
            convenioDataProvider,
            new BuscarConvenioPorIdOutputDataConverter()
        );

        BuscarConvenioPorIdInputData inputData = BuscarConvenioPorIdInputData
            .builder()
            .id(1L)
            .build();

        Mockito.when(convenioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test
    public void deveRetornarObjeto(){
        BuscarConvenioPorIdUseCaseImpl usecase = new BuscarConvenioPorIdUseCaseImpl(
            convenioDataProvider,
            new BuscarConvenioPorIdOutputDataConverter()
        );

        LocalDateTime dataInicial = LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1);
        LocalDateTime dataFinal = LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1);


        Mockito.when(convenioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Convenio
                .builder()
                .id(1L)
                .nome("convenio")
                .fonteRecurso("fonte")
                .concedente(Concedente.builder().id(1L).nome("concedente").build())
                .numero("1234567")
                .situacao(Convenio.Situacao.ATIVO)
                .tipo("tipo")
                .dataVigenciaInicio(dataInicial)
                .dataVigenciaFim(dataFinal)
                .build()));

        BuscarConvenioPorIdInputData inputData = new BuscarConvenioPorIdInputData();
        inputData.setId(1L);

        BuscarConvenioPorIdOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("convenio", outputData.getNome());
        Assert.assertEquals("fonte", outputData.getFonteRecurso());
        Assert.assertEquals(Long.valueOf(1), outputData.getConcedente());
        Assert.assertEquals("1234567", outputData.getNumero());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(dataInicial, outputData.getDataVigenciaInicio());
        Assert.assertEquals(dataFinal, outputData.getDataVigenciaFim());

    }
}
