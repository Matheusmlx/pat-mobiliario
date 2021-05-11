package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.converter.GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception.GerarMemorandoMovimentacaoInternaFinalizadaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GerarMemorandoMovimentacaoInternaFinalizadaUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @InjectMocks
    private MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    @InjectMocks
    private GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter outputDataConverter;

    private GerarMemorandoMovimentacaoInternaFinalizadaUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            lancamentoContabilDataProvider,
            sistemaDeRelatoriosIntegration,
            memorandoMovimentacaoInternaUtils,
            outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData());
    }

    @Test(expected = GerarMemorandoMovimentacaoInternaFinalizadaException.class)
    public void deveFalharQuandoMovimentacaoDiferenteDeFinalizada() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(102L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                    .build()));

        useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData(102L));
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarMovimentacao() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData(102L));
    }

    @Test
    public void deveMontarMemorandoParaMovimentacaoEntreSetores() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .setorDestino(UnidadeOrganizacional.builder().id(2L) .sigla("BELMAR") .nome("1 Regional de Campo Grande - Unidade Belmar").build())
            .setorOrigem(UnidadeOrganizacional.builder().id(3L) .sigla("BELMAR") .nome("2 Regional de Campo Grande - Unidade Belmar").build())
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 10, 0, 0 ,0))
            .motivoObservacao("Testando os termos")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder().id(1L).descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.").build();

        Patrimonio patrimonio1 = Patrimonio.builder().id(1L).numero("0000000001").valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();
        Patrimonio patrimonio2 = Patrimonio.builder().id(2L).numero("0000000002").valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();

        MovimentacaoItem itemMovimentacao1 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio1)
            .build();

        MovimentacaoItem itemMovimentacao2 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio2)
            .build();

        LancamentoContabil lancamentoContabil1 =  LancamentoContabil
            .builder()
            .id(142L)
            .valor(BigDecimal.valueOf(10))
            .contaContabil(ContaContabil.builder().id(1L).codigo("123110504").descricao("CARROS DE COMBATE").build())
            .patrimonio(patrimonio1)
            .movimentacao(movimentacao)
            .build();

        LancamentoContabil lancamentoContabil2 =    LancamentoContabil
            .builder()
            .id(143L)
            .valor(BigDecimal.valueOf(10))
            .contaContabil(ContaContabil.builder().id(1L).codigo("123110504").descricao("CARROS DE COMBATE").build())
            .patrimonio(patrimonio2)
            .movimentacao(movimentacao)
            .build();

        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .setorDestino("BELMAR - 1 Regional de Campo Grande - Unidade Belmar")
            .setorOrigem("BELMAR - 2 Regional de Campo Grande - Unidade Belmar")
            .numeroMemorando("000001/2021")
            .tipo("Movimentação Entre Setores")
            .motivoObs("Testando os termos")
            .dataFinalizacao("10/01/2021")
            .mesAnoEnvio("Janeiro/2021")
            .valorEntrada(new BigDecimal("30").setScale(2, RoundingMode.HALF_EVEN))
            .valorDepreciadoAcumulado(new BigDecimal("10").setScale(2, RoundingMode.HALF_EVEN))
            .valorLiquido(new BigDecimal("20").setScale(2, RoundingMode.HALF_EVEN))
            .possuiItens(Boolean.TRUE)
            .emElaboracao(Boolean.FALSE)
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
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(itemMovimentacao1, itemMovimentacao2));
        Mockito.when(lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(any(), anyLong())).thenReturn(List.of(lancamentoContabil1, lancamentoContabil2));
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong())).thenReturn(Optional.of(itemMovimentacao1));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("memorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000001");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

    @Test
    public void deveMontarMemorandoParaDevolucaoAlmoxarifado() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .setorOrigem(UnidadeOrganizacional.builder().id(3L) .sigla("BELMAR") .nome("2 Regional de Campo Grande - Unidade Belmar").build())
            .setorDestino(UnidadeOrganizacional.builder().id(2L) .sigla("BELMAR") .nome("1 Regional de Campo Grande - Unidade Belmar").build())
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 10, 0, 0 ,0))
            .motivoObservacao("Testando os termos")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder().id(1L).descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.").build();

        Patrimonio patrimonio1 = Patrimonio.builder().id(1L).numero("0000000001").valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();
        Patrimonio patrimonio2 = Patrimonio.builder().id(2L).numero("0000000002").valorEntrada(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();

        MovimentacaoItem itemMovimentacao1 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio1)
            .build();

        MovimentacaoItem itemMovimentacao2 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonio2)
            .build();

        LancamentoContabil lancamentoContabil1 =  LancamentoContabil
            .builder()
            .id(142L)
            .valor(BigDecimal.valueOf(10))
            .contaContabil(ContaContabil.builder().id(1L).codigo("123110504").descricao("CARROS DE COMBATE").build())
            .patrimonio(patrimonio1)
            .movimentacao(movimentacao)
            .build();

        LancamentoContabil lancamentoContabil2 =    LancamentoContabil
            .builder()
            .id(143L)
            .valor(BigDecimal.valueOf(10))
            .contaContabil(ContaContabil.builder().id(1L).codigo("123110504").descricao("CARROS DE COMBATE").build())
            .patrimonio(patrimonio2)
            .movimentacao(movimentacao)
            .build();

        MemorandoMovimentacaoInterna memorando = MemorandoMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .setorDestino("BELMAR - 1 Regional de Campo Grande - Unidade Belmar")
            .setorOrigem("BELMAR - 2 Regional de Campo Grande - Unidade Belmar")
            .numeroMemorando("000002/2021")
            .tipo("Devolução Almoxarifado")
            .motivoObs("Testando os termos")
            .dataFinalizacao("10/01/2021")
            .mesAnoEnvio("Janeiro/2021")
            .valorEntrada(new BigDecimal("30").setScale(2, RoundingMode.HALF_EVEN))
            .valorDepreciadoAcumulado(new BigDecimal("10").setScale(2, RoundingMode.HALF_EVEN))
            .valorLiquido(new BigDecimal("20").setScale(2, RoundingMode.HALF_EVEN))
            .possuiItens(Boolean.TRUE)
            .emElaboracao(Boolean.FALSE)
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
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(itemMovimentacao1, itemMovimentacao2));
        Mockito.when(lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(any(), anyLong())).thenReturn(List.of(lancamentoContabil1, lancamentoContabil2));
        Mockito.when(movimentacaoDataProvider.buscarUltimoNumeroTermoResponsabilidade()).thenReturn(Optional.of(Movimentacao.builder().id(100L).numeroTermoResponsabilidade("000001").build()));
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong())).thenReturn(Optional.of(itemMovimentacao1));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(any(MemorandoMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("memorandomovimentacao.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000002");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarMemorandoMovimentacaoInterna(memorando);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

}
