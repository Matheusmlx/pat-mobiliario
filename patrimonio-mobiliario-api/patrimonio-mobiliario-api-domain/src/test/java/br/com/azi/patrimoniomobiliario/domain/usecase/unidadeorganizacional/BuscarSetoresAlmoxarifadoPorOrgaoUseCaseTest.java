package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.converter.BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuscarSetoresAlmoxarifadoPorOrgaoUseCaseTest {

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Test
    public void deveRetornarUnidadesFilhas() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());


        BuscarSetoresAlmoxarifadoPorOrgaoInputData inputData = new BuscarSetoresAlmoxarifadoPorOrgaoInputData();
        inputData.setIdOrgao(1L);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(anyLong(), anyList(), anyBoolean()))
            .thenReturn(List.of(
                UnidadeOrganizacional.builder()
                    .id(1L)
                    .nome("Agência de Regulação dos Serviços Públicos Delegados de Campo Grande")
                    .sigla("AGEREG")
                    .codHierarquia("0000.0001")
                    .build(),
                UnidadeOrganizacional.builder()
                    .id(2L)
                    .nome("Agência Municipal de Habitação")
                    .sigla("EMHA")
                    .codHierarquia("0000.0002")
                    .descricao("Descrição agência Municipal")
                    .build()));

        BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl usecase = new BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter());

        BuscarSetoresAlmoxarifadoPorOrgaoOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        Assert.assertEquals("Agência Municipal de Habitação", outputData.getItems().get(1).getNome());
        Assert.assertEquals("EMHA", outputData.getItems().get(1).getSigla());
        Assert.assertEquals("0000.0002", outputData.getItems().get(1).getCodHierarquia());
        Assert.assertEquals("Descrição agência Municipal", outputData.getItems().get(1).getDescricao());

        verify(sistemaDeGestaoAdministrativaIntegration, times(1))
            .buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(1L, funcoes, Boolean.TRUE);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter converter = Mockito.mock(BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter.class);

        BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl usecase = new BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            converter
        );

        usecase.executar(new BuscarSetoresAlmoxarifadoPorOrgaoInputData());
    }
}
