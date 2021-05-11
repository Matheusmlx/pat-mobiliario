package br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao;

import br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps.DepreciacaoItemProcessor;
import br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps.DepreciacaoItemReader;
import br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps.DepreciacaoItemWriter;
import br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps.DepreciacaoJobExecutionListener;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.DepreciacaoPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoOutputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class DepreciacaoJobFactory {

    private static final String JOB_NAME = "DepreciacaoPatrimonialJob";
    private static final String STEP_DEPRECIAR_PATRIMONIOS = "DepreciacaoPatrimonialJob.depreciarPatrimonios";
    private static final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final JobLauncher jobLauncher;

    private final DepreciacaoJobExecutionListener depreciacaoJobExecutionListener;

    private final DepreciacaoItemReader depreciacaoItemReader;

    private final DepreciacaoItemProcessor depreciacaoItemProcessor;

    private final DepreciacaoItemWriter depreciacaoItemWriter;

    @Bean(JOB_NAME)
    public Job createJob() {
        return jobBuilderFactory
            .get(JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .start(depreciarPatrimonios())
            .listener(depreciacaoJobExecutionListener)
            .build();
    }

    @Bean(STEP_DEPRECIAR_PATRIMONIOS)
    public Step depreciarPatrimonios() {
        return stepBuilderFactory
            .get(STEP_DEPRECIAR_PATRIMONIOS)
            .allowStartIfComplete(true)
            .<PatrimonioEntity, CalcularDepreciacaoOutputData>chunk(CHUNK_SIZE)
            .reader(depreciacaoItemReader)
            .processor(depreciacaoItemProcessor)
            .writer(depreciacaoItemWriter)
            .build();
    }

    @Bean("DepreciacaoPatrimonial")
    @DependsOn({JOB_NAME})
    public DepreciacaoPatrimonial createJobRunner(Job job) {
        return () -> {
            try {
                jobLauncher.run(job, new JobParameters());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("[DEPRECIACAO] Erro ao iniciar o job de depreciação de patrimônios.", e);
            }
        };
    }
}
