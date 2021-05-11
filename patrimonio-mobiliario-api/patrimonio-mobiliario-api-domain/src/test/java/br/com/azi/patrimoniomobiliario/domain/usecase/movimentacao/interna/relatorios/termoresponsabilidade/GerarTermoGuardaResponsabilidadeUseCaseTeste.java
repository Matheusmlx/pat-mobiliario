package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResposabilidadeMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.exception.GerarTermoResponsabilidadeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GerarTermoGuardaResponsabilidadeUseCaseTeste {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @InjectMocks
    private GerarTermoGuardaResponsabilidadeOutputDataConverter outputDataConverter;

    private GerarTermoGuardaResponsabilidadeUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new GerarTermoGuardaResponsabilidadeUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            lancamentoContabilDataProvider,
            sistemaDeRelatoriosIntegration,
            outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        useCase.executar(new GerarTermoGuardaResponsabilidadeInputData());
    }

    @Test(expected = GerarTermoResponsabilidadeException.class)
    public void deveFalharQuandoMovimentacaoNaoFinalizada() {
        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                    .id(102L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(new GerarTermoGuardaResponsabilidadeInputData(102L));
    }

    @Test
    public void deveMontarTermoResponsabilidade() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .setorDestino(UnidadeOrganizacional.builder().id(2L) .sigla("BELMAR") .nome("1 Regional de Campo Grande - Unidade Belmar").build())
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .responsavel(Responsavel.builder().id(2L).nome("Ana").build())
            .dataFinalizacao(LocalDateTime.of(2021, 1, 10, 0, 0 ,0))
            .motivoObservacao("Testando os termos")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder().id(1L).descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.").build();

        Patrimonio patrimonio1 = Patrimonio.builder().id(1L).numero("0000000001").valorLiquido(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();
        Patrimonio patrimonio2 = Patrimonio.builder().id(2L).numero("0000000002").valorLiquido(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();

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

        TermoGuardaResposabilidadeMovimentacaoInterna termo = TermoGuardaResposabilidadeMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .setor("BELMAR - 1 Regional de Campo Grande - Unidade Belmar")
            .numeroTermo("000001/2021")
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES.getValor())
            .motivoObs("Testando os termos")
            .totalDeBens(2)
            .valorTotal(BigDecimal.valueOf(20))
            .responsavel("Ana")
            .dataMovimentacao("10/01/2021")
            .patrimonios(List.of(
                TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(1L)
                    .numero("0000000001")
                    .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                    .valorLiquido(BigDecimal.valueOf(10))
                    .build(),
                TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(2L)
                    .numero("0000000002")
                    .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                    .valorLiquido(BigDecimal.valueOf(10))
                    .build()))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(itemMovimentacao1, itemMovimentacao2));
        Mockito.when(lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(any(), anyLong())).thenReturn(List.of(lancamentoContabil1, lancamentoContabil2));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarTermoGuardaResponsabilidadeMovimentacaoInterna(any(TermoGuardaResposabilidadeMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("termoguardaresponsabilidade.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarTermoGuardaResponsabilidadeInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000001");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarTermoGuardaResponsabilidadeMovimentacaoInterna(termo);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

    @Test
    public void deveGerarProximoNumeroTermoResponsabilidade() {
        Movimentacao movimentacao = Movimentacao.builder()
            .id(102L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L) .sigla("DPGE") .nome("Defensoria Pública Geral do Estado").build())
            .setorDestino(UnidadeOrganizacional.builder().id(2L) .sigla("BELMAR") .nome("1 Regional de Campo Grande - Unidade Belmar").build())
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 10, 0, 0 ,0))
            .motivoObservacao("Testando os termos")
            .build();

        ItemIncorporacao itemIncorporacao = ItemIncorporacao.builder().id(1L).descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.").build();

        Patrimonio patrimonio1 = Patrimonio.builder().id(1L).numero("0000000001").valorLiquido(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();
        Patrimonio patrimonio2 = Patrimonio.builder().id(2L).numero("0000000002").valorLiquido(BigDecimal.valueOf(15)).itemIncorporacao(itemIncorporacao).build();

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

        TermoGuardaResposabilidadeMovimentacaoInterna termo = TermoGuardaResposabilidadeMovimentacaoInterna
            .builder()
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .setor("BELMAR - 1 Regional de Campo Grande - Unidade Belmar")
            .numeroTermo("000002/2021")
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES.getValor())
            .motivoObs("Testando os termos")
            .totalDeBens(2)
            .valorTotal(BigDecimal.valueOf(20))
            .responsavel("-")
            .dataMovimentacao("10/01/2021")
            .patrimonios(List.of(
                TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(1L)
                    .numero("0000000001")
                    .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                    .valorLiquido(BigDecimal.valueOf(10))
                    .build(),
                TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(2L)
                    .numero("0000000002")
                    .descricao("Caminhonete pickup - cabine dupla 4x4 motor 163 CV, direção hidráulica.")
                    .valorLiquido(BigDecimal.valueOf(10))
                    .build()))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong())).thenReturn(List.of(itemMovimentacao1, itemMovimentacao2));
        Mockito.when(lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(any(), anyLong())).thenReturn(List.of(lancamentoContabil1, lancamentoContabil2));
        Mockito.when(movimentacaoDataProvider.buscarUltimoNumeroTermoResponsabilidade()).thenReturn(Optional.of(Movimentacao.builder().id(100L).numeroTermoResponsabilidade("000001").build()));

        Mockito.when(sistemaDeRelatoriosIntegration.gerarTermoGuardaResponsabilidadeMovimentacaoInterna(any(TermoGuardaResposabilidadeMovimentacaoInterna.class)))
            .thenReturn(
                Arquivo.builder()
                    .nome("termoguardaresponsabilidade.pdf")
                    .contentType("application/pdf")
                    .build());

        useCase.executar(new GerarTermoGuardaResponsabilidadeInputData(102L));

        movimentacao.setNumeroTermoResponsabilidade("000002");
        verify(sistemaDeRelatoriosIntegration, times(1)).gerarTermoGuardaResponsabilidadeMovimentacaoInterna(termo);
        verify(movimentacaoDataProvider, times(1)).salvar(movimentacao);
    }

}
