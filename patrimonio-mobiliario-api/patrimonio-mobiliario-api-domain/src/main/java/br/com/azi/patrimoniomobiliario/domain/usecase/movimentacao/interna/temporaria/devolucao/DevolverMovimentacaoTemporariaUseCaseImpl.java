package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao;

import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.converter.DevolverMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.MovimentacaoItemNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.MovimentacaoNaoEstaAguardandoDevolucaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.NenhumPatrimonioSelecionadoParaDevolucaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.PatrimonioJaDevolvidoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DevolverMovimentacaoTemporariaUseCaseImpl implements DevolverMovimentacaoTemporariaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final Clock clock;

    private final DevolverMovimentacaoTemporariaOutputDataConverter converter;

    @Override
    public DevolverMovimentacaoTemporariaOutputData executar(DevolverMovimentacaoTemporariaInputData inputData) {
        validarDadosEntrada(inputData);
        validarSeListaPatrimoniosVazia(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData.getMovimentacaoId());
        validarSeMovimentacaoAguardandoDevolucao(movimentacao);

        List<Patrimonio> patrimonios;
        if (inputData.isDevolverTodos()) {
            patrimonios = buscarPatrimoniosAguardandoDevolucao(movimentacao);
        } else {
            patrimonios = buscarPatrimonios(inputData);
            validarSePatrimoniosAguardandoDevolucao(patrimonios, movimentacao);
        }

        realizarMovimentacaoPatrimonios(patrimonios, movimentacao);
        atualizarMovimentacaoItemComDataDevolucao(movimentacao, patrimonios);
        atualizarMovimentacaoComInformacoesDeDevolucao(movimentacao);

        return converter.to(salvarMovimentacao(movimentacao));
    }

    private void validarDadosEntrada(DevolverMovimentacaoTemporariaInputData inputData) {
        Validator.of(inputData)
            .validate(DevolverMovimentacaoTemporariaInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo.")
            .validate(
                DevolverMovimentacaoTemporariaInputData::getPatrimoniosId,
                patrimonios -> inputData.isDevolverTodos() || Objects.nonNull(patrimonios),
                "Lista de patrimônios é nula."
            )
            .get();
    }

    private void validarSeListaPatrimoniosVazia(DevolverMovimentacaoTemporariaInputData inputData) {
        if (!inputData.isDevolverTodos() && inputData.getPatrimoniosId().isEmpty()) {
            throw new NenhumPatrimonioSelecionadoParaDevolucaoException();
        }
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(movimentacaoId);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoAguardandoDevolucao(Movimentacao movimentacao) {
        if (Movimentacao.Situacao.DEVOLVIDO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaAguardandoDevolucaoException();
        }
    }

    private List<Patrimonio> buscarPatrimoniosAguardandoDevolucao(Movimentacao movimentacao) {
        List<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarItensAguardandoDevolucao(movimentacao.getId());
        return itens.stream()
            .map(MovimentacaoItem::getPatrimonio)
            .collect(Collectors.toList());
    }

    private boolean validarSeExistembuscarItensAguardandoDevolucao(Movimentacao movimentacao) {
        return movimentacaoItemDataProvider.existeItemAguardandoDevolucaoPorMovimentacao(movimentacao.getId());
    }

    private void realizarMovimentacaoPatrimonios(List<Patrimonio> patrimonios, Movimentacao movimentacao) {
        for (Patrimonio patrimonio : patrimonios) {
            patrimonio.setSetor(movimentacao.getSetorOrigem());
            gerarLancamentoContabil(patrimonio, movimentacao);
        }
        salvarPatrimonios(patrimonios);
    }

    private void gerarLancamentoContabil(Patrimonio patrimonio, Movimentacao movimentacao){
        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoDestino(), movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.TEMPORARIA_VOLTA, LancamentoContabil.TipoLancamento.DEBITO);

        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoOrigem(), movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.TEMPORARIA_VOLTA, LancamentoContabil.TipoLancamento.CREDITO);
    }

    private void salvarPatrimonios(List<Patrimonio> patrimonios) {
        patrimonioDataProvider.atualizarTodos(patrimonios);
    }

    private void atualizarMovimentacaoComInformacoesDeDevolucao(Movimentacao movimentacao) {
        if (validarSeExistembuscarItensAguardandoDevolucao(movimentacao)) {
            movimentacao.setSituacao(Movimentacao.Situacao.DEVOLVIDO_PARCIAL);
        }else {
            movimentacao.setSituacao(Movimentacao.Situacao.DEVOLVIDO);
        }
        movimentacao.setDataFinalizacao(LocalDateTime.now(clock));
    }

    private Movimentacao salvarMovimentacao(Movimentacao movimentacao) {
        return movimentacaoDataProvider.salvar(movimentacao);
    }


    private void validarSePatrimoniosAguardandoDevolucao(List<Patrimonio> patrimonios, Movimentacao movimentacao) {
        for (Patrimonio patrimonio: patrimonios) {
            if (!movimentacaoItemDataProvider.aguardandoDevolucao(movimentacao.getId(), patrimonio.getId())) {
                throw new PatrimonioJaDevolvidoException();
            }
        }
    }

    private List<Patrimonio> buscarPatrimonios(DevolverMovimentacaoTemporariaInputData inputData) {
        return patrimonioDataProvider.buscarTodosPatrimonios(inputData.getPatrimoniosId());
    }

    private void atualizarMovimentacaoItemComDataDevolucao(Movimentacao movimentacao, List<Patrimonio> patrimonios) {
        for (Patrimonio patrimonio : patrimonios) {
            MovimentacaoItem movimentacaoItem = buscarMovimentacaoitem(movimentacao, patrimonio);
            movimentacaoItem.setDataDevolucao(LocalDateTime.now(clock));
            salvarMovimentacaoItem(movimentacaoItem);
        }
    }

    private MovimentacaoItem buscarMovimentacaoitem(Movimentacao movimentacao, Patrimonio patrimonio) {
        Optional<MovimentacaoItem> movimentacaoItem = movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(movimentacao.getId(), patrimonio.getId());
        return movimentacaoItem.orElseThrow(MovimentacaoItemNaoEncontradoException::new);
    }

    private void salvarMovimentacaoItem(MovimentacaoItem movimentacaoItem) {
        movimentacaoItemDataProvider.salvar(movimentacaoItem);
    }
}
