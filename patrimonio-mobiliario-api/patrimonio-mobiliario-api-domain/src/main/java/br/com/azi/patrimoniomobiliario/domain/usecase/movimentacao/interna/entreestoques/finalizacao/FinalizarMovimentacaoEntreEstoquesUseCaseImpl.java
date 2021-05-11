package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
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
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.converter.FinalizarMovimentacaoEntreEstoquesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEhEntreEstoquesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.SetorNaoAlmoxarifadoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FinalizarMovimentacaoEntreEstoquesUseCaseImpl implements FinalizarMovimentacaoEntreEstoquesUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final FinalizarMovimentacaoEntreEstoquesOutputDataConverter outputDataConverter;

    private final Clock clock;

    @Override
    public FinalizarMovimentacaoEntreEstoquesOutputData executar(FinalizarMovimentacaoEntreEstoquesInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeMovimentacaoEmElaboracao(movimentacao);
        validarSeMovimentacaoDoTipoEntreEstoques(movimentacao);

        validarSeOrgaoOrigemAtivo(movimentacao);
        validarSetorOrigemAtivo(movimentacao);
        validarSetorDestinoAtivo(movimentacao);

        validarSeSetorOrigemAlmoxarifado(movimentacao);
        validarSetorDestinoAlmoxarifado(movimentacao);
        atualizarMovimentacaoComInformacoesDeFinalizacao(movimentacao);

        Movimentacao movimentacaoFinalizada = salvarMovimentacao(movimentacao);
        List<Patrimonio> patrimonios = buscarPatrimoniosMovimentacao(movimentacaoFinalizada);
        realizarMovimentacaoPatrimonios(patrimonios, movimentacaoFinalizada);

        return outputDataConverter.to(movimentacaoFinalizada);
    }

    private void validarDadosEntrada(FinalizarMovimentacaoEntreEstoquesInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarMovimentacaoEntreEstoquesInputData::getId, Objects::nonNull, "id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(FinalizarMovimentacaoEntreEstoquesInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoEmElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarSeMovimentacaoDoTipoEntreEstoques(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.ENTRE_ESTOQUES.equals(movimentacao.getTipo())) {
            throw new MovimentacaoNaoEhEntreEstoquesException();
        }
    }

    private void validarSeOrgaoOrigemAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getOrgaoOrigem().getSituacao())) {
            throw new OrgaoInativoException();
        }
    }

    private void validarSetorOrigemAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getSetorOrigem().getSituacao())) {
            throw new SetorInativoException("Setor origem selecionado está inativo, por favor, selecione outro antes de finalizar.");
        }
    }

    private void validarSetorDestinoAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getSetorDestino().getSituacao())) {
            throw new SetorInativoException("Setor destino selecionado está inativo, por favor, selecione outro antes de finalizar.");
        }
    }

    private void validarSeSetorOrigemAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.FALSE.equals(movimentacao.getSetorOrigem().getAlmoxarifado())) {
            throw new SetorNaoAlmoxarifadoException("Setor origem não é almoxarifado!");
        }
    }

    private void validarSetorDestinoAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.FALSE.equals(movimentacao.getSetorDestino().getAlmoxarifado())) {
            throw new SetorNaoAlmoxarifadoException("Setor destino não é almoxarifado!");
        }
    }

    private void atualizarMovimentacaoComInformacoesDeFinalizacao(Movimentacao movimentacao) {
        movimentacao.setSituacao(Movimentacao.Situacao.FINALIZADO);
        movimentacao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        movimentacao.setDataFinalizacao(LocalDateTime.now(clock));
    }

    private Movimentacao salvarMovimentacao(Movimentacao movimentacao) {
        return movimentacaoDataProvider.salvar(movimentacao);
    }

    private List<Patrimonio> buscarPatrimoniosMovimentacao(Movimentacao movimentacao) {
        List<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarPorMovimentacaoId(movimentacao.getId());
        List<Long> patrimoniosId = itens.stream()
            .map(item -> item.getPatrimonio().getId())
            .collect(Collectors.toList());
        return patrimonioDataProvider.buscarTodosPatrimonios(patrimoniosId);
    }

    private void realizarMovimentacaoPatrimonios(List<Patrimonio> patrimonios, Movimentacao movimentacao) {
        for (Patrimonio patrimonio : patrimonios) {
            atualizarSetorPatrimonio(patrimonio, movimentacao.getSetorDestino());
            gerarLancamentoContabil (patrimonio, movimentacao);
        }
        salvarPatrimonios(patrimonios);
    }

    private void gerarLancamentoContabil (Patrimonio patrimonio, Movimentacao movimentacao){
        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoOrigem(), movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.ENTRE_ESTOQUES, LancamentoContabil.TipoLancamento.DEBITO);

        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoDestino(), movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.ENTRE_ESTOQUES, LancamentoContabil.TipoLancamento.CREDITO);
    }

    private void atualizarSetorPatrimonio(Patrimonio patrimonio, UnidadeOrganizacional setor) {
        patrimonio.setSetor(setor);
    }

    private void salvarPatrimonios(List<Patrimonio> patrimonios) {
        patrimonioDataProvider.atualizarTodos(patrimonios);
    }

}
