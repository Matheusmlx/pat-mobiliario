package br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.PatrimonioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;

@StepScope
@Component
@RequiredArgsConstructor
public class DepreciacaoItemReader extends RepositoryItemReader<PatrimonioEntity> implements InitializingBean {

    private final PatrimonioRepository patrimonioRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        setRepository(patrimonioRepository);
        setMethodName("buscarPatrimoniosDepreciaveis");
        setSort(Map.of("id", Sort.Direction.ASC));
    }
}
