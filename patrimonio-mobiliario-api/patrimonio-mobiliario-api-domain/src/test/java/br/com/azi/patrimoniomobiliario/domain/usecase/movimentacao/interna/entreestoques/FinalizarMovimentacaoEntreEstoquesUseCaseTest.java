package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.FinalizarMovimentacaoEntreEstoquesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.converter.FinalizarMovimentacaoEntreEstoquesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEhEntreEstoquesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.SetorNaoAlmoxarifadoException;
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

@RunWith(MockitoJUnitRunner.class)
public class FinalizarMovimentacaoEntreEstoquesUseCaseTest {

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

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private FinalizarMovimentacaoEntreEstoquesUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new FinalizarMovimentacaoEntreEstoquesUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            sessaoUsuarioDataProvider,
            new FinalizarMovimentacaoEntreEstoquesOutputDataConverter(),
            fixedClock
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoNulo() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharSeMovimentacaoNaoEstaEmElaboracao() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(16L)
                    .situacao(Movimentacao.Situacao.FINALIZADO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEhEntreEstoquesException.class)
    public void deveFalharSeMovimentacaNaoPossuirTipoValido() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(16L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoInativoException.class)
    public void deveFalharSeOrgaoOrigemInativo() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(16L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharSeSetorOrigemInativo() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(16L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(100L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(105L)
                            .situacao(UnidadeOrganizacional.Situacao.INATIVO)
                            .build()
                    )
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharSeSetorDestinoInativo() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(100L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(105L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorDestino =  UnidadeOrganizacional.builder().id(106L).situacao(UnidadeOrganizacional.Situacao.INATIVO).build();
        Movimentacao movimentacao =  Movimentacao
            .builder()
            .id(16L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));

        useCase.executar(inputData);
    }

    @Test(expected = SetorNaoAlmoxarifadoException.class)
    public void deveFalharSeSetorNaoAlmoxarifado() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(100L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(105L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(false).build();
        UnidadeOrganizacional setorDestino =  UnidadeOrganizacional.builder().id(106L).situacao(UnidadeOrganizacional.Situacao.ATIVO) .almoxarifado(true).build();
        Movimentacao movimentacao =  Movimentacao
            .builder()
            .id(16L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));

        useCase.executar(inputData);
    }

    @Test
    public void deveMudarInformacoesMovimentacaoFinalizada() {
        final String usuarioFinalizacao = "Usuário";

        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(16L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(100L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .build()
            )
            .setorOrigem(
                UnidadeOrganizacional
                    .builder()
                    .id(105L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(true)
                    .build()
            )
            .setorDestino(
                UnidadeOrganizacional
                    .builder()
                    .id(106L)
                    .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                    .almoxarifado(true)
                    .build()
            )
            .usuarioFinalizacao(usuarioFinalizacao)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 12, 0, 0))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(16L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .orgaoOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(100L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .build()
                    )
                    .setorOrigem(
                        UnidadeOrganizacional
                            .builder()
                            .id(105L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .setorDestino(
                        UnidadeOrganizacional
                            .builder()
                            .id(106L)
                            .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                            .almoxarifado(true)
                            .build()
                    )
                    .build()
            ));

        Mockito.when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioFinalizacao);

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(16L)).thenReturn(List.of());

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).buscarPorId(16L);
        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).salvar(movimentacaoFinalizada);
    }

    @Test
    public void deveGerarLancamentosContabeis() {
        FinalizarMovimentacaoEntreEstoquesInputData inputData = new FinalizarMovimentacaoEntreEstoquesInputData(16L);

        ContaContabil contaContabil = ContaContabil.builder().id(1L).build();
        Patrimonio patrimonio =  Patrimonio.builder()
            .id(12L)
            .valorAquisicao(BigDecimal.valueOf(1500))
            .valorLiquido(BigDecimal.valueOf(1500))
            .contaContabilAtual(contaContabil)
            .build();
        UnidadeOrganizacional orgaoOrigem = UnidadeOrganizacional.builder().id(100L).situacao(UnidadeOrganizacional.Situacao.ATIVO).build();
        UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(105L).situacao(UnidadeOrganizacional.Situacao.ATIVO).almoxarifado(true).build();
        UnidadeOrganizacional setorDestino =  UnidadeOrganizacional.builder().id(106L).situacao(UnidadeOrganizacional.Situacao.ATIVO) .almoxarifado(true).build();
        Movimentacao movimentacao =  Movimentacao
            .builder()
            .id(16L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        Movimentacao movimentacaoFinalizada = Movimentacao
            .builder()
            .id(16L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.FINALIZADO)
            .orgaoOrigem(orgaoOrigem)
            .orgaoDestino(orgaoOrigem)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .dataFinalizacao(LocalDateTime.of(2021, 1, 12, 0, 0))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoFinalizada);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(16L))
            .thenReturn(List.of(
                MovimentacaoItem
                    .builder()
                    .patrimonio(patrimonio)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(List.of(patrimonio));

        useCase.executar(inputData);

        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).buscarPorId(16L);
        Mockito.verify(movimentacaoDataProvider, Mockito.times(1)).salvar(movimentacaoFinalizada);
        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarTodosPatrimonios(List.of(12L));
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoOrigem(),
            movimentacaoFinalizada.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.ENTRE_ESTOQUES,
            LancamentoContabil.TipoLancamento.DEBITO);
        Mockito.verify(lancamentoContabilDataProvider, Mockito.times(1)).registrarLancamentoContabil(contaContabil,
            patrimonio,
            movimentacaoFinalizada,
            movimentacaoFinalizada.getOrgaoDestino(),
            movimentacaoFinalizada.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.ENTRE_ESTOQUES,
            LancamentoContabil.TipoLancamento.CREDITO);
    }
}
