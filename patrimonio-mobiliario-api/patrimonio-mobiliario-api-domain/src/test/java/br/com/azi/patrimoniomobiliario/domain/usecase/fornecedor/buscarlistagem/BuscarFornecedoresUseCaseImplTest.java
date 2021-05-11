package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarFornecedoresUseCaseImplTest {

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarFornecedoresInputData inputData = new BuscarFornecedoresInputData();
        inputData.setSort("ASC");

        try {
            new BuscarFornecedoresUseCaseImpl(null, null, null).executar(inputData);
        } catch (Exception e) {
            Assert.assertEquals(3, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }

    @Test
    public void deveBuscarListagemComFiltrosCorretos() {
        SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration = Mockito.mock(SistemaDeFornecedoresIntegration.class);

        BuscarFornecedoresUseCaseImpl useCase = new BuscarFornecedoresUseCaseImpl(
            sistemaDeFornecedoresIntegration,
            new BuscarFornecedoresFiltroConverter(),
            new BuscarFornecedoresOutputDataConverter());

        BuscarFornecedoresInputData inputData = BuscarFornecedoresInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("ASC")
            .direction("")
            .conteudo("Nu")
            .build();

        Mockito.when(sistemaDeFornecedoresIntegration.buscarPorFiltro(any(Fornecedor.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Fornecedor>builder()
                    .items(
                        List.of(Fornecedor.builder()
                            .id(1L)
                            .nome("Nu Pagamentos S.A.")
                            .cpfCnpj("18236120000158")
                            .situacao(Fornecedor.Situacao.ATIVO)
                            .build(),
                        Fornecedor.builder()
                            .id(2L)
                            .nome("Fortaleza L.T.D.A")
                            .cpfCnpj("71845401000190")
                            .situacao(Fornecedor.Situacao.INATIVO)
                            .build())
                    )
                    .totalElements(1L)
                    .totalPages(1L)
                    .build()
            );

        BuscarFornecedoresOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(outputData.getItems().size(), 1);
        Assert.assertEquals(outputData.getItems().get(0).getNome(), "Nu Pagamentos S.A.");
        Assert.assertEquals(outputData.getItems().get(0).getId(), Long.valueOf(1));
        Assert.assertEquals(outputData.getItems().get(0).getCpfCnpj(), "18236120000158");
        Assert.assertEquals(outputData.getTotalElements(), Long.valueOf(1));
        Assert.assertEquals(outputData.getTotalPages(), Long.valueOf(1));
    }
}
