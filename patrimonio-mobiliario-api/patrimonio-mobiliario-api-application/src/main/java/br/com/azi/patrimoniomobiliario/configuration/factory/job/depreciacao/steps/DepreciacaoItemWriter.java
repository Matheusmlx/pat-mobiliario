package br.com.azi.patrimoniomobiliario.configuration.factory.job.depreciacao.steps;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao.PersistirDepreciacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao.PersistirDepreciacaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@StepScope
@Component
@RequiredArgsConstructor
public class DepreciacaoItemWriter implements ItemWriter<CalcularDepreciacaoOutputData> {

    private final PersistirDepreciacaoUseCase persistirDepreciacaoUseCase;

    @Override
    public void write(List<? extends CalcularDepreciacaoOutputData> itens) throws Exception {

        PersistirDepreciacaoInputData inputData = PersistirDepreciacaoInputData
            .builder()
            .itens(
                itens.stream()
                    .map(item -> new PersistirDepreciacaoInputData.Item(item.getPatrimonio(), item.getDepreciacao()))
                    .collect(Collectors.toList())
            )
            .build();

        persistirDepreciacaoUseCase.executar(inputData);

    }
}
