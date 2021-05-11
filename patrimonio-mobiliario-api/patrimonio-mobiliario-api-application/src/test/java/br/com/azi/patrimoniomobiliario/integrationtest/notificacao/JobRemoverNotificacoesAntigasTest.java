package br.com.azi.patrimoniomobiliario.integrationtest.notificacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCase;
import br.com.azi.patrimoniomobiliario.entrypoint.notificacao.JobRemoverNotificacoesAntigas;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobRemoverNotificacoesAntigasTest {

    @Mock
    private RemoverNotificacoesAntigasUseCase removerNotificacoesAntigasUseCase;

    @InjectMocks
    private JobRemoverNotificacoesAntigas jobRemoverNotificacoesAntigas;

    @Test
    public void deveChamarUseCaseParaRemoverNotificacoesAntigas() {
        when(removerNotificacoesAntigasUseCase.executar()).thenReturn(
          new RemoverNotificacoesAntigasOutputData("25/02/2021", 2)
        );

        jobRemoverNotificacoesAntigas.removerNotificacoesAntigas();

        verify(removerNotificacoesAntigasUseCase, times(1)).executar();
    }
}
