package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.converter.EditarConvenioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.ConvenioJaCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.ConvenioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.DataInicioMaiorDataFimException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EditarConvenioUseCaseTest {

    private ConvenioDataProvider convenioDataProvider;

    @Before
    public void inicializa() {
        convenioDataProvider = Mockito.mock(ConvenioDataProvider.class);
    }

    @Test(expected = ConvenioNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarConvenio() {
        EditarConvenioUseCaseImpl usecase = new EditarConvenioUseCaseImpl(
            convenioDataProvider,
            new EditarConvenioOutputDataConverter()
        );
        EditarConvenioInputData inputData = new EditarConvenioInputData();
        inputData.setNumero("12334433443");
        inputData.setNome("TESTE EDIÇÃO");
        inputData.setSituacao("ATIVO");
        inputData.setDataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setDataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setFonteRecurso("TESTE");
        inputData.setConcedente(1L);

        usecase.executar(inputData);
    }

    @Test
    public void deveEditarConvenio() {

        EditarConvenioInputData inputData = EditarConvenioInputData
            .builder()
            .id(1L)
            .numero("12334433443")
            .nome("TESTE")
            .situacao("ATIVO")
            .dataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .dataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .fonteRecurso("TESTE")
            .concedente(1L)
            .build();

        EditarConvenioOutputDataConverter atualizarConvenioOutputDataConverter = new EditarConvenioOutputDataConverter();

        EditarConvenioUseCaseImpl useCase = new EditarConvenioUseCaseImpl(
            convenioDataProvider,
            atualizarConvenioOutputDataConverter
        );

        Mockito.when(convenioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Convenio.builder()
                .id(1L)
                .numero("12334433443")
                .nome("TESTE")
                .fonteRecurso("TESTE")
                .concedente(Concedente.builder()
                    .id(1L)
                    .nome("concedente")
                    .build())
                .dataVigenciaInicio(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1))
                .dataVigenciaFim(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1))
                .situacao(Convenio.Situacao.ATIVO)
                .build()));

        Mockito.when(convenioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        LocalDateTime dataInicial = LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1);
        LocalDateTime dataFinal = LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1);


        Mockito.when(convenioDataProvider.atualizar(any(Convenio.class)))
            .thenReturn(Convenio.builder()
                .id(1L)
                .numero("12334433443")
                .nome("TESTE EDIÇÃO")
                .fonteRecurso("TESTE")
                .concedente(Concedente.builder()
                    .id(1L)
                    .nome("concedente")
                    .build())
                .dataVigenciaInicio(dataInicial)
                .dataVigenciaFim(dataFinal)
                .situacao(Convenio.Situacao.ATIVO)
                .build());

        EditarConvenioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals("12334433443", outputData.getNumero());
        Assert.assertEquals("TESTE EDIÇÃO", outputData.getNome());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(dataInicial, outputData.getDataVigenciaInicio());
        Assert.assertEquals(dataFinal, outputData.getDataVigenciaFim());
        Assert.assertEquals("TESTE", outputData.getFonteRecurso());
        Assert.assertEquals(Long.valueOf(1L), outputData.getConcedente());
    }

    @Test(expected = DataInicioMaiorDataFimException.class)
    public void deveFalharQuandoDataInicioMaiorDataFim() {

        EditarConvenioInputData inputData = EditarConvenioInputData
            .builder()
            .id(1L)
            .numero("12334433443")
            .nome("TESTE")
            .situacao("ATIVO")
            .dataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .dataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .fonteRecurso("TESTE")
            .concedente(1L)
            .build();

        EditarConvenioOutputDataConverter atualizarConvenioOutputDataConverter = new EditarConvenioOutputDataConverter();

        EditarConvenioUseCaseImpl useCase = new EditarConvenioUseCaseImpl(
            convenioDataProvider,
            atualizarConvenioOutputDataConverter
        );

        Mockito.when(convenioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        useCase.executar(inputData);
    }

    @Test(expected = ConvenioJaCadastradoException.class)
    public void deveFalharQuandoNumeroJaCadastrado() {

        EditarConvenioInputData inputData = EditarConvenioInputData
            .builder()
            .id(1L)
            .numero("12334433443")
            .nome("TESTE")
            .situacao("ATIVO")
            .dataVigenciaInicio(Date.from(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .dataVigenciaFim(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant()))
            .fonteRecurso("TESTE")
            .concedente(1L)
            .build();

        EditarConvenioOutputDataConverter atualizarConvenioOutputDataConverter = new EditarConvenioOutputDataConverter();

        EditarConvenioUseCaseImpl useCase = new EditarConvenioUseCaseImpl(
            convenioDataProvider,
            atualizarConvenioOutputDataConverter
        );

        Mockito.when(convenioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Convenio.builder()
                .id(1L)
                .numero("12334433463")
                .nome("TESTE")
                .fonteRecurso("TESTE")
                .concedente(Concedente.builder()
                    .id(1L)
                    .nome("concedente")
                    .build())
                .dataVigenciaInicio(LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(1))
                .dataVigenciaFim(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1))
                .situacao(Convenio.Situacao.ATIVO)
                .build()));

        Mockito.when(convenioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(convenioDataProvider.existePorNumero(any(String.class)))
            .thenReturn(Boolean.TRUE);

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoEntradaForVazia() {
        EditarConvenioOutputDataConverter atualizarConvenioOutputDataConverter = new EditarConvenioOutputDataConverter();

        EditarConvenioUseCaseImpl useCase = new EditarConvenioUseCaseImpl(
            convenioDataProvider,
            atualizarConvenioOutputDataConverter
        );

        useCase.executar(new EditarConvenioInputData());
    }
}
