package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.converter.BuscarFundosOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarFundosUseCaseTest {

    @Test
    public void deveBuscarFundos() {
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);
        BuscarFundosInputData inputData = new BuscarFundosInputData(1L);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarFundosPorEstruturaAdministradoraEFuncao(any(List.class), any(Long.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande")
                    .sigla("AGEREG")
                    .codHierarquia("0000.0001")
                    .descricao("Descrição agência de Regulação")
                    .build(),
                UnidadeOrganizacional.builder()
                    .id(2L)
                    .nome("Agência Municipal de Habitação 1")
                    .sigla("EMHA")
                    .codHierarquia("0000.0002")
                    .descricao("Descrição agência Municipal 1")
                    .build()));


        BuscarFundosUseCaseImpl usecase = new BuscarFundosUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarFundosOutputDataConverter());

        BuscarFundosOutputData outputData = usecase.executar(inputData);

        Mockito.verify(sistemaDeGestaoAdministrativaIntegration, Mockito.times(1)).buscarFundosPorEstruturaAdministradoraEFuncao(any(List.class), any(Long.class));
        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(2, outputData.getItems().size());
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande", outputData.getItems().get(0).getNome());
        Assert.assertEquals("AGEREG", outputData.getItems().get(0).getSigla());
        Assert.assertEquals("0000.0001", outputData.getItems().get(0).getCodHierarquia());
        Assert.assertEquals("Descrição agência de Regulação", outputData.getItems().get(0).getDescricao());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarEstruturaAdministradora() {
        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        BuscarFundosUseCaseImpl usecase = new BuscarFundosUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarFundosOutputDataConverter());

        usecase.executar(new BuscarFundosInputData());
    }
}
