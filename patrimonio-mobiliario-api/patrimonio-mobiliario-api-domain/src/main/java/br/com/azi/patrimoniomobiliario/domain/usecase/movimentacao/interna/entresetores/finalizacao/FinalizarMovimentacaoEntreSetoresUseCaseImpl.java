package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao;

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
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.converter.FinalizarMovimentacaoEntreSetoresOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.OrgaoInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SetorInativoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SetorTipoAlmoxarifadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.SituacaoMovimentacaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception.TipoDeMovimentacaoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FinalizarMovimentacaoEntreSetoresUseCaseImpl implements FinalizarMovimentacaoEntreSetoresUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final Clock clock;

    private final FinalizarMovimentacaoEntreSetoresOutputDataConverter converter;

    @Override
    public FinalizarMovimentacaoEntreSetoresOutputData executar(FinalizarMovimentacaoEntreSetoresInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeMovimentacaoEmElaboracao(movimentacao);
        validarSeMovimentacaoDoTipoDefinitiva(movimentacao);

        validarSeOrgaoOrigemAtivo(movimentacao);
        validarSetorOrigemAtivo(movimentacao);
        validarSetorDestinoAtivo(movimentacao);

        validarSeSetorOrigemDiferenteDeAlmoxarifado(movimentacao);
        validarSetorDestinoDiferenteDeAlmoxarifado(movimentacao);

        atualizarMovimentacaoComInformacoesDeFinalizacao(movimentacao);
        Movimentacao movimentacaoFinalizada = salvarMovimentacao(movimentacao);

        List<Patrimonio> patrimonios = buscarPatrimoniosMovimentacao(movimentacaoFinalizada);
        realizarMovimentacaoPatrimonios(patrimonios, movimentacaoFinalizada);

        return converter.to(movimentacaoFinalizada);
    }

    private void validarDadosEntrada(FinalizarMovimentacaoEntreSetoresInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarMovimentacaoEntreSetoresInputData::getId, Objects::nonNull, "O id da movimentacão é nulo.")
            .get();
    }

    private Movimentacao buscarMovimentacao(FinalizarMovimentacaoEntreSetoresInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoEmElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao())) {
            throw new SituacaoMovimentacaoException();
        }
    }

    private void validarSeMovimentacaoDoTipoDefinitiva(Movimentacao movimentacao) {
        if (!TipoMovimentacaoEnum.ENTRE_SETORES.equals(movimentacao.getTipo())) {
            throw new TipoDeMovimentacaoException();
        }
    }

    private void validarSeOrgaoOrigemAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getOrgaoOrigem().getSituacao())) {
            throw new OrgaoInativoException();
        }
    }

    private void validarSetorOrigemAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getSetorOrigem().getSituacao())) {
            throw new SetorInativoException("Setor origem selecionado está inativo. Por favor, selecione outro setor para finalizar.");
        }
    }

    private void validarSetorDestinoAtivo(Movimentacao movimentacao) {
        if (UnidadeOrganizacional.Situacao.INATIVO.equals(movimentacao.getSetorDestino().getSituacao())) {
            throw new SetorInativoException("Setor destino selecionado está inativo. Por favor, selecione outro setor para finalizar.");
        }
    }

    private void validarSeSetorOrigemDiferenteDeAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.TRUE.equals(movimentacao.getSetorOrigem().getAlmoxarifado())) {
            throw new SetorTipoAlmoxarifadoException("Setor origem é tipo almoxarifado!");
        }
    }

    private void validarSetorDestinoDiferenteDeAlmoxarifado(Movimentacao movimentacao) {
        if (Boolean.TRUE.equals(movimentacao.getSetorDestino().getAlmoxarifado())) {
            throw new SetorTipoAlmoxarifadoException("Setor destino é tipo almoxarifado!");
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
            gerarLancamentoContabil(patrimonio, movimentacao);
        }
        salvarPatrimonios(patrimonios);
    }

    private void atualizarSetorPatrimonio(Patrimonio patrimonio, UnidadeOrganizacional setor) {
        patrimonio.setSetor(setor);
    }

    private void gerarLancamentoContabil (Patrimonio patrimonio, Movimentacao movimentacao){
        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoOrigem(), movimentacao.getSetorOrigem(),
            LancamentoContabil.TipoMovimentacao.ENTRE_SETORES, LancamentoContabil.TipoLancamento.DEBITO);

        lancamentoContabilDataProvider.registrarLancamentoContabil(patrimonio.getContaContabilAtual(),
            patrimonio, movimentacao,  movimentacao.getOrgaoDestino(), movimentacao.getSetorDestino(),
            LancamentoContabil.TipoMovimentacao.ENTRE_SETORES, LancamentoContabil.TipoLancamento.CREDITO);
    }

    private void salvarPatrimonios(List<Patrimonio> patrimonios) {
        patrimonioDataProvider.atualizarTodos(patrimonios);
    }
}
