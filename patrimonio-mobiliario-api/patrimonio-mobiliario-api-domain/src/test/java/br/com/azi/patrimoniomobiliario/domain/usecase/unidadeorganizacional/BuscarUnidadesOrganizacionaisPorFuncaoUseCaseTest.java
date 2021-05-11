package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.converter.BuscarUnidadesOrganizacionaisOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class BuscarUnidadesOrganizacionaisPorFuncaoUseCaseTest {

    @Test
    public void deveRetornarOrgaos() {

        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
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
                                .build(),
                            UnidadeOrganizacional.builder()
                                .id(3L)
                                .nome("Agência Municipal de Habitação 2")
                                .sigla("teste")
                                .codHierarquia("0000.0002.003")
                                .descricao("Descrição agência Municipal 2")
                                .build()));

        BuscarUnidadesOrganizacionaisUseCaseImpl usecase = new BuscarUnidadesOrganizacionaisUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarUnidadesOrganizacionaisOutputDataConverter()
        );

        BuscarUnidadesOrganizacionaisOutputData outputData = usecase.executar();

        Mockito.verify(sistemaDeGestaoAdministrativaIntegration, Mockito.times(1)).buscarUnidadesOrganizacionaisPorFuncao(any(List.class));
        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(3, outputData.getItems().size());
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande", outputData.getItems().get(0).getNome());
        Assert.assertEquals("AGEREG", outputData.getItems().get(0).getSigla());
        Assert.assertEquals("0000.0001", outputData.getItems().get(0).getCodHierarquia());
        Assert.assertEquals("Descrição agência de Regulação", outputData.getItems().get(0).getDescricao());

        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        Assert.assertEquals("Agência Municipal de Habitação 1", outputData.getItems().get(1).getNome());
        Assert.assertEquals("EMHA", outputData.getItems().get(1).getSigla());
        Assert.assertEquals("0000.0002", outputData.getItems().get(1).getCodHierarquia());
        Assert.assertEquals("Descrição agência Municipal 1", outputData.getItems().get(1).getDescricao());

        Assert.assertEquals(Long.valueOf(3), outputData.getItems().get(2).getId());
        Assert.assertEquals("Agência Municipal de Habitação 2", outputData.getItems().get(2).getNome());
        Assert.assertEquals("teste", outputData.getItems().get(2).getSigla());
        Assert.assertEquals("0000.0002.003", outputData.getItems().get(2).getCodHierarquia());
        Assert.assertEquals("Descrição agência Municipal 2", outputData.getItems().get(2).getDescricao());
    }
}
