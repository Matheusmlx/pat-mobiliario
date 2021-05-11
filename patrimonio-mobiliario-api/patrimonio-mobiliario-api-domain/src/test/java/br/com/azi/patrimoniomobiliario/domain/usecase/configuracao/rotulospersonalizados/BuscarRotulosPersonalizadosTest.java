package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados.converter.RotulosPersonalizadosConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BuscarRotulosPersonalizadosTest {

    private final Map<String, Object> rotulosPersonalizados = Map.of(
        "i18n", new Object[]{
            "INCORPORACAO_DADOS_GERAIS", Map.of("campos", Map.of("numero", "Número")),
            "INCORPORACAO_ITEM", Map.of("campos", Map.of("patrimonio", "Patrimônio"))
        });

    private BuscarRotulosPersonalizadosUseCase useCase;

    @Test
    public void deveRetornarOsRotulosPersonalizados() throws NoSuchMethodException {
        useCase = new BuscarRotulosPersonalizadosUseCaseImpl(
            rotulosPersonalizados,
            new RotulosPersonalizadosConverter()
        );

        final BuscarRotulosPersonalizadosOutputData outputData = useCase.executar();

        assertNotNull(outputData.getRotulosPersonalizados());
        assertEquals(outputData.getRotulosPersonalizados(), rotulosPersonalizados);
    }
}
