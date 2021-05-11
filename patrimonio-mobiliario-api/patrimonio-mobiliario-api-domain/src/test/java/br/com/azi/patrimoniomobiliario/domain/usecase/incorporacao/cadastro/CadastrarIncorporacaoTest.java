package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.converter.CadastrarIncorporacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarIncorporacaoTest {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Before
    public void inicializa() {
        incorporacaoDataProvider = Mockito.mock(IncorporacaoDataProvider.class);
    }

    @Test
    public void deveSalvarIncorporacao() {

        CadastrarIncorporacaoUseCaseImpl useCase = new CadastrarIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            new CadastrarIncorporacaoOutputDataConverter());

        Mockito.when(incorporacaoDataProvider.salvar(any(Incorporacao.class)))
            .thenReturn(Incorporacao
                .builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build());

        CadastrarIncorporacaoOutputData outputData = useCase.executar();
        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }
}
