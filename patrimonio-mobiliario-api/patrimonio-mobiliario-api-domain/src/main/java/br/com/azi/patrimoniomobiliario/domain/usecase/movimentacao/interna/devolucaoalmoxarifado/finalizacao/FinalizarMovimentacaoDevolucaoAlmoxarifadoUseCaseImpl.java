package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao;

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
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.converter.FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEhDevolucaoAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception.SetorNaoAlmoxarifadoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCaseImpl implements FinalizarMovimentacaoDevolucaoAlmoxarifadoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final boolean patrimonioParaContaAlmoxarifado;

    private final String codigoContaContabilAlmoxarifado;

    private final Clock clock;

    private final FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputDataConverter outputDataConverter;

    @Override
    public FinalizarMovimentacaoDevolucaoAlmoxarifadoOutputData executar(FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarMovimentacaoEmElaboracao(movimentacao);
        validarMovimentacaoDoTipoDevolucaoAlmoxarifado(movimentacao);

        validarOrgaoOrigemAtivo(movimentacao);
        validarSetorOrigemAtivo(movimentacao);
        validarSetorDestinoAtivo(movimentacao);

        validarSetorOrigemNaoAlmoxarifado(movimentacao);
        validarSetorDestinoAlmoxarifado(movimentacao);

        atualizarDistribuicaoComInformacoesDeFinalizacao(movimentacao);
        Movimentacao movimentacaoFinalizada = salvarDistribuicao(movimentacao);

        List<Patrimonio> patrimonios = buscarPatrimoniosMovimentacao(movimentacaoFinalizada);
        realizarMovimentacaoPatrimonios(patrimonios, movimentacaoFinalizada);

        return outputDataConverter.to(movimentacaoFinalizada);
    }

    private void validarDadosEntrada(FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData::getId, Objects::nonNull, "Id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(FinalizarMovimentacaoDevolucaoAlmoxarifadoInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarMovimentacaoEmElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void validarMovimentacaoDoTipoDevolucaoAlmoxarifado(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO.equals(movimentacao.getTipo())) {
            throw new MovimentacaoNaoEhDevolucaoAlmoxarifadoException();
        }
    }

    private void validarOrgaoOrigemAtivo(Movimentacao movimentacao) {
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

    private void validarSetorOrigemNaoAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.TRUE.equals(movimentacao.getSetorOrigem().getAlmoxarifado())) {
            throw new SetorAlmoxarifadoException();
        }
    }

    private void validarSetorDestinoAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.FALSE.equals(movimentacao.getSetorDestino().getAlmoxarifado())) {
            throw new SetorNaoAlmoxarifadoException();
        }
    }

    private void atualizarDistribuicaoComInformacoesDeFinalizacao(Movimentacao movimentacao) {
        movimentacao.setSituacao(Movimentacao.Situacao.FINALIZADO);
        movimentacao.setUsuarioFinalizacao(sessaoUsuarioDataProvider.getLogin());
        movimentacao.setDataFinalizacao(LocalDateTime.now(clock));
    }

    private Movimentacao salvarDistribuicao(Movimentacao movimentacao) {
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
            gerarLancamentoContabil(patrimonio, movimentacao);
        }
        salvarPatrimonios(patrimonios);
    }

    private void atualizarSetorPatrimonio(Patrimonio patrimonio, UnidadeOrganizacional setorDestino) {
        patrimonio.setSetor(setorDestino);
    }

    private void gerarLancamentoContabil (Patrimonio patrimonio, Movimentacao movimentacao){
        ContaContabil contaContabilCredito = definirContaContabilCredito(patrimonio);

        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoOrigem(), movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO, LancamentoContabil.TipoLancamento.DEBITO);

        lancamentoContabilDataProvider.registrarLancamentoContabil(contaContabilCredito,
            patrimonio, movimentacao,  movimentacao.getOrgaoDestino(), movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.DEVOLUCAO_ALMOXARIFADO, LancamentoContabil.TipoLancamento.CREDITO);
    }

    private ContaContabil definirContaContabilCredito(Patrimonio patrimonio) {
        if (patrimonioParaContaAlmoxarifado) {
            return contaContabilDataProvider.buscarPorCodigo(codigoContaContabilAlmoxarifado)
                .orElseThrow(ContaContabilNaoEncontradaException::new);
        }

        return ContaContabil.builder()
            .id(patrimonio.getContaContabilClassificacao().getId())
            .build();
    }

    private void salvarPatrimonios(List<Patrimonio> patrimonios) {
        patrimonioDataProvider.atualizarTodos(patrimonios);
    }
}
