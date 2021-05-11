package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.GerarMemorandoMovimentacaoInternaEmElaboracaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.GerarMemorandoMovimentacaoInternaEmElaboracaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.converter.GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.exception.GerarMemorandoMovimentacaoEmElaboracaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseTest {
    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @InjectMocks
    private GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter outputDataConverter;

    @InjectMocks
    private MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    private GerarMemorandoMovimentacaoInternaEmElaboracaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            sistemaDeRelatoriosIntegration,
            memorandoMovimentacaoInternaUtils,
            outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData());
    }

    @Test(expected = GerarMemorandoMovimentacaoEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoNaoFinalizada() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(102L)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData(102L));
    }

    @Test
    public void deveMontarMemorando() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .build();


        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .numeroMemorando("000001/2021")
            .tipo("Movimentação Entre Setores")
            .possuiItens(Boolean.FALSE)
            .emElaboracao(Boolean.TRUE)
            .setorDestino("-")
            .setorOrigem("-")
            .temporaria(Boolean.FALSE)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(Boolean.FALSE);

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("termoguardaresponsabilidade.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000001");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

    @Test
    public void deveGerarProximoNumeroTermoResponsabilidade() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .setorOrigem(UnidadeOrganizacional.builder().id(3L) .sigla("BELMAR") .nome("2 Regional de Campo Grande - Unidade Belmar").build())
            .setorDestino(UnidadeOrganizacional.builder().id(2L) .sigla("BELMAR") .nome("1 Regional de Campo Grande - Unidade Belmar").build())
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .motivoObservacao("Testando os termos")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder().id(1L).descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.").build();

        ContaContabil contaContabil =  ContaContabil.builder().id(1L).codigo("123110504").descricao("CARROS DE COMBATE").build();

        Patrimonio patrimonio1 = Patrimonio.builder().id(1L).numero("0000000001").valorLiquido(BigDecimal.valueOf(10)).valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).contaContabilAtual(contaContabil).build();
        Patrimonio patrimonio2 = Patrimonio.builder().id(2L).numero("0000000002").valorLiquido(BigDecimal.valueOf(10)).valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).contaContabilAtual(contaContabil).build();

        MovimentacaoItem itemMovimentacao1 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio1)
            .build();

        MovimentacaoItem itemMovimentacao2 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio2)
            .build();

        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .setorOrigem("BELMAR - 2 Regional de Campo Grande - Unidade Belmar")
            .setorDestino("BELMAR - 1 Regional de Campo Grande - Unidade Belmar")
            .numeroMemorando("000002/2021")
            .tipo("Movimentação Entre Setores")
            .motivoObs("Testando os termos")
            .valorEntrada(new BigDecimal("30").setScale(2, RoundingMode.HALF_EVEN))
            .valorDepreciadoAcumulado(new BigDecimal("10").setScale(2, RoundingMode.HALF_EVEN))
            .valorLiquido(new BigDecimal("20").setScale(2, RoundingMode.HALF_EVEN))
            .possuiItens(Boolean.TRUE)
            .emElaboracao(Boolean.TRUE)
            .temporaria(Boolean.FALSE)
            .contasContabeis(List.of(
                MemorandoMovimentacaoInterna.ContaContabil
                    .builder()
                    .id(1L)
                    .descricao("123110504 - CARROS DE COMBATE")
                    .valorDepreciadoAcumulado(new BigDecimal("10").setScale(2, RoundingMode.HALF_EVEN))
                    .valorEntrada(new BigDecimal("30").setScale(2, RoundingMode.HALF_EVEN))
                    .valorLiquido(new BigDecimal("20").setScale(2, RoundingMode.HALF_EVEN))
                    .patrimonios(List.of(
                        MemorandoMovimentacaoInterna.Patrimonio
                            .builder()
                            .id(1L)
                            .numero("0000000001")
                            .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                            .valorEntrada(BigDecimal.valueOf(15))
                            .valorDepreciadoAcumulado(BigDecimal.valueOf(5))
                            .valorLiquido(BigDecimal.valueOf(10))
                            .build(),
                        MemorandoMovimentacaoInterna.Patrimonio
                            .builder()
                            .id(2L)
                            .numero("0000000002")
                            .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                            .valorEntrada(BigDecimal.valueOf(15))
                            .valorDepreciadoAcumulado(BigDecimal.valueOf(5))
                            .valorLiquido(BigDecimal.valueOf(10))
                            .build()))
                    .build()
            ))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(itemMovimentacao1, itemMovimentacao2));
        Mockito.when(movimentacaoDataProvider.buscarUltimoNumeroTermoResponsabilidade()).thenReturn(Optional.of(Movimentacao.builder().id(100L).numeroTermoResponsabilidade("000001").build()));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("termoguardaresponsabilidade.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000002");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

    @Test
    public void deveGerarMemorando() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .motivoObservacao("Testando os termos")
            .build();

        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("-")
            .setorOrigem("-")
            .setorDestino("-")
            .numeroMemorando("000001/2021")
            .tipo("Devolução Almoxarifado")
            .motivoObs("Testando os termos")
            .possuiItens(Boolean.FALSE)
            .emElaboracao(Boolean.TRUE)
            .temporaria(Boolean.FALSE)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(Boolean.FALSE);

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("termoguardaresponsabilidade.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData(102L));

        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

    @Test
    public void deveGerarUmMemorandoCasoSejaUmaMovimentacaoTemporaria() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.TEMPORARIA)
            .motivoObservacao("Testando os termos")
            .build();

        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("-")
            .setorOrigem("-")
            .setorDestino("-")
            .numeroMemorando("000001/2021")
            .tipo("Movimentação Temporária")
            .motivoObs("Testando os termos")
            .possuiItens(Boolean.FALSE)
            .emElaboracao(Boolean.TRUE)
            .temporaria(Boolean.TRUE)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(Boolean.FALSE);

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("memorando-movimentacao-em-elaboracao.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaEmElaboracaoInputData(102L));

        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }


}

