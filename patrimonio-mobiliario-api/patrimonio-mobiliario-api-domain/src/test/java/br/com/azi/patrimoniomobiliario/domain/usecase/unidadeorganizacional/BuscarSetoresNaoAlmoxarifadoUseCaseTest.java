package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.converter.BuscarSetoresNaoAlmoxarifadoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarSetoresNaoAlmoxarifadoUseCaseTest {

    @Mock
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarSetoresNaoAlmoxarifadoUseCase useCase;

    @Before
    public void setupTest() {
        useCase = new BuscarSetoresNaoAlmoxarifadoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            new BuscarSetoresNaoAlmoxarifadoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoCodigoHierarquiaOrgaoForNulo() {
        useCase.executar(new BuscarSetoresNaoAlmoxarifadoInputData());

        verifyZeroInteractions(sistemaDeGestaoAdministrativaIntegration);
    }

    @Test
    public void deveRetornarOsSetoresDoOrgaoQueNaoSaoAlmoxarifadosQuandoOCodigoHierarquiaEstiverCorreto() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        final Long orgaoId = 1L;

        final UnidadeOrganizacional[] unidadesOrganizacionais = {
            UnidadeOrganizacional.builder().id(1L).build(),
            UnidadeOrganizacional.builder().id(2L).build(),
            UnidadeOrganizacional.builder().id(3L).build()
        };

        final BuscarSetoresNaoAlmoxarifadoOutputData outputDataEsperado =
            BuscarSetoresNaoAlmoxarifadoOutputData.builder()
                .items(Arrays.asList(
                    BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional.builder().id(1L).build(),
                    BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional.builder().id(2L).build(),
                    BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional.builder().id(3L).build()
                ))
                .build();

        when(sistemaDeGestaoAdministrativaIntegration.buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(anyLong(), anyList(), anyBoolean()))
            .thenReturn(Arrays.asList(unidadesOrganizacionais));

        final BuscarSetoresNaoAlmoxarifadoOutputData outputData = useCase.executar(new BuscarSetoresNaoAlmoxarifadoInputData(orgaoId));

        Assert.assertEquals(outputData, outputDataEsperado);
        verify(sistemaDeGestaoAdministrativaIntegration, times(1))
            .buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(orgaoId, funcoes, Boolean.FALSE);
    }
}
