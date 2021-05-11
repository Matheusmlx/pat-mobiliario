package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.converter.BuscarConcedentePorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.exception.ConcedenteNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarConcedentePorIdTest {

    private ConcedenteDataProvider concedenteDataProvider;

    @Before
    public void inicializa() {
        concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId(){
        BuscarConcedentePorIdUseCaseImpl usecase = new BuscarConcedentePorIdUseCaseImpl(
            concedenteDataProvider,
            new BuscarConcedentePorIdOutputDataConverter()
        );

        BuscarConcedentePorIdInputData inputData = BuscarConcedentePorIdInputData
            .builder()
            .build();

        usecase.executar(inputData);
    }

    @Test(expected = ConcedenteNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarId(){
        BuscarConcedentePorIdUseCaseImpl usecase = new BuscarConcedentePorIdUseCaseImpl(
            concedenteDataProvider,
            new BuscarConcedentePorIdOutputDataConverter()
        );

        BuscarConcedentePorIdInputData inputData = BuscarConcedentePorIdInputData
            .builder()
            .id(1L)
            .build();

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test
    public void deveRetornarObjeto(){
        BuscarConcedentePorIdUseCaseImpl usecase = new BuscarConcedentePorIdUseCaseImpl(
            concedenteDataProvider,
            new BuscarConcedentePorIdOutputDataConverter()
        );

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .nome("concedente teste")
                .cpfCnpj("62684618003")
                .tipoPessoa(Concedente.Pessoa.FISICA)
                .situacao(Concedente.Situacao.ATIVO)
                .inclusaoSistema(false)
                .build()));

        BuscarConcedentePorIdInputData inputData = BuscarConcedentePorIdInputData
            .builder()
            .id(1L)
            .build();

        BuscarConcedentePorIdOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("concedente teste", outputData.getNome());
        Assert.assertEquals("62684618003", outputData.getCpfCnpj());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals("FISICA", outputData.getTipoPessoa());
        Assert.assertEquals(false, outputData.getInclusaoSistema());
    }
}
