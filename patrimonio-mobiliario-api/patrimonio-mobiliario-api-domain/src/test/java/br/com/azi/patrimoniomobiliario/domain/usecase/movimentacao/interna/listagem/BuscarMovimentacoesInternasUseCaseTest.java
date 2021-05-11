package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuscarMovimentacoesInternasUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarMovimentacoesInternasUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarMovimentacoesInternasUseCaseImpl(
            movimentacaoDataProvider,
            sessaoUsuarioDataProvider,
            new BuscarMovimentacoesInternasFiltroConverter(),
            new BuscarMovimentacoesInternasOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarMovimentacoesInternasInputData inputData = BuscarMovimentacoesInternasInputData
            .builder()
            .page(3L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarMovimentacoesInternasQueUsuarioPossuiAcesso() {
        BuscarMovimentacoesInternasInputData inputData = BuscarMovimentacoesInternasInputData
            .builder()
            .page(1L)
            .size(10L)
            .direction("ASC")
            .build();

        List<String> funcoes = List.of("Mobiliario.Normal", "Mobiliario.Consulta");

        Movimentacao.Filtro filtro = Movimentacao.Filtro
            .builder()
            .usuarioId(1L)
            .funcoes(funcoes)
            .build();

        filtro.setPage(0L);
        filtro.setSize(10L);
        filtro.setDirection("ASC");

        Mockito.when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario.builder().id(1L).build());

        Mockito.when(movimentacaoDataProvider.buscarMovimentacaoInternaPorFiltro(any(Movimentacao.Filtro.class)))
            .thenReturn(ListaPaginada
                .<Movimentacao>builder()
                .items(
                    List.of(
                        Movimentacao
                            .builder()
                            .id(5L)
                            .orgaoOrigem(UnidadeOrganizacional.builder()
                                .id(129L)
                                .sigla("AEM-MS")
                                .build()
                            )
                            .orgaoDestino(UnidadeOrganizacional.builder()
                                .id(129L)
                                .sigla("AEM-MS")
                                .build()
                            )
                            .setorOrigem(UnidadeOrganizacional
                                .builder()
                                .id(1129L)
                                .sigla("DIPA")
                                .nome("Divisão de Patrimônio e Almoxarifado")
                                .build())
                            .setorDestino(UnidadeOrganizacional
                                .builder()
                                .id(5141L)
                                .sigla("ACS")
                                .nome("Assessoria de Comunicação Social")
                                .build())
                            .build())
                )
                .totalElements(1L)
                .totalPages(1L)
                .build());

        BuscarMovimentacoesInternasOutputData outputData = useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarMovimentacaoInternaPorFiltro(filtro);

        assertEquals(Long.valueOf(5), outputData.getItems().get(0).getId());
        assertEquals("AEM-MS", outputData.getItems().get(0).getOrgao());
        assertEquals("DIPA - Divisão de Patrimônio e Almoxarifado", outputData.getItems().get(0).getSetorOrigem());
        assertEquals("ACS - Assessoria de Comunicação Social", outputData.getItems().get(0).getSetorDestino());
        assertEquals(Long.valueOf(1), outputData.getTotalElements());
        assertEquals(Long.valueOf(1), outputData.getTotalPages());
    }
}
