package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/movimentacoes/internas/distribuicao/{id}/finalizar")
@RequiredArgsConstructor
public class FinalizarDistribuicaoController {

    private final PlatformTransactionManager transactionManager;

    private final FinalizarDistribuicaoUseCase useCase;

    private final FinalizarDistribuicaoAmqpSender finalizarDistribuicaoAmqpSender;

    @PostMapping
    public FinalizarDistribuicaoOutputData finalizarDistribuicao(@PathVariable Long id) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        final FinalizarDistribuicaoOutputData outputData = transactionTemplate.execute(status -> useCase.executar(new FinalizarDistribuicaoInputData(id)));

        if (Objects.nonNull(outputData) && "EM_PROCESSAMENTO".equals(outputData.getSituacao())) {
            finalizarDistribuicaoAmqpSender.sendMessage(outputData.getId());
        }

        return outputData;
    }
}
