package br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Log4j2
@Component
public class DepreciacaoJobExecutionListener implements JobExecutionListener {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void beforeJob(JobExecution jobExecution) {
        final String dataInicio = formatarDataComHora(jobExecution.getStartTime());
        log.info("[DEPRECIACAO] Depreciação de patrimônios iniciada em: " + dataInicio);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        final String dataFinal = formatarDataComHora(jobExecution.getStartTime());
        final String tempoExecucao = calcularTempoExecucao(jobExecution);

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("[DEPRECIACAO] Depreciação de patrimônios finalizada com sucesso em: " + dataFinal);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("[DEPRECIACAO] Depreciação de patrimônios finalizada com erro em: " + dataFinal);
        }

        log.info("[DEPRECIACAO] Tempo total de execução: " + tempoExecucao);
    }

    private String formatarDataComHora(Date data) {
        return simpleDateFormat.format(data);
    }

    private String calcularTempoExecucao(JobExecution jobExecution) {
        final Duration tempoExecucao = Duration.between(jobExecution.getStartTime().toInstant(),
            jobExecution.getEndTime().toInstant());

        final long segundos = Math.abs(tempoExecucao.getSeconds());
        return String.format("%d:%02d:%02d", segundos / 3600, (segundos % 3600) / 60, segundos % 60);
    }
}
