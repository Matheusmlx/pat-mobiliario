package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.converter.CadastrarConvenioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.ConvenioJaCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.DataInicioMaiorDataFimException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarConvenioUseCaseTest {

    private ConvenioDataProvider convenioDataProvider;

    @Before
    public void inicializa() {
        convenioDataProvider = Mockito.mock(ConvenioDataProvider.class);
    }

    @Test
    public void deveSalvarConvenio() {

        CadastrarConvenioUseCaseImpl useCase = new CadastrarConvenioUseCaseImpl(
            convenioDataProvider,
            new CadastrarConvenioOutputDataConverter());

        CadastrarConvenioInputData inputData = new CadastrarConvenioInputData();
        inputData.setNumero("12334433443");
        inputData.setNome("Convenio");
        inputData.setSituacao("ATIVO");
        inputData.setDataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setDataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setFonteRecurso("TESTE");
        inputData.setConcedente(1L);

        LocalDateTime dataInicial = LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1);
        LocalDateTime dataFinal = LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1);

        Mockito.when(convenioDataProvider.existePorNumero(any(String.class)))
            .thenReturn(Boolean.FALSE);

        Mockito.when(convenioDataProvider.salvar(any(Convenio.class)))
            .thenReturn(Convenio
            .builder()
                .id(1L)
                .numero("12334433443")
                .nome("Convenio")
                .situacao(Convenio.Situacao.ATIVO)
                .dataVigenciaInicio(dataInicial)
                .dataVigenciaFim(dataFinal)
                .fonteRecurso("TESTE")
                .concedente(Concedente.builder()
                    .id(1L)
                    .nome("concedente")
                    .build())
            .build());

        CadastrarConvenioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals("12334433443", outputData.getNumero());
        Assert.assertEquals("Convenio", outputData.getNome());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(dataInicial, outputData.getDataVigenciaInicio());
        Assert.assertEquals(dataFinal, outputData.getDataVigenciaFim());
        Assert.assertEquals("TESTE", outputData.getFonteRecurso());
        Assert.assertEquals(Long.valueOf(1L), outputData.getConcedente());
    }

    @Test(expected = ConvenioJaCadastradoException.class)
    public void deveFalharQuandoNumeroJaExistir() {

        CadastrarConvenioUseCaseImpl useCase = new CadastrarConvenioUseCaseImpl(
            convenioDataProvider,
            new CadastrarConvenioOutputDataConverter());

        CadastrarConvenioInputData inputData = CadastrarConvenioInputData
            .builder()
            .numero("12334433443")
            .nome("Convenio")
            .situacao("ATIVO")
            .dataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .dataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .fonteRecurso("TESTE")
            .concedente(1L)
            .build();

        Mockito.when(convenioDataProvider.existePorNumero(any(String.class)))
            .thenReturn(Boolean.TRUE);

        useCase.executar(inputData);
    }

    @Test(expected = DataInicioMaiorDataFimException.class)
    public void deveFalharQuandoDataInicioMaiorDataFim() {

        CadastrarConvenioUseCaseImpl useCase = new CadastrarConvenioUseCaseImpl(
            convenioDataProvider,
            new CadastrarConvenioOutputDataConverter());

        CadastrarConvenioInputData inputData = CadastrarConvenioInputData
            .builder()
            .numero("12334433443")
            .nome("Convenio")
            .situacao("ATIVO")
            .dataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .dataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .fonteRecurso("TESTE")
            .concedente(1L)
            .build();

        Mockito.when(convenioDataProvider.existePorNumero(any(String.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoEntradaForVazia() {
        CadastrarConvenioUseCaseImpl useCase = new CadastrarConvenioUseCaseImpl(
            convenioDataProvider,
            new CadastrarConvenioOutputDataConverter());

        useCase.executar(new CadastrarConvenioInputData());
    }
}
