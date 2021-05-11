package br.com.azi.patrimoniomobiliario.entrypoint.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.AgendarDepreciacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.AgendarDepreciacaoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobAgendarDepreciacao {

    private final AgendarDepreciacaoUseCase agendarDepreciacaoUseCase;

    private final JobGerarDepreciacaoPatrimonial jobGerarDepreciacaoPatrimonial;

    @Scheduled(cron = "${az.patrimonio-mobiliario.cron-dia-agendamento-depreciacao}")
    @SchedulerLock(name = "JobAgendarDepreciacao.agendarDepreciacao", lockAtLeastFor = "1m")
    public void agendarDepreciacao() {
        log.info("[AGENDAR_DEPRECIACAO] Inicio job agendador da depreciação");

        AgendarDepreciacaoOutputData outputData = agendarDepreciacaoUseCase.executar();

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.schedule(jobGerarDepreciacaoPatrimonial, new CronTrigger(outputData.getCron()));

        log.info("[AGENDAR_DEPRECIACAO] Fim job agendador da depreciação");
    }
}
