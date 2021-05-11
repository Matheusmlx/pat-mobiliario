package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.converter.FinalizarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ContaContabilAlmoxarifadoInativaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ContaContabilAlmoxarifadoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ContaContabilInativaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ContasContabeisDivergentesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.FornecedorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.IncorporacaoNaoPossuiItensException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.IncorporacaoPossuiItensNaoFinalizadosException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.NaturezaInativaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ReconhecimentoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.ValoresDivergentesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception.VinculoEntreNaturezaDespesaContaContabilException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarIncorporacaoUseCaseTest {

    private static final Boolean BLOQUEAR_VALORES_DIVERGENTES = true;
    private static final Long LIMITE_REGISTROS_PROCESSAMENTO_SINCRONO = 1L;
    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Mock
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Mock
    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    @Mock
    private IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private FinalizarIncorporacaoUseCase useCase;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        instanciarUseCase(false, null);
        useCase.executar(FinalizarIncorporacaoInputData.builder().build());
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarIncorporacao() {
        instanciarUseCase(false, null);

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(FinalizarIncorporacaoInputData.builder().id(1L).build());
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoSituacaoDaIncorporacaoNaoForIgualEmElaboracaoOuErroProcessamento() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(1L)
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = ValoresDivergentesException.class)
    public void deveFalharQuandoValorDaNotaEValorTotalDosItensForemDivergentesEBloqueioAtivado() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = new FinalizarIncorporacaoInputData();
        inputData.setId(1L);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .valorNota(BigDecimal.valueOf(102.557898))
                .build()
            )
        );

        when(itemIncorporacaoDataProvider.buscaSomaDeValorTotalDosItens(any(Long.class))).thenReturn(BigDecimal.valueOf(102.567898));

        useCase.executar(inputData);
    }

    @Test(expected = ReconhecimentoInativoException.class)
    public void deveFalharQuandoReconhecimentoEstiverInativo() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .valorNota(BigDecimal.TEN)
                .reconhecimento(Reconhecimento.builder().situacao(Reconhecimento.Situacao.INATIVO).build())
                .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = FornecedorInativoException.class)
    public void deveFalharQuandoFornecedorEstiverInativo() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .valorNota(BigDecimal.TEN)
                .reconhecimento(Reconhecimento.builder().situacao(Reconhecimento.Situacao.ATIVO).build())
                .fornecedor(Fornecedor.builder().situacao(Fornecedor.Situacao.INATIVO).build())
                .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoInativoException.class)
    public void deveFalharQuandoOrgaoEstiverInativo() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .valorNota(BigDecimal.TEN)
                .reconhecimento(Reconhecimento.builder().situacao(Reconhecimento.Situacao.ATIVO).build())
                .fornecedor(Fornecedor.builder().situacao(Fornecedor.Situacao.ATIVO).build())
                .orgao(UnidadeOrganizacional.builder().situacao(UnidadeOrganizacional.Situacao.INATIVO).build())
                .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = SetorInativoException.class)
    public void deveFalharQuandoSetorEstiverInativo() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(
            Optional.of(Incorporacao.builder()
                .id(1L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .valorNota(BigDecimal.TEN)
                .reconhecimento(Reconhecimento.builder().situacao(Reconhecimento.Situacao.ATIVO).build())
                .fornecedor(Fornecedor.builder().situacao(Fornecedor.Situacao.ATIVO).build())
                .orgao(UnidadeOrganizacional.builder().situacao(UnidadeOrganizacional.Situacao.ATIVO).build())
                .setor(UnidadeOrganizacional.builder().situacao(UnidadeOrganizacional.Situacao.INATIVO).build())
                .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = ContaContabilAlmoxarifadoNaoEncontradaException.class)
    public void deveFalharQuandoContaContabilAlmoxarifadoNaoForEncontrada() {
        final String codigoContaContabilAlmoxarifado = "1234";
        instanciarUseCase(true, codigoContaContabilAlmoxarifado);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(contaContabilDataProvider.buscarPorCodigo(anyString())).thenReturn(Optional.empty());

        useCase.executar(inputData);

        verify(contaContabilDataProvider, times(1)).buscarPorCodigo(codigoContaContabilAlmoxarifado);
    }

    @Test(expected = ContaContabilAlmoxarifadoInativaException.class)
    public void deveFalharQuandoContaContabilAlmoxarifadoEstiverInativa() {
        final String codigoContaContabilAlmoxarifado = "1234";
        instanciarUseCase(true, codigoContaContabilAlmoxarifado);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(contaContabilDataProvider.buscarPorCodigo(anyString()))
            .thenReturn(Optional.of(ContaContabil.builder().situacao(ContaContabil.Situacao.INATIVO).build()));

        useCase.executar(inputData);

        verify(contaContabilDataProvider, times(1)).buscarPorCodigo(codigoContaContabilAlmoxarifado);
    }

    @Test(expected = IncorporacaoNaoPossuiItensException.class)
    public void deveFalharQuandoIncorporacaoNaoPossuirItens() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(Collections.emptyList());

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoPossuiItensNaoFinalizadosException.class)
    public void deveFalharQuandoIncorporacaoPossuirItensNaoFinalizados() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                    .build(),
                ItemIncorporacao
                    .builder()
                    .id(2L)
                    .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
                    .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = NaturezaInativaException.class)
    public void deveFalharQuandoNaturezaDeDespesaDeAlgumItemEstiverInativa() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                    .naturezaDespesa(NaturezaDespesa.builder()
                        .id(1L)
                        .situacao(NaturezaDespesa.Situacao.INATIVO)
                        .build()
                    )
                    .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = VinculoEntreNaturezaDespesaContaContabilException.class)
    public void deveFalharQuandoNaoHouverVinculoEntreNaturezaDespesaEContaContabil() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                    .naturezaDespesa(NaturezaDespesa.builder()
                        .id(1L)
                        .situacao(NaturezaDespesa.Situacao.ATIVO)
                        .build()
                    )
                    .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = ContaContabilInativaException.class)
    public void deveFalharQuandoContaContabilEstiverInativa() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                    .naturezaDespesa(NaturezaDespesa.builder()
                        .id(1L)
                        .situacao(NaturezaDespesa.Situacao.ATIVO)
                        .contaContabil(ContaContabil.builder().situacao(ContaContabil.Situacao.INATIVO).build())
                        .build()
                    )
                    .contaContabil(ContaContabil.builder().situacao(ContaContabil.Situacao.INATIVO).build())
                    .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test(expected = ContasContabeisDivergentesException.class)
    public void deveFalharQuandoContaContabilDoItemIncorporacaoForDiferenteDaContaContabilDaNaturezaDespesa() {
        instanciarUseCase(false, null);

        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(construirIncorporacaoValida(1L)));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .situacao(ItemIncorporacao.Situacao.FINALIZADO)
                    .naturezaDespesa(NaturezaDespesa.builder()
                        .id(1L)
                        .situacao(NaturezaDespesa.Situacao.ATIVO)
                        .contaContabil(ContaContabil.builder().id(1L).situacao(ContaContabil.Situacao.ATIVO).build())
                        .build()
                    )
                    .contaContabil(ContaContabil.builder().id(2L).situacao(ContaContabil.Situacao.ATIVO).build())
                    .build()
            )
        );

        useCase.executar(inputData);
    }

    @Test
    public void devePrepararAIncorporacaoParaProcessamentoAssincronoQuandoONumeroDePatrimoniosExcederOParametro() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);

        final Incorporacao incorporacaoParaSalvar = construirIncorporacaoValida(incorporacaoId);
        incorporacaoParaSalvar.setSituacao(Incorporacao.Situacao.EM_PROCESSAMENTO);
        incorporacaoParaSalvar.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoParaSalvar.setDataFinalizacao(LOCAL_DATE.atStartOfDay());
        incorporacaoParaSalvar.setDataInicioProcessamento(LOCAL_DATE.atStartOfDay());

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(List.of(construirItemIncorporacaoValido(1L, incorporacaoExistente)));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(3L);

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario.builder().build());
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test
    public void deveGerarNotificacaoDeIncorporacaoEmProcessamentoQuandoFinalizacaoOcorrerDeFormaAssincrona() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);

        final Incorporacao incorporacaoParaSalvar = construirIncorporacaoValida(incorporacaoId);
        incorporacaoParaSalvar.setSituacao(Incorporacao.Situacao.EM_PROCESSAMENTO);
        incorporacaoParaSalvar.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoParaSalvar.setDataFinalizacao(LOCAL_DATE.atStartOfDay());
        incorporacaoParaSalvar.setDataInicioProcessamento(LOCAL_DATE.atStartOfDay());

        final SessaoUsuario sessaoUsuario = SessaoUsuario.builder()
            .id(1L)
            .login(incorporacaoParaSalvar.getUsuarioFinalizacao())
            .build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacaoId)
            .assunto("Incorporação " + incorporacaoExistente.getCodigo())
            .mensagem("Em processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(Usuario.builder().id(sessaoUsuario.getId()).build())
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(List.of(construirItemIncorporacaoValido(1L, incorporacaoExistente)));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(3L);

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(sessaoUsuarioDataProvider.get()).thenReturn(sessaoUsuario);
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoParaSalvar);

        useCase.executar(inputData);

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void devePermitirRealizarAFinalizacaoQuandoASituacaoDaIncorporacaoForErroProcessamento() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);
        incorporacaoExistente.setSituacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO);

        final Incorporacao incorporacaoParaSalvar = construirIncorporacaoValida(incorporacaoId);
        incorporacaoParaSalvar.setSituacao(Incorporacao.Situacao.EM_PROCESSAMENTO);
        incorporacaoParaSalvar.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoParaSalvar.setDataFinalizacao(LOCAL_DATE.atStartOfDay());
        incorporacaoParaSalvar.setDataInicioProcessamento(LOCAL_DATE.atStartOfDay());

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(construirItemIncorporacaoValido(1L, incorporacaoExistente)));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(3L);

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario.builder().build());
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test
    public void deveFinalizarIncorporacaoDeFormaSincronaQuandoNumeroPatrimoniosForMenorQueParametro() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);
        incorporacaoExistente.setSituacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO);

        final Incorporacao incorporacaoParaSalvar = construirIncorporacaoValida(incorporacaoId);
        incorporacaoParaSalvar.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoParaSalvar.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoParaSalvar.setDataFinalizacao(LOCAL_DATE.atStartOfDay());

        final ItemIncorporacao itemIncorporacao = construirItemIncorporacaoValido(1L, incorporacaoExistente);
        final Patrimonio patrimonio1 = construirPatrimonioIncorporacao(1L, itemIncorporacao);
        final Patrimonio patrimonio2 = construirPatrimonioIncorporacao(2L, itemIncorporacao);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(List.of(itemIncorporacao));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(1L);
        when(patrimonioDataProvider.buscarPatrimoniosPorIncorporacao(anyLong()))
            .thenReturn(List.of(patrimonio1, patrimonio2));

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(patrimonio1, itemIncorporacao, incorporacaoExistente, null);
        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(patrimonio2, itemIncorporacao, incorporacaoExistente, null);
        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test
    public void deveFinalizarIncorporacaoDeFormaSincronaQuandoNumeroPatrimoniosForMenorQueParametroEUtilizarContaAlmoxarifado() {
        final String codigoContaAlmoxarifado = "1234";
        final ContaContabil contaAlmoxarifado = ContaContabil.builder()
            .id(1L)
            .codigo(codigoContaAlmoxarifado)
            .situacao(ContaContabil.Situacao.ATIVO)
            .build();

        instanciarUseCase(true, codigoContaAlmoxarifado);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);
        incorporacaoExistente.setSituacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO);

        final Incorporacao incorporacaoParaSalvar = construirIncorporacaoValida(incorporacaoId);
        incorporacaoParaSalvar.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacaoParaSalvar.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoParaSalvar.setDataFinalizacao(LOCAL_DATE.atStartOfDay());

        final ItemIncorporacao itemIncorporacao = construirItemIncorporacaoValido(1L, incorporacaoExistente);
        final Patrimonio patrimonio1 = construirPatrimonioIncorporacao(1L, itemIncorporacao);
        final Patrimonio patrimonio2 = construirPatrimonioIncorporacao(2L, itemIncorporacao);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(contaContabilDataProvider.buscarPorCodigo(anyString()))
            .thenReturn(Optional.of(contaAlmoxarifado));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(List.of(itemIncorporacao));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosPorIncorporacaoId(anyLong())).thenReturn(1L);
        when(patrimonioDataProvider.buscarPatrimoniosPorIncorporacao(anyLong()))
            .thenReturn(List.of(patrimonio1, patrimonio2));

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(patrimonio1, itemIncorporacao, incorporacaoExistente, contaAlmoxarifado);
        verify(incorporarPatrimonioHelper, times(1)).incorporarPatrimonio(patrimonio2, itemIncorporacao, incorporacaoExistente, contaAlmoxarifado);
        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoParaSalvar);
    }

    @Test
    public void devePromoverOsArquivosTemporariosDosItensIncorporacaoParaQuandoAsValidacoesPassarem() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);
        final ItemIncorporacao itemIncorporacao = construirItemIncorporacaoValido(1L, incorporacaoExistente);

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(itemIncorporacao));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(new Incorporacao());

        useCase.executar(inputData);

        verify(sistemaDeArquivosIntegration, times(1)).promote(Arquivo.builder()
            .uri(itemIncorporacao.getUriImagem())
            .build());
    }

    @Test
    public void deveRetornarAsInformacoesDaIncorporacaoAtualizadaQuandoAsValidacoesPassarem() {
        instanciarUseCase(false, null);

        final Long incorporacaoId = 1L;
        final String usuarioLogado = "Usuário";
        final FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder()
            .id(incorporacaoId).build();

        final Incorporacao incorporacaoExistente = construirIncorporacaoValida(incorporacaoId);

        final Incorporacao incorporacaoAtualizada = construirIncorporacaoValida(incorporacaoId);
        incorporacaoAtualizada.setSituacao(Incorporacao.Situacao.EM_PROCESSAMENTO);
        incorporacaoAtualizada.setUsuarioFinalizacao(usuarioLogado);
        incorporacaoAtualizada.setDataFinalizacao(LOCAL_DATE.atStartOfDay());
        incorporacaoAtualizada.setDataInicioProcessamento(LOCAL_DATE.atStartOfDay());

        final ItemIncorporacao itemIncorporacao = construirItemIncorporacaoValido(1L, incorporacaoExistente);

        final FinalizarIncorporacaoOutputData outputDataEsperado = FinalizarIncorporacaoOutputData.builder()
            .id(incorporacaoAtualizada.getId())
            .codigo(incorporacaoAtualizada.getCodigo())
            .situacao(incorporacaoAtualizada.getSituacao().toString())
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacaoExistente));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(itemIncorporacao));

        when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.valueOf(12.45))
                .vidaUtil(12)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build()));

        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogado);
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);

        final FinalizarIncorporacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
    }

    private void instanciarUseCase(boolean patrimonioParaContaAlmoxarifado, String codigoContaContabilAlmoxarifado) {
        useCase = new FinalizarIncorporacaoUseCaseImpl(
            fixedClock,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            contaContabilDataProvider,
            sessaoUsuarioDataProvider,
            configContaContabilDataProvider,
                configuracaoDepreciacaoDataProvider,
            notificacaoDataProvider,
            sistemaDeArquivosIntegration,
            BLOQUEAR_VALORES_DIVERGENTES,
            patrimonioParaContaAlmoxarifado,
            codigoContaContabilAlmoxarifado,
            LIMITE_REGISTROS_PROCESSAMENTO_SINCRONO,
            incorporarPatrimonioHelper,
            new FinalizarIncorporacaoOutputDataConverter()
        );
    }

    private Incorporacao construirIncorporacaoValida(Long incorporacaoId) {
        return Incorporacao.builder()
            .id(incorporacaoId)
            .codigo("00001")
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .valorNota(BigDecimal.TEN)
            .reconhecimento(Reconhecimento.builder().situacao(Reconhecimento.Situacao.ATIVO).build())
            .fornecedor(Fornecedor.builder().situacao(Fornecedor.Situacao.ATIVO).build())
            .orgao(UnidadeOrganizacional.builder().situacao(UnidadeOrganizacional.Situacao.ATIVO).build())
            .setor(UnidadeOrganizacional.builder().situacao(UnidadeOrganizacional.Situacao.ATIVO).build())
            .build();
    }

    private ItemIncorporacao construirItemIncorporacaoValido(Long itemIncorporacaoId, Incorporacao incorporacao) {
        return ItemIncorporacao
            .builder()
            .id(itemIncorporacaoId)
            .situacao(ItemIncorporacao.Situacao.FINALIZADO)
            .naturezaDespesa(NaturezaDespesa.builder()
                .id(1L)
                .situacao(NaturezaDespesa.Situacao.ATIVO)
                .contaContabil(ContaContabil.builder().id(1L).situacao(ContaContabil.Situacao.ATIVO).build())
                .build()
            )
            .contaContabil(ContaContabil.builder().id(1L).situacao(ContaContabil.Situacao.ATIVO).build())
            .uriImagem("repo1:imagem1")
            .incorporacao(incorporacao)
            .build();
    }

    private Patrimonio construirPatrimonioIncorporacao(Long patrimonioId, ItemIncorporacao itemIncorporacao) {
        return Patrimonio.builder()
            .id(patrimonioId)
            .itemIncorporacao(itemIncorporacao)
            .build();
    }
}
