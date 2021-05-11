package br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoUseCase;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepreciacaoItemProcessor implements ItemProcessor<PatrimonioEntity, CalcularDepreciacaoOutputData> {

    @Autowired
    private CalcularDepreciacaoUseCase calcularDepreciacaoUseCase;

    @Override
    public CalcularDepreciacaoOutputData process(PatrimonioEntity patrimonioEntity) throws Exception {
        final CalcularDepreciacaoInputData inputData = new CalcularDepreciacaoInputData(patrimonioEntity.getId());
        return calcularDepreciacaoUseCase.executar(inputData);
    }

}
