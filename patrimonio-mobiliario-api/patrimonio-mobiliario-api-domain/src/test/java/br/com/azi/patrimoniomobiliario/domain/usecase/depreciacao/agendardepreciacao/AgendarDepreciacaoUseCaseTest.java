package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.converter.AgendarDepreciacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AgendarDepreciacaoUseCaseTest {


    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 5, 1);
    private Clock fixedClock;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    @Test
    public void deveAgendarDepreciacao() {

        String dataMensalDepreciacao = "01";
        List<String> feriadosNacionais = new ArrayList<>();
        List<String> feriadosLocais = new ArrayList<>();

        feriadosNacionais.add("01/01");
        feriadosNacionais.add("21/04");
        feriadosNacionais.add("01/05");
        feriadosNacionais.add("07/09");

        feriadosLocais.add("04/05");
        feriadosLocais.add("26/08");

        AgendarDepreciacaoUseCaseImpl usecase = new AgendarDepreciacaoUseCaseImpl(
            Mockito.mock(SistemaDeConfiguracoesIntegration.class),
            new AgendarDepreciacaoOutputDataConverter(),
            dataMensalDepreciacao,
            feriadosNacionais,
            feriadosLocais,
            fixedClock
        );

        AgendarDepreciacaoOutputData outputData = usecase.executar();
        Assert.assertEquals(LocalDate.of(2020, 5, 1), outputData.getData());
        Assert.assertEquals("0 5 0 1 5 ?", outputData.getCron());
    }

    @Test
    public void deveAgendarDepreciacaoComFeriadoEmBranco() {

        String dataMensalDepreciacao = "01";
        List<String> feriadosNacionais = new ArrayList<>();
        List<String> feriadosLocais = new ArrayList<>();

        feriadosNacionais.add("01/01");
        feriadosNacionais.add("21/04");
        feriadosNacionais.add("01/05");
        feriadosNacionais.add("");

        feriadosLocais.add("");
        feriadosLocais.add("26/08");

        AgendarDepreciacaoUseCaseImpl usecase = new AgendarDepreciacaoUseCaseImpl(
            Mockito.mock(SistemaDeConfiguracoesIntegration.class),
            new AgendarDepreciacaoOutputDataConverter(),
            dataMensalDepreciacao,
            feriadosNacionais,
            feriadosLocais,
            fixedClock
        );

        AgendarDepreciacaoOutputData outputData = usecase.executar();
        Assert.assertEquals(LocalDate.of(2020, 5, 1), outputData.getData());
        Assert.assertEquals("0 5 0 1 5 ?", outputData.getCron());

    }
}
