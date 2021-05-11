package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.converter.FinalizarDistribuicaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.ContaContabilAlmoxarifadoInativaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.DistribuicaoNaoPossuiItensException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.MovimentacaoNaoEhDistribuicaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.OrgaoOrigemInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorDestinoDistribuicaoEhAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorDestinoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorOrigemDistribuicaoNaoEhAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception.SetorOrigemInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class FinalizarDistribuicaoUseCaseImpl implements FinalizarDistribuicaoUseCase {

    private final Clock clock;

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final Boolean patrimonioParaContaAlmoxarifado;

    private final String codigoContaContabilAlmoxarifado;

    private final Long limiteRegistrosProcessamentoSincrono;

    private final DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    private final FinalizarDistribuicaoOutputDataConverter converter;

    @Override
    public FinalizarDistribuicaoOutputData executar(FinalizarDistribuicaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao distribuicao = buscarMovimentacao(inputData);
        validarMovimentacaoEmModoElaboracao(distribuicao);
        validarMovimentacaoDoTipoDistribuicao(distribuicao);
        validarOrgaoOrigemAtivo(distribuicao);
        validarSetorOrigemAtivo(distribuicao);
        validarSetorDestinoAtivo(distribuicao);
        validarSetorOrigemAlmoxarifado(distribuicao);
        validarSetorDestinoNaoAlmoxarifado(distribuicao);

        final Long quantidadeItensMovimentacao = buscarQuantidadeItensMovimentacao(distribuicao.getId());
        validarDistribuicaoComItensAdicionados(quantidadeItensMovimentacao);

        ContaContabil contaAlmoxarifado = null;
        if (Boolean.TRUE.equals(patrimonioParaContaAlmoxarifado)) {
            contaAlmoxarifado = buscarContaContabilAlmoxarifado();
            validarContaContabilAlmoxarifadoAtiva(contaAlmoxarifado);
        }

        final Movimentacao distribuicaoAtualizada = finalizarDistribuicao(distribuicao, contaAlmoxarifado,
            quantidadeItensMovimentacao);

        return converter.to(distribuicaoAtualizada);
    }

    private void validarDadosEntrada(FinalizarDistribuicaoInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarDistribuicaoInputData::getId, Objects::nonNull, "O id da movimentacão é nulo.")
            .get();
    }

    private Movimentacao buscarMovimentacao(FinalizarDistribuicaoInputData inputData) {
        final Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(DistribuicaoNaoEncontradaException::new);
    }

    private void validarMovimentacaoEmModoElaboracao(Movimentacao distribuicao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(distribuicao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(distribuicao.getSituacao())) {
            throw new DistribuicaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarMovimentacaoDoTipoDistribuicao(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.DISTRIBUICAO.equals(movimentacao.getTipo())) {
            throw new MovimentacaoNaoEhDistribuicaoException();
        }
    }

    private void validarOrgaoOrigemAtivo(Movimentacao distribuicao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(distribuicao.getOrgaoOrigem().getSituacao())) {
            throw new OrgaoOrigemInativoException();
        }
    }

    private void validarSetorOrigemAtivo(Movimentacao distribuicao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(distribuicao.getSetorOrigem().getSituacao())) {
            throw new SetorOrigemInativoException();
        }
    }

    private void validarSetorDestinoAtivo(Movimentacao distribuicao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(distribuicao.getSetorDestino().getSituacao())) {
            throw new SetorDestinoInativoException();
        }
    }

    private void validarSetorOrigemAlmoxarifado(Movimentacao distribuicao) {
        if (Boolean.FALSE.equals(distribuicao.getSetorOrigem().getAlmoxarifado())) {
            throw new SetorOrigemDistribuicaoNaoEhAlmoxarifadoException();
        }
    }

    private void validarSetorDestinoNaoAlmoxarifado(Movimentacao distribuicao) {
        if (Boolean.TRUE.equals(distribuicao.getSetorDestino().getAlmoxarifado())) {
            throw new SetorDestinoDistribuicaoEhAlmoxarifadoException();
        }
    }

    private Long buscarQuantidadeItensMovimentacao(Long movimentacaoId) {
        return movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(movimentacaoId);
    }

    private void validarDistribuicaoComItensAdicionados(Long quantidadeItensMovimentacao) {
        if (quantidadeItensMovimentacao == 0) {
            throw new DistribuicaoNaoPossuiItensException();
        }
    }

    private ContaContabil buscarContaContabilAlmoxarifado() {
        return contaContabilDataProvider.buscarPorCodigo(codigoContaContabilAlmoxarifado)
            .orElseThrow(ContaContabilNaoEncontradaException::new);
    }

    private void validarContaContabilAlmoxarifadoAtiva(ContaContabil contaAlmoxarifado) {
        if (ContaContabil.Situacao.INATIVO.equals(contaAlmoxarifado.getSituacao())) {
            throw new ContaContabilAlmoxarifadoInativaException();
        }
    }

    private Movimentacao finalizarDistribuicao(Movimentacao distribuicao, ContaContabil contaAlmoxarifado, Long quantidadeItens) {
        if (validarNecessidadeProcessamentoAssincrono(quantidadeItens)) {
            return prepararMovimentacaoParaProcessamentoAssincrono(distribuicao);
        } else {
            return processarFinalizacaoSincrona(distribuicao, contaAlmoxarifado);
        }
    }

    private boolean validarNecessidadeProcessamentoAssincrono(Long quantidadeItensMovimentacao) {
        return quantidadeItensMovimentacao > limiteRegistrosProcessamentoSincrono;
    }

    private Movimentacao prepararMovimentacaoParaProcessamentoAssincrono(Movimentacao distribuicao) {
        final LocalDateTime dataAtual = LocalDateTime.now(clock);
        distribuicao.setSituacao(Movimentacao.Situacao.EM_PROCESSAMENTO);
        distribuicao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        distribuicao.setDataFinalizacao(dataAtual);
        distribuicao.setDataInicioProcessamento(dataAtual);
        final Movimentacao distribuicaoEmProcessamento = salvarDistribuicao(distribuicao);

        gerarNotificacaoDistribuicaoEmProcessamento(distribuicaoEmProcessamento);

        return distribuicaoEmProcessamento;
    }

    private Movimentacao salvarDistribuicao(Movimentacao distribuicao) {
        return movimentacaoDataProvider.salvar(distribuicao);
    }

    private void gerarNotificacaoDistribuicaoEmProcessamento(Movimentacao distribuicao) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.DISTRIBUICAO)
                .origemId(distribuicao.getId())
                .assunto("Distribuição " + distribuicao.getCodigo())
                .mensagem("Em processamento")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(Usuario.builder().id(sessaoUsuarioDataProvider.get().getId()).build())
                .build()
        );
    }

    private Movimentacao processarFinalizacaoSincrona(Movimentacao distribuicao, ContaContabil contaAlmoxarifado) {
        final List<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarPorMovimentacaoId(distribuicao.getId());

        for (MovimentacaoItem movimentacaoItem : itens) {
            distribuirPatrimonioHelper.distribuirPatrimonio(movimentacaoItem, distribuicao, contaAlmoxarifado);
        }

        atualizarDistribuicaoComInformacoesFinalizacao(distribuicao);
        return salvarDistribuicao(distribuicao);
    }

    private void atualizarDistribuicaoComInformacoesFinalizacao(Movimentacao distribuicao) {
        distribuicao.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        distribuicao.setDataFinalizacao(LocalDateTime.now(clock));
    }
}
