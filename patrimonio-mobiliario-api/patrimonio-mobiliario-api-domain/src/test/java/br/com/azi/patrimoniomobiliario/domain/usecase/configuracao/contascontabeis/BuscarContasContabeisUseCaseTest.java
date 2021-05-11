package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter.BuscarContasContabeisFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter.BuscarContasContabeisOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class BuscarContasContabeisUseCaseTest {

    @Test
    public void deveBuscarContasContabeis(){
        ContaContabilDataProvider dataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);

        BuscarContasContabeisUseCaseImpl useCase = new BuscarContasContabeisUseCaseImpl(
            dataProvider,
            configContaContabilDataProvider,
            new BuscarContasContabeisFiltroConverter(),
            new BuscarContasContabeisOutputDataConverter()
        );

        BuscarContasContabeisInputData inputData = BuscarContasContabeisInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("nome")
            .direction("ASC")
            .build();

        Mockito.when(dataProvider.buscarPorFiltro(any(ContaContabil.Filtro.class)))
            .thenReturn(ListaPaginada.<ContaContabil>builder()
                .items(List.of(
                    ContaContabil
                        .builder()
                        .id(1L)
                        .codigo("123456")
                        .descricao("MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS")
                        .situacao(ContaContabil.Situacao.ATIVO)
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                .build());

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil.builder()
                    .id(1L)
                    .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                    .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                    .tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO)
                    .situacao(ConfigContaContabil.Situacao.ATIVO)
                    .vidaUtil(2)
                    .percentualResidual(BigDecimal.ONE)
                    .build()
            ));

        BuscarContasContabeisOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals("ATIVO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals("MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS",outputData.getItems().get(0).getDescricao());
        Assert.assertEquals("123456",outputData.getItems().get(0).getCodigo());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals("DEPRECIAVEL", outputData.getItems().get(0).getTipo());
        Assert.assertEquals("ARMAMENTO", outputData.getItems().get(0).getTipoBem());
        Assert.assertEquals(Integer.valueOf(2), outputData.getItems().get(0).getVidaUtil());
        Assert.assertEquals(BigDecimal.ONE, outputData.getItems().get(0).getPercentualResidual());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());

    }


}
