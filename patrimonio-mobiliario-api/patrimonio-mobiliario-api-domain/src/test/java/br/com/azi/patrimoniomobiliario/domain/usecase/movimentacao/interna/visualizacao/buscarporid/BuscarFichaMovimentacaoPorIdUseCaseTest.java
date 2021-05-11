package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.converter.BuscarFichaMovimentacaoPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid.exception.MovimentacaoNaoEncontradaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarFichaMovimentacaoPorIdUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @InjectMocks
    private BuscarFichaMovimentacaoPorIdUseCaseImpl useCase;

    @InjectMocks
    private BuscarFichaMovimentacaoPorIdOutputDataConverter outputDataConverter;

    @Before
    public void gerarInstanciaDoUseCase() {
        useCase = new BuscarFichaMovimentacaoPorIdUseCaseImpl(movimentacaoDataProvider, outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        useCase.executar(new BuscarFichaMovimentacaoPorIdInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoForEncontrada() {
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(new BuscarFichaMovimentacaoPorIdInputData(1L));
    }

    @Test
    public void deveBuscarMovimentacaoPorId() {
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .codigo("00152")
                .numeroProcesso("22222")
                .motivoObservacao("motivo")
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .dataFinalizacao(LocalDateTime.of(2020,Month.MARCH,12,5,12))
                .orgaoOrigem(UnidadeOrganizacional.builder().sigla("DPGE").nome("Defensoria Pública Geral do Estado").build())
                .setorOrigem(UnidadeOrganizacional.builder().sigla("2SUBDEF").nome("Segunda Subdefensoria Pública-Geral").build())
                .setorDestino(UnidadeOrganizacional.builder().sigla("1SUBDEF").nome("Primeira Subdefensoria Pública-Geral").build())
                .usuarioCriacao("Joao")
                .build()));

        BuscarFichaMovimentacaoPorIdOutputData outputData = useCase.executar(new BuscarFichaMovimentacaoPorIdInputData(1L));

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("motivo", outputData.getMotivoObservacao());
        Assert.assertEquals("00152", outputData.getCodigo());
        Assert.assertEquals("DPGE - Defensoria Pública Geral do Estado", outputData.getOrgao());
        Assert.assertEquals("2SUBDEF - Segunda Subdefensoria Pública-Geral", outputData.getSetorOrigem());
        Assert.assertEquals("1SUBDEF - Primeira Subdefensoria Pública-Geral", outputData.getSetorDestino());
        Assert.assertEquals("DISTRIBUICAO", outputData.getTipo());
        Assert.assertEquals("FINALIZADO", outputData.getSituacao());
        Assert.assertEquals("Joao", outputData.getUsuarioCriacao());
        Assert.assertEquals("22222", outputData.getNumeroProcesso());
    }

}
