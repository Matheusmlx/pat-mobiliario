package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.FinalizarDistribuicaoAsyncUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.FinalizarDistribuicaoAsyncException;
import br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar.FinalizarDistribuicaoAmqpListener;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarDistribuicaoAmqpListenerTest {

    @Mock
    private FinalizarDistribuicaoAsyncUseCase finalizarDistribuicaoAsyncUseCase;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @InjectMocks
    private FinalizarDistribuicaoAmqpListener finalizarDistribuicaoAmqpListener;

    @Test
    public void deveChamarCasoDeUsoParaProcessarADistribuicao() {
        final String amqpMessage = "{\"movimentacaoId\": 1}";

        finalizarDistribuicaoAmqpListener.handleMessage(amqpMessage);

        Mockito.verify(finalizarDistribuicaoAsyncUseCase, Mockito.times(1))
            .executar(new FinalizarDistribuicaoAsyncInputData(1L));
    }

    @Test
    public void deveLancarExcecaoParaNaoProcessarADistribuicaoNovamenteQuandoOProcessamentoFalhar() {
        final String amqpMessage = "{\"movimentacaoId\": 1}";
        final FinalizarDistribuicaoAsyncException exception = new FinalizarDistribuicaoAsyncException("Test");

        Mockito.doThrow(exception).when(finalizarDistribuicaoAsyncUseCase)
            .executar(Mockito.any(FinalizarDistribuicaoAsyncInputData.class));

        exceptionRule.expect(AmqpRejectAndDontRequeueException.class);
        exceptionRule.expectCause(IsEqual.equalTo(exception));

        finalizarDistribuicaoAmqpListener.handleMessage(amqpMessage);
    }
}
