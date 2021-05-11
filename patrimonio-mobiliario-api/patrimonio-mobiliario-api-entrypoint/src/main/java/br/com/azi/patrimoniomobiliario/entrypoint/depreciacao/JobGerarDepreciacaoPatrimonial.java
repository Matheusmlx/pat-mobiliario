package br.com.azi.patrimoniomobiliario.entrypoint.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.DepreciacaoPatrimonial;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobGerarDepreciacaoPatrimonial implements Runnable {

    private final DepreciacaoPatrimonial depreciacaoPatrimonial;

    @Override
    @Scheduled(cron = "${az.patrimonio-mobiliario.cron-agendamento-depreciacao}")
    @SchedulerLock(name = "JobGerarDepreciacaoPatrimonial.run", lockAtLeastFor = "1m")
    public void run() {
        log.info("[DEPRECIACAO] Inicio Job Depreciação");

        depreciacaoPatrimonial.executar();

        log.info("[DEPRECIACAO] Fim Job Depreciação");
    }
}
