package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosFiltroConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosIncorporados {

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private PatrimonioDataProvider patrimonioDataProvider;

    @Before
    public void setUp() {
        sistemaDeGestaoAdministrativaIntegration = Mockito.mock(SistemaDeGestaoAdministrativaIntegration.class);
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarPatrimoniosIncorporadosUseCase useCase = new BuscarPatrimoniosIncorporadosUseCaseImpl(
            new BuscarPatrimoniosIncorporadosFiltroConverter(),
            new BuscarPatrimoniosIncorporadosConverter(),
            sistemaDeGestaoAdministrativaIntegration,
            patrimonioDataProvider
        );

        BuscarPatrimoniosIncorporadosInputData inputData = BuscarPatrimoniosIncorporadosInputData.builder()
            .page(1L)
            .direction("ASC")
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarPatrimoniosIncorporados() {
        BuscarPatrimoniosIncorporadosUseCase useCase = new BuscarPatrimoniosIncorporadosUseCaseImpl(
            new BuscarPatrimoniosIncorporadosFiltroConverter(),
            new BuscarPatrimoniosIncorporadosConverter(),
            sistemaDeGestaoAdministrativaIntegration,
            patrimonioDataProvider
        );

        BuscarPatrimoniosIncorporadosInputData inputData = BuscarPatrimoniosIncorporadosInputData.builder()
            .page(1L)
            .size(10L)
            .direction("ASC")
            .build();

        List<UnidadeOrganizacional> orgaos = List.of(
            UnidadeOrganizacional.builder().id(5L).sigla("DPGE").build(),
            UnidadeOrganizacional.builder().id(150L).sigla("AGEPAN").build(),
            UnidadeOrganizacional.builder().id(300L).sigla("FUNDESPORTE").build()
        );

        List<UnidadeOrganizacional> setores = List.of(
            UnidadeOrganizacional.builder().id(27L).sigla("1SUBDEF").nome("Primeira Subdefensoria Pública-Geral").build(),
            UnidadeOrganizacional.builder().id(35L).sigla("ARI").nome("Assessoria de Relações Institucionais").build(),
            UnidadeOrganizacional.builder().id(180L).sigla("UALPAT").nome("Unidade de Almoxarifado e Patrimônio").build()
        );

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(anyList()))
            .thenReturn(orgaos);

        Mockito.when(sistemaDeGestaoAdministrativaIntegration.buscarSetoresPorOrgaos(anyList()))
            .thenReturn(setores);

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosIncorporados(any(Patrimonio.Filtro.class)))
            .thenReturn(ListaPaginada
                .<Patrimonio>builder()
                .items(List.of(
                    Patrimonio
                        .builder()
                        .numero("000001")
                        .orgao(orgaos.get(1))
                        .setor(setores.get(1))
                        .itemIncorporacao(ItemIncorporacao.builder().descricao("Item 1").build())
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .build(),
                    Patrimonio
                        .builder()
                        .numero("000002")
                        .orgao(orgaos.get(0))
                        .setor(setores.get(0))
                        .itemIncorporacao(ItemIncorporacao.builder().descricao("Item 2").build())
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .build(),
                    Patrimonio
                        .builder()
                        .numero("000003")
                        .orgao(orgaos.get(2))
                        .setor(setores.get(2))
                        .itemIncorporacao(ItemIncorporacao.builder().descricao("Item 3").build())
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .build()
                ))
                .totalPages(1L)
                .totalElements(3L)
                .build());

        BuscarPatrimoniosIncorporadosOutputData outputData = useCase.executar(inputData);

        assertEquals("000001 - Item 1", outputData.getItems().get(0).getDescricao());
        assertEquals("AGEPAN", outputData.getItems().get(0).getOrgao());
        assertEquals("ARI - Assessoria de Relações Institucionais", outputData.getItems().get(0).getSetor());
        assertEquals("ATIVO", outputData.getItems().get(0).getSituacao());

        assertEquals("000002 - Item 2", outputData.getItems().get(1).getDescricao());
        assertEquals("DPGE", outputData.getItems().get(1).getOrgao());
        assertEquals("1SUBDEF - Primeira Subdefensoria Pública-Geral", outputData.getItems().get(1).getSetor());
        assertEquals("ATIVO", outputData.getItems().get(1).getSituacao());

        assertEquals("000003 - Item 3", outputData.getItems().get(2).getDescricao());
        assertEquals("FUNDESPORTE", outputData.getItems().get(2).getOrgao());
        assertEquals("UALPAT - Unidade de Almoxarifado e Patrimônio", outputData.getItems().get(2).getSetor());
        assertEquals("ATIVO", outputData.getItems().get(2).getSituacao());
    }
}
