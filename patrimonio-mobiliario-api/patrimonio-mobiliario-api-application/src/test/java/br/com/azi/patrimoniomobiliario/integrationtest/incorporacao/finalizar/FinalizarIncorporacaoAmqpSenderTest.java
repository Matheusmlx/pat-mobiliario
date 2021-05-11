package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar.FinalizarIncorporacaoAmqpSender;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarIncorporacaoAmqpSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private FinalizarIncorporacaoAmqpSender sender;

    @Test
    public void deveEnviarMensagemParaFilaCorretaComPayloadCorreto() {
        final long incorporacaoId = 1L;

        sender.sendMessage(incorporacaoId);

        verify(rabbitTemplate, times(1)).convertAndSend(
            "patrimonio-mobiliario-exchange",
            "incorporacao.finalizacao",
            JsonHelper.toJson(new FinalizarIncorporacaoAmqpSender.FinalizarIncorporacaoMessage(incorporacaoId))
        );
    }
}
