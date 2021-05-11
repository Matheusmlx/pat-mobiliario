package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.FinalizarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.FinalizarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.FinalizarIncorporacaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/patrimonios/incorporacao/finalizar/{id}")
@RequiredArgsConstructor
public class FinalizarIncorporacaoController {

    private final PlatformTransactionManager transactionManager;

    private final FinalizarIncorporacaoUseCase useCase;

    private final FinalizarIncorporacaoAmqpSender finalizarIncorporacaoAmqpSender;

    @PatchMapping
    public FinalizarIncorporacaoOutputData finalizar(FinalizarIncorporacaoInputData inputData) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        final FinalizarIncorporacaoOutputData outputData = transactionTemplate.execute(status -> useCase.executar(inputData));

        if (Objects.nonNull(outputData) && "EM_PROCESSAMENTO".equals(outputData.getSituacao())) {
            finalizarIncorporacaoAmqpSender.sendMessage(outputData.getId());
        }

        return outputData;
    }

}
