package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.converter.BuscarFornecedorPorIdOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarFornacedorPorIdUseCaseImplTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {
        BuscarFornecedorPorIdUseCaseImpl useCase = new BuscarFornecedorPorIdUseCaseImpl(
            Mockito.mock(SistemaDeFornecedoresIntegration.class),
            new BuscarFornecedorPorIdOutputDataConverter()
        );

        BuscarFornecedorPorIdInputData inputData = new BuscarFornecedorPorIdInputData();
        inputData.setId(null);
        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarFornecedor() {
        SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration = Mockito.mock(SistemaDeFornecedoresIntegration.class);
        BuscarFornecedorPorIdInputData inputData = Mockito.mock(BuscarFornecedorPorIdInputData.class);

        BuscarFornecedorPorIdUseCaseImpl useCase = new BuscarFornecedorPorIdUseCaseImpl(
            sistemaDeFornecedoresIntegration,
            new BuscarFornecedorPorIdOutputDataConverter()
        );

        Mockito.when(sistemaDeFornecedoresIntegration.buscarPorId(any(Long.class)))
            .thenReturn(
                Fornecedor.builder()
                    .id(1L)
                    .nome("Banco do Brasil")
                    .cpfCnpj("00000000000")
                    .build()
            );

        inputData.builder().id(1L).build();

        BuscarFornecedorPorIdOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(outputData.getId(), Long.valueOf(1));
        Assert.assertEquals(outputData.getCpfCnpj(), "00000000000");
        Assert.assertEquals(outputData.getNome(), "Banco do Brasil");
    }
}
