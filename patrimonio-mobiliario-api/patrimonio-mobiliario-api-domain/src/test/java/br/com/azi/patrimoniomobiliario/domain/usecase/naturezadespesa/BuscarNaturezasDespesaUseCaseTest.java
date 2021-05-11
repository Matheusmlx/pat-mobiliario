package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.conveter.BuscarNaturezasDespesaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception.VinculoMaterialServicoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarNaturezasDespesaUseCaseTest {

    @Test
    public void deveBuscarNaurezasDespesa() {
        NaturezaDespesaDataProvider naturezaDespesaDataProvider = Mockito.mock(NaturezaDespesaDataProvider.class);
        ItemCatalogoDataProvider itemCatalogoDataProvider = Mockito.mock(ItemCatalogoDataProvider.class);

        BuscarNaturezasDespesaUseCaseImpl useCase = new BuscarNaturezasDespesaUseCaseImpl(
            naturezaDespesaDataProvider,
            itemCatalogoDataProvider,
            new BuscarNaturezasDespesaOutputDataConverter());

        Mockito.when(itemCatalogoDataProvider.buscarPorCodigo(any(String.class)))
            .thenReturn(Optional.of(
                ItemSimplificado
                    .builder()
                    .id(1L)
                    .codigo("0000019")
                    .descricao("Caminhonete pick up - descrição: cabine dupla tração 4x4 motor 163 CV, direção hidráulica")
                    .situacao("ATIVO")
                    .status("APROVADO")
                    .tipo("MATERIAL")
                    .materialServicoId(1L)
                    .build()));

        Mockito.when(naturezaDespesaDataProvider.buscarPorFiltro(any(Long.class)))
            .thenReturn(List.of(
                NaturezaDespesa
                    .builder()
                    .id(1L)
                    .descricao("Veículos Diversos")
                    .contaContabil(ContaContabil.builder().id(1L).build())
                    .situacao(NaturezaDespesa.Situacao.ATIVO)
                    .despesa("0001")
                    .build()));

        BuscarNaturezasDespesaOutputData outputData = useCase.executar(new BuscarNaturezasDespesaInputData("0000019"));

        Assert.assertEquals(Long.valueOf(1L), outputData.getItems().get(0).getId());
        Assert.assertEquals("0001", outputData.getItems().get(0).getDespesa());
        Assert.assertEquals("Veículos Diversos", outputData.getItems().get(0).getDescricao());
    }

    @Test(expected = VinculoMaterialServicoException.class)
    public void deveFalharQuandoItemNaoPossuirVinculoComMaterialServico() {
        NaturezaDespesaDataProvider naturezaDespesaDataProvider = Mockito.mock(NaturezaDespesaDataProvider.class);
        ItemCatalogoDataProvider itemCatalogoDataProvider = Mockito.mock(ItemCatalogoDataProvider.class);

        BuscarNaturezasDespesaUseCaseImpl useCase = new BuscarNaturezasDespesaUseCaseImpl(
            naturezaDespesaDataProvider,
            itemCatalogoDataProvider,
            new BuscarNaturezasDespesaOutputDataConverter());

        Mockito.when(itemCatalogoDataProvider.buscarPorCodigo(any(String.class)))
            .thenReturn(Optional.of(
                ItemSimplificado
                    .builder()
                    .id(1L)
                    .codigo("0000019")
                    .descricao("Caminhonete pick up - descrição: cabine dupla tração 4x4 motor 163 CV, direção hidráulica")
                    .situacao("ATIVO")
                    .status("APROVADO")
                    .tipo("MATERIAL")
                    .build()));

        useCase.executar(new BuscarNaturezasDespesaInputData("0000019"));

    }
}
