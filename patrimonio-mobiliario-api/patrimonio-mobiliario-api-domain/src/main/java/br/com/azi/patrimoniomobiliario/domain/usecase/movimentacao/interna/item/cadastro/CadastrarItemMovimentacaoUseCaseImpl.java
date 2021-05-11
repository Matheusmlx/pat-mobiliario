package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.converter.CadastrarItemMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.NenhumPatrimonioSelecionadoParaMovimentacaoException;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CadastrarItemMovimentacaoUseCaseImpl implements CadastrarItemMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final CadastrarItemMovimentacaoOutputDataConverter converter;

    @Override
    public CadastrarItemMovimentacaoOutputData executar(CadastrarItemMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarListaPatrimoniosVazia(inputData);

        final Movimentacao movimentacao = buscarMovimentacao(inputData.getMovimentacaoId());
        validarMovimentacaoEmModoElaboracao(movimentacao);

        final List<Patrimonio> patrimonios = buscarPatrimonios(inputData, movimentacao);
        final List<MovimentacaoItem> itens = criarItensMovimentacao(movimentacao, patrimonios);
        return converter.to(salvarItensMovimentacao(itens));
    }

    private void validarDadosEntrada(CadastrarItemMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarItemMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo.")
            .validate(
                CadastrarItemMovimentacaoInputData::getPatrimoniosId,
                patrimonios -> inputData.isTodosSelecionados() || Objects.nonNull(patrimonios),
                "Lista de patrimônios é nula."
            )
            .get();
    }

    private void validarListaPatrimoniosVazia(CadastrarItemMovimentacaoInputData inputData) {
        if (!inputData.isTodosSelecionados() && inputData.getPatrimoniosId().isEmpty()) {
            throw new NenhumPatrimonioSelecionadoParaMovimentacaoException();
        }
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        return movimentacaoDataProvider.buscarPorId(movimentacaoId)
            .orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarMovimentacaoEmModoElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private List<Patrimonio> buscarPatrimonios(CadastrarItemMovimentacaoInputData inputData, Movimentacao movimentacao) {
        if (inputData.isTodosSelecionados()) {
            return patrimonioDataProvider.buscarPatrimoniosAtivosPorOrgaoSetorQueNaoEstaoEmOutraMovimentacao(
                Optional.ofNullable(inputData.getPatrimoniosNaoConsiderar()).orElse(Collections.emptyList()),
                movimentacao.getOrgaoOrigem().getId(),
                movimentacao.getSetorOrigem().getId());
        }

        return patrimonioDataProvider.buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(
            inputData.getPatrimoniosId(), movimentacao.getOrgaoOrigem().getId(), movimentacao.getSetorOrigem().getId());
    }

    private List<MovimentacaoItem> criarItensMovimentacao(Movimentacao movimentacao, List<Patrimonio> patrimonios) {
        return patrimonios
            .stream()
            .map(patrimonio ->
                MovimentacaoItem.builder()
                    .movimentacao(movimentacao)
                    .patrimonio(patrimonio)
                    .build())
            .collect(Collectors.toList());
    }

    private List<MovimentacaoItem> salvarItensMovimentacao(List<MovimentacaoItem> itens) {
        return movimentacaoItemDataProvider.salvar(itens);
    }
}
