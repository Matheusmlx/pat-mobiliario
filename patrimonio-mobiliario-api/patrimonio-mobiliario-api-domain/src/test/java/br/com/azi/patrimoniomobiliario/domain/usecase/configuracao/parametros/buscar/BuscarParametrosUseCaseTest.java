package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.ParametrosSistema;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar.converter.BuscarParametrosOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuscarParametrosUseCaseTest {

    @Test
    public void deveRetornarOsParametrosDoSistemaCorretamente() throws NoSuchMethodException {
        final ParametrosSistema parametrosSistema = ParametrosSistema.builder()
                .urlChatSuporte("http://urlchatsuporte.com")
                .quantidadeDigitosNumeroPatrimonio("10")
                .reservaPatrimonialGlobal(true)
                .build();

        final BuscarParametrosOutputData outputDataEsperado = BuscarParametrosOutputData.builder()
                .urlChatSuporte("http://urlchatsuporte.com")
                .quantidadeDigitosNumeroPatrimonio("10")
                .reservaPatrimonialGlobal(true)
                .build();

        final BuscarParametrosUsecase useCase = new BuscarParametrosUseCaseImpl(
                parametrosSistema,
                new BuscarParametrosOutputDataConverter()
        );

        Assert.assertEquals(useCase.executar(), outputDataEsperado);
    }
}
