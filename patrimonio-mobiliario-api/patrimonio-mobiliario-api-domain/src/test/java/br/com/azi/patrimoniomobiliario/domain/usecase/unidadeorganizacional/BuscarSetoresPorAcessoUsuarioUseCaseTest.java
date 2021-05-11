package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.converter.BuscarSetoresOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class BuscarSetoresPorAcessoUsuarioUseCaseTest {

    @Test
    public void deveRetornarUmaListaDeSetores() {

        SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(any(List.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande")
                    .sigla("AGEREG")
                    .codHierarquia("0000.0001")
                    .descricao("Descrição agência de Regulação")
                    .build()));

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarSetoresPorOrgaos(any(List.class)))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Assessoria de Relações Institucionais")
                    .sigla("ARI")
                    .codHierarquia("0000.0001")
                    .descricao("ARI - Assessoria de Relações Institucionais")
                    .build()));

        BuscarSetoresUseCaseImpl usecase = new BuscarSetoresUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarSetoresOutputDataConverter()
        );

        BuscarSetoresOutputData outputData = usecase.executar();

        Mockito.verify(sistemaDeGestaoAdministrativaIntegration, Mockito.times(1)).buscarSetoresPorOrgaos(any(List.class));
        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Assessoria de Relações Institucionais", outputData.getItems().get(0).getNome());
        Assert.assertEquals("ARI", outputData.getItems().get(0).getSigla());
        Assert.assertEquals("0000.0001", outputData.getItems().get(0).getCodHierarquia());
        Assert.assertEquals("ARI - Assessoria de Relações Institucionais", outputData.getItems().get(0).getDescricao());
    }
}
