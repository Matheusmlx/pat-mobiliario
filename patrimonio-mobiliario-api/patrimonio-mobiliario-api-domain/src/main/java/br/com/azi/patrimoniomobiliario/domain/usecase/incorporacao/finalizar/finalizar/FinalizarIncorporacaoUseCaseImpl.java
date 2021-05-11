package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
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
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.ConfigContaContabilNaoEncontradaException;
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
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FinalizarIncorporacaoUseCaseImpl implements FinalizarIncorporacaoUseCase {

    private final Clock clock;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final ConfigContaContabilDataProvider configContaContabilDataProvider;

    private final ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final Boolean bloquearValoresDivergentesAoFinalizar;

    private final boolean patrimonioParaContaAlmoxarifado;

    private final String codigoContaContabilAlmoxarifado;

    private final long limiteRegistrosProcessamentoSincrono;

    private final IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    private final FinalizarIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public FinalizarIncorporacaoOutputData executar(FinalizarIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarIncorporacaoEmModoElaboracao(incorporacao);
        validarDivergenciaDeValores(incorporacao.getValorNota(), inputData);
        validarCamposInativos(incorporacao);

        ContaContabil contaAlmoxarifado = null;
        if (patrimonioParaContaAlmoxarifado) {
            contaAlmoxarifado = buscarContaContabilAlmoxarifado();
            validarContaContabilAlmoxarifado(contaAlmoxarifado);
        }

        final List<ItemIncorporacao> itensIncorporacao = buscarItensIncorporacao(incorporacao.getId());
        validarIncorporacaoComItens(itensIncorporacao);
        validarIncorporacaoComTodosItensFinalizados(itensIncorporacao);
        validarNaturezaDespesaEContaContabil(itensIncorporacao);

        congelarConfiguracaoDaDepreciacao(itensIncorporacao);

        promoverImagensTemporariasItensIncorporacao(itensIncorporacao);

        final Incorporacao incorporacaoAtualizada = finalizarIncorporacao(incorporacao, itensIncorporacao, contaAlmoxarifado);

        return outputDataConverter.to(incorporacaoAtualizada);
    }

    private void validarDadosEntrada(FinalizarIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarIncorporacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Incorporacao buscarIncorporacao(FinalizarIncorporacaoInputData inputData) {
        return incorporacaoDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarDivergenciaDeValores(BigDecimal valorNota, FinalizarIncorporacaoInputData inputData) {
        if (Objects.nonNull(valorNota)) {
            BigDecimal valorTotalIncorporacao = buscarSomaDeValorTotalDosItens(inputData);
            if (Objects.nonNull(valorTotalIncorporacao) && Boolean.TRUE.equals(bloquearValoresDivergentesAoFinalizar) &&
                !valorNota.setScale(2, RoundingMode.DOWN).equals(valorTotalIncorporacao.setScale(2, RoundingMode.DOWN))) {
                throw new ValoresDivergentesException();
            }
        }
    }

    private BigDecimal buscarSomaDeValorTotalDosItens(FinalizarIncorporacaoInputData inputData) {
        return itemIncorporacaoDataProvider.buscaSomaDeValorTotalDosItens(inputData.getId());
    }

    private void validarCamposInativos(Incorporacao incorporacao) {
        validarReconhecimentoInativo(incorporacao);
        validarFornecedorInativo(incorporacao);
        validarOrgaoInativo(incorporacao);
        validarSetorInativo(incorporacao);
    }

    private void validarReconhecimentoInativo(Incorporacao incorporacao) {
        if (Reconhecimento.Situacao.INATIVO.equals(incorporacao.getReconhecimento().getSituacao())) {
            throw new ReconhecimentoInativoException();
        }
    }

    private void validarFornecedorInativo(Incorporacao incorporacao) {
        if (Objects.nonNull(incorporacao.getFornecedor()) &&
            Fornecedor.Situacao.INATIVO.equals(incorporacao.getFornecedor().getSituacao())) {
            throw new FornecedorInativoException();
        }
    }

    private void validarOrgaoInativo(Incorporacao incorporacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(incorporacao.getOrgao().getSituacao())) {
            throw new OrgaoInativoException();
        }
    }

    private void validarSetorInativo(Incorporacao incorporacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(incorporacao.getSetor().getSituacao())) {
            throw new SetorInativoException();
        }
    }

    private ContaContabil buscarContaContabilAlmoxarifado() {
        return contaContabilDataProvider.buscarPorCodigo(codigoContaContabilAlmoxarifado)
            .orElseThrow(ContaContabilAlmoxarifadoNaoEncontradaException::new);
    }

    private void validarContaContabilAlmoxarifado(ContaContabil contaAlmoxarifado) {
        if (ContaContabil.Situacao.INATIVO.equals(contaAlmoxarifado.getSituacao())) {
            throw new ContaContabilAlmoxarifadoInativaException();
        }
    }

    private List<ItemIncorporacao> buscarItensIncorporacao(Long incorporacaoId) {
        return itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(incorporacaoId);
    }

    private void validarIncorporacaoComItens(List<ItemIncorporacao> itensIncorporacao) {
        if (itensIncorporacao.isEmpty()) {
            throw new IncorporacaoNaoPossuiItensException();
        }
    }

    private void validarIncorporacaoComTodosItensFinalizados(List<ItemIncorporacao> itensIncorporacao) {
        final List<ItemIncorporacao> itensNaoFinalizados = itensIncorporacao.stream()
            .filter(item -> !ItemIncorporacao.Situacao.FINALIZADO.equals(item.getSituacao()))
            .collect(Collectors.toList());

        if (!itensNaoFinalizados.isEmpty()) {
            throw new IncorporacaoPossuiItensNaoFinalizadosException();
        }
    }

    private void validarNaturezaDespesaEContaContabil(List<ItemIncorporacao> itensIncorporacao) {
        for (ItemIncorporacao itemIncorporacao : itensIncorporacao) {
            validarNaturezaInativa(itemIncorporacao);
            validarVinculoEntreNaturezaDespesaContaContabil(itemIncorporacao);
            validarContaContabilInativa(itemIncorporacao);
            validarDivergenciaDeContas(itemIncorporacao);
        }
    }

    private void validarNaturezaInativa(ItemIncorporacao itemIncorporacao) {
        if (NaturezaDespesa.Situacao.INATIVO.equals(itemIncorporacao.getNaturezaDespesa().getSituacao())) {
            throw new NaturezaInativaException();
        }
    }

    private Boolean validarSeTipoEhDepreciavel(ConfigContaContabil configContaContabil) {
        return ConfigContaContabil.Tipo.DEPRECIAVEL.equals(configContaContabil.getTipo());
    }

    private void validarVinculoEntreNaturezaDespesaContaContabil(ItemIncorporacao itemIncorporacao) {
        if (Objects.isNull(itemIncorporacao.getNaturezaDespesa().getContaContabil())) {
            throw new VinculoEntreNaturezaDespesaContaContabilException();
        }
    }

    private void validarContaContabilInativa(ItemIncorporacao itemIncorporacao) {
        if (ContaContabil.Situacao.INATIVO.equals(itemIncorporacao.getContaContabil().getSituacao())) {
            throw new ContaContabilInativaException();
        }
    }

    private void validarDivergenciaDeContas(ItemIncorporacao itemIncorporacao) {
        Long contaContabilAtualId = itemIncorporacao.getNaturezaDespesa().getContaContabil().getId();
        if (!(itemIncorporacao.getContaContabil().getId().equals(contaContabilAtualId))) {
            throw new ContasContabeisDivergentesException();
        }
    }

    private void congelarConfiguracaoDaDepreciacao(List<ItemIncorporacao> itensIncorporacao) {
        for (ItemIncorporacao itemIncorporacao : itensIncorporacao) {
            ConfiguracaoDepreciacao configuracaoDepreciacao = configurarConfiguracaoContaContabil(itemIncorporacao);
            atualizarItemIncorporacao(itemIncorporacao, configuracaoDepreciacao);
        }
    }

    private ConfiguracaoDepreciacao configurarConfiguracaoContaContabil(ItemIncorporacao itemIncorporacao) {
        ConfigContaContabil configContaContabil = buscarConfiguracaoDeContaContabilClassificacao(itemIncorporacao.getContaContabil());
        ConfiguracaoDepreciacao configuracaoDepreciacao = getConfiguracaoDepreciacao(configContaContabil);
        return salvarConfiguracaoDepreciacao(configuracaoDepreciacao);
    }

    private ConfigContaContabil buscarConfiguracaoDeContaContabilClassificacao(ContaContabil contaContabil) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(contaContabil.getId());
        return configContaContabil.orElseThrow(ConfigContaContabilNaoEncontradaException::new);
    }

    private ConfiguracaoDepreciacao getConfiguracaoDepreciacao(ConfigContaContabil configContaContabil) {
        return ConfiguracaoDepreciacao
            .builder()
            .vidaUtil(configContaContabil.getVidaUtil())
            .percentualResidual(configContaContabil.getPercentualResidual())
            .contaContabil(configContaContabil.getContaContabil())
            .depreciavel(validarSeTipoEhDepreciavel(configContaContabil))
            .build();
    }

    private ConfiguracaoDepreciacao salvarConfiguracaoDepreciacao(ConfiguracaoDepreciacao configuracaoDepreciacao) {
        return configuracaoDepreciacaoDataProvider.salvar(configuracaoDepreciacao);
    }

    private void atualizarItemIncorporacao(ItemIncorporacao itemIncorporacao, ConfiguracaoDepreciacao configuracaoDepreciacaoSalva) {
        itemIncorporacao.setConfiguracaoDepreciacao(configuracaoDepreciacaoSalva);
        itemIncorporacaoDataProvider.atualizar(itemIncorporacao);
    }

    private void promoverImagensTemporariasItensIncorporacao(final List<ItemIncorporacao> itensIncorporacao) {
        for (ItemIncorporacao itemIncorporacao : itensIncorporacao) {
            if (StringUtils.isNotEmpty(itemIncorporacao.getUriImagem())) {
                sistemaDeArquivosIntegration.promote(Arquivo.builder()
                    .uri(itemIncorporacao.getUriImagem())
                    .build());
            }
        }
    }

    private Incorporacao finalizarIncorporacao(Incorporacao incorporacao, List<ItemIncorporacao> itensIncorporacao, ContaContabil contaAlmoxarifado) {
        if (validarNecessidadeDeProcessamentoAssincrono(incorporacao.getId())) {
            return prepararIncorporacaoProcessamentoAssincrono(incorporacao);
        } else {
            return processarFinalizacaoSincrona(incorporacao, itensIncorporacao, contaAlmoxarifado);
        }
    }

    private boolean validarNecessidadeDeProcessamentoAssincrono(Long incorporacaoId) {
        final long quantidadePatrimoniosIncorporacao = patrimonioDataProvider
            .buscarQuantidadePatrimoniosPorIncorporacaoId(incorporacaoId);

        return quantidadePatrimoniosIncorporacao > limiteRegistrosProcessamentoSincrono;
    }

    private Incorporacao prepararIncorporacaoProcessamentoAssincrono(Incorporacao incorporacao) {
        final LocalDateTime dataAtual = LocalDateTime.now(clock);
        incorporacao.setSituacao(Incorporacao.Situacao.EM_PROCESSAMENTO);
        incorporacao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        incorporacao.setDataFinalizacao(dataAtual);
        incorporacao.setDataInicioProcessamento(dataAtual);
        final Incorporacao incorporacaoEmProcessamento = salvarIncorporacao(incorporacao);

        gerarNotificacaoIncorporacaoEmProcessamento(incorporacaoEmProcessamento);

        return incorporacaoEmProcessamento;
    }

    private Incorporacao salvarIncorporacao(Incorporacao incorporacao) {
        return incorporacaoDataProvider.salvar(incorporacao);
    }

    private void gerarNotificacaoIncorporacaoEmProcessamento(Incorporacao incorporacao) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.INCORPORACAO)
                .origemId(incorporacao.getId())
                .assunto("Incorporação " + incorporacao.getCodigo())
                .mensagem("Em processamento")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(Usuario.builder().id(sessaoUsuarioDataProvider.get().getId()).build())
                .build()
        );
    }

    private Incorporacao processarFinalizacaoSincrona(Incorporacao incorporacao, List<ItemIncorporacao> itensIncorporacao, ContaContabil contaAlmoxarifado) {
        incorporarPatrimonios(incorporacao, itensIncorporacao, contaAlmoxarifado);
        return atualizarIncorporacaoComInformacoesFinalizacao(incorporacao);
    }

    private void incorporarPatrimonios(Incorporacao incorporacao, List<ItemIncorporacao> itensIncorporacao, ContaContabil contaAlmoxarifado) {
        final List<Patrimonio> patrimonios = buscarPatrimonios(incorporacao.getId());
        final Map<Long, ItemIncorporacao> itensIncorporacaoMap = converterItensIncorporacaoParaMap(itensIncorporacao);

        for (Patrimonio patrimonio : patrimonios) {
            ItemIncorporacao itemIncorporacao = encontrarItemIncorporacaoDoPatrimonio(itensIncorporacaoMap, patrimonio);
            incorporarPatrimonioHelper.incorporarPatrimonio(patrimonio, itemIncorporacao, incorporacao, contaAlmoxarifado);
        }
    }

    private List<Patrimonio> buscarPatrimonios(Long incorporacaoId) {
        return patrimonioDataProvider.buscarPatrimoniosPorIncorporacao(incorporacaoId);
    }

    private Map<Long, ItemIncorporacao> converterItensIncorporacaoParaMap(List<ItemIncorporacao> itensIncorporacao) {
        return itensIncorporacao.stream().collect(Collectors.toMap(ItemIncorporacao::getId, item -> item));
    }

    private ItemIncorporacao encontrarItemIncorporacaoDoPatrimonio(Map<Long, ItemIncorporacao> itensIncorporacaoMap, Patrimonio patrimonio) {
        return itensIncorporacaoMap.get(patrimonio.getItemIncorporacao().getId());
    }

    private Incorporacao atualizarIncorporacaoComInformacoesFinalizacao(Incorporacao incorporacao) {
        incorporacao.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        incorporacao.setDataFinalizacao(LocalDateTime.now(clock));
        return salvarIncorporacao(incorporacao);
    }

}
