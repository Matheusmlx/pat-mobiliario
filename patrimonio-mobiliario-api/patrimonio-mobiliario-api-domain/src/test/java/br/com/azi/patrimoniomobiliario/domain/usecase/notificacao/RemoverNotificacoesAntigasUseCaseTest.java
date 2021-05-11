package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.converter.RemoverNotificacoesAntigasOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoverNotificacoesAntigasUseCaseTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 7, 12);

    private final Clock fixedClock = Clock.fixed(
        LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        ZoneId.systemDefault()
    );

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    private RemoverNotificacoesAntigasUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new RemoverNotificacoesAntigasUseCaseImpl(
            fixedClock,
            notificacaoDataProvider,
            new RemoverNotificacoesAntigasOutputDataConverter()
        );
    }

    @Test
    public void deveRemoverNotificacoesComMaisDe30DiasDeIdade() {
        final int numeroRegistrosRemovidos = 2;
        final Date dataReferencia = DateUtils.asDate(LOCAL_DATE.minusDays(30).atStartOfDay());

        when(notificacaoDataProvider.removerNotificacoesAntigas(any(Date.class)))
            .thenReturn(numeroRegistrosRemovidos);

        final RemoverNotificacoesAntigasOutputData outputaDataEsperado = new RemoverNotificacoesAntigasOutputData(
            (new SimpleDateFormat("dd/MM/yyyy")).format(dataReferencia),
            numeroRegistrosRemovidos
        );

        final RemoverNotificacoesAntigasOutputData outputData = useCase.executar();

        assertEquals(outputaDataEsperado, outputData);
        verify(notificacaoDataProvider, times(1)).removerNotificacoesAntigas(DateUtils.asDate(dataReferencia));
    }
}
