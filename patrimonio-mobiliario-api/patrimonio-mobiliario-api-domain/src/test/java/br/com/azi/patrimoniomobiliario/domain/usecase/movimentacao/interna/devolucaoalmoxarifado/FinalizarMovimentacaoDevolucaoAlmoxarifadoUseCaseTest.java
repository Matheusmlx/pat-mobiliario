package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.converter.FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEhDevolucaoAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorNaoAlmoxarifadoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            contaContabilDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            false,
            null,
            fixedClock,
            new FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSeIdMovimentacaoNulo() {
        useCase.executar(new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
         Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L));
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstaEmElaboracao() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.FINALIZADO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEhDevolucaoAlmoxarifadoException.class)
    public void deveFalharQuandoTipoMovimentacaoInvalida() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoInativoException.class)
    public void deveFalharQuandoOrgaoInativo() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharQuandoSetorOrigemInativo() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharQuandoSetorDestinoInativo() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorAlmoxarifadoException.class)
    public void deveFalharQuandoSetorOrigemAlmoxarifado() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorNaoAlmoxarifadoException.class)
    public void deveFalharQuandoSetorDestinoNaoAlmoxarifado() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test
    public void deveAtualizarInformacoesMovimentacaoFinalizado() {
        final String usuarioFinalizacao = "Usuário";

        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .usuarioFinalizacao(usuarioFinalizacao)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 12, 0, 0))
            .orgaoOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(13L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build()
            )
            .setorOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(14L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(false)
                    .build()
            )
            .setorDestino(
                UnidadeOrganizacional
                    .builder()
                    .id(14L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(true)
                    .build()
            )
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(13L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(false)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(14L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .build()
            ));

        Mockito.when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
    }

    @Test
    public void deveGerarLancamentosContabeisParaContaNaoAlmoxarifado() {
        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(13L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(14L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(false).build();
        UnidadeOrganizacional setorDestino = UnidadeOrganizacional.builder().id(14L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(true).build();

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 12, 0, 0))
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        ContaContabil contaContabil = ContaContabil.builder().id(255L).build();


        Patrimonio patrimonio =  Patrimonio.builder()
            .id(12L)
            .contaContabilAtual(contaContabil)
            .contaContabilClassificacao(contaContabil)
            .valorAquisicao(BigDecimal.valueOf(1500))
            .valorLiquido(BigDecimal.valueOf(1500))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(orgaoOrigem)
                    .orgaoDestino(orgaoOrigem)
                    .setorOrigem(setorOrigem)
                    .setorDestino(setorDestino)
                    .build()
            ));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong()))
            .thenReturn(List.of(
                MovimentacaoItem
                    .builder()
                    .patrimonio(patrimonio)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(
                List.of(patrimonio));

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoOrigem(),
            movimentacaoFinalizada.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO,
            LancamentoContabil.TipoLancamento.DEBITO);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoDestino(),
            movimentacaoFinalizada.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO,
            LancamentoContabil.TipoLancamento.CREDITO);
    }

    @Test
    public void deveGerarLancamentosContabeisParaContaAlmoxarifado() {
        useCase = new FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            contaContabilDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            true,
            "1234",
            fixedClock,
            new FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter()
        );

        FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData = new FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData(12L);

        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(13L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(14L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(false).build();
        UnidadeOrganizacional setorDestino = UnidadeOrganizacional.builder().id(14L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(true).build();

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 12, 0, 0))
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        ContaContabil contaContabil = ContaContabil.builder().id(255L).build();
        ContaContabil contaContabilAlmoxarifado = ContaContabil.builder().id(23L).build();
        Patrimonio patrimonio =  Patrimonio.builder()
            .id(12L)
            .contaContabilAtual(contaContabil)
            .contaContabilClassificacao(contaContabil)
            .valorAquisicao(BigDecimal.valueOf(1500))
            .valorLiquido(BigDecimal.valueOf(1500))
            .build();


        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
                    .orgaoOrigem(orgaoOrigem)
                    .setorOrigem(setorOrigem)
                    .setorDestino(setorDestino)
                    .orgaoDestino(orgaoOrigem)
                    .build()
            ));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(anyLong()))
            .thenReturn(List.of(
                MovimentacaoItem
                    .builder()
                    .patrimonio(patrimonio)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(List.of(patrimonio));

        Mockito.when(contaContabilDataProvider.buscarPorCodigo(anyString()))
            .thenReturn(Optional.of(contaContabilAlmoxarifado));

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoFinalizada);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoOrigem(),
            movimentacaoFinalizada.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO,
            LancamentoContabil.TipoLancamento.DEBITO);

        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabilAlmoxarifado,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoDestino(),
            movimentacaoFinalizada.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO,
            LancamentoContabil.TipoLancamento.CREDITO);
    }


}
