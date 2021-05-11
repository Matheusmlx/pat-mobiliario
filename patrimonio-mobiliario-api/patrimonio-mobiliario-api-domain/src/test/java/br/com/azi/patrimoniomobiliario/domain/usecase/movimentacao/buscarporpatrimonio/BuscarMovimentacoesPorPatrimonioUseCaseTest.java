package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio;


import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarMovimentacoesPorPatrimonioUseCaseTest {

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharAoBuscarMovimentacoesSemPatrimonio() {
        BuscarMovimentacoesPorPatrimonioUseCase useCase = new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            movimentacaoItemDataProvider,
            new BuscarMovimentacoesPorPatrimonioOutputDataConverter());

        BuscarMovimentacoesPorPatrimonioInputData inputData = BuscarMovimentacoesPorPatrimonioInputData.builder().build();
        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarMovimentacoes() {

        LocalDateTime dataFinalizacao = LocalDateTime.of(2020, 5, 10, 10, 0, 0);
        LocalDateTime dataCadastro = LocalDateTime.of(2020, 7, 10, 10, 0, 0);

        BuscarMovimentacoesPorPatrimonioUseCase useCase = new BuscarMovimentacoesPorPatrimonioUseCaseImpl(
            movimentacaoItemDataProvider,
            new BuscarMovimentacoesPorPatrimonioOutputDataConverter()
        );

        BuscarMovimentacoesPorPatrimonioInputData inputData = BuscarMovimentacoesPorPatrimonioInputData
            .builder()
            .patrimonioId(5L)
            .build();

        Incorporacao incorporacao = Incorporacao
            .builder()
            .id(7L)
            .situacao(Incorporacao.Situacao.FINALIZADO)
            .nota("9845")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao
            .builder()
            .id(10L)
            .incorporacao(incorporacao)
            .build();

        Movimentacao movimentacao = Movimentacao
            .builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .dataFinalizacao(dataFinalizacao)
            .dataCadastro(dataCadastro)
            .setorOrigem(UnidadeOrganizacional.builder().descricao("ORG - origem").build())
            .setorDestino(UnidadeOrganizacional.builder().descricao("DEST - destino").build())
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .notaLancamentoContabil(NotaLancamentoContabil
                .builder()
                .id(1L)
                .numero("9845")
                .build())
            .build();

        Patrimonio patrimonio = Patrimonio
            .builder()
            .id(5L)
            .valorAquisicao(BigDecimal.valueOf(1800))
            .itemIncorporacao(itemIncorporacao)
            .build();

        Mockito.when(movimentacaoItemDataProvider.buscarPorPatrimonio(any(Long.class)))
            .thenReturn(List.of(MovimentacaoItem
                        .builder()
                        .movimentacao(movimentacao)
                        .patrimonio(patrimonio)
                        .build()));

        BuscarMovimentacoesPorPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1L), outputData.getItems().get(0).getId());
        Assert.assertEquals("DISTRIBUICAO", outputData.getItems().get(0).getTipo());
        Assert.assertEquals(BigDecimal.valueOf(1800L), outputData.getItems().get(0).getValorTotal());
        Assert.assertEquals("EM_ELABORACAO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals("ORG - origem", outputData.getItems().get(0).getSetorOrigem());
        Assert.assertEquals("DEST - destino", outputData.getItems().get(0).getSetorDestino());
        Assert.assertEquals(DateValidate.formatarData(dataFinalizacao), outputData.getItems().get(0).getDataFinalizacao());
        Assert.assertEquals(DateValidate.formatarData(dataCadastro), outputData.getItems().get(0).getDataCriacao());
        Assert.assertEquals("9845", outputData.getItems().get(0).getNumeroNotaLancamentoContabil());
    }

}
