package br.com.azi.patrimoniomobiliario.entrypoint.notificacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobRemoverNotificacoesAntigas {

    private final RemoverNotificacoesAntigasUseCase removerNotificacoesAntigasUseCase;

    @Scheduled(cron = "${az.patrimonio-mobiliario.cron-remover-notificacoes-antigas}")
    @SchedulerLock(name = "JobRemoverNotificacoesAntigas.removerNotificacoesAntiga", lockAtLeastFor = "1m")
    public void removerNotificacoesAntigas() {
        log.info("[REMOVER_NOTIFICACOES_ANTIGAS] Início do job remover notificações antigas.");

        final RemoverNotificacoesAntigasOutputData outputData = removerNotificacoesAntigasUseCase.executar();

        log.info("[REMOVER_NOTIFICACOES_ANTIGAS] Data de referência: " + outputData.getDataReferencia());
        log.info("[REMOVER_NOTIFICACOES_ANTIGAS] Quantidade de notificações removidas: " + outputData.getQuantidadeRemovidas());
        log.info("[REMOVER_NOTIFICACOES_ANTIGAS] Fim do job remover notificações antigas.");
    }
}
