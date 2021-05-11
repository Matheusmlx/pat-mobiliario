package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.OrgaoOrigemNaoSelecionadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.SetorOrigemNaoSelecionadoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarPatrimoniosParaSelecaoUseCaseImpl implements BuscarPatrimoniosParaSelecaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final BuscarPatrimoniosParaSelecaoFiltroConverter filtroConverter;

    private final BuscarPatrimoniosParaSelecaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarPatrimoniosParaSelecaoOutputData executar(BuscarPatrimoniosParaSelecaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao movimentacao = buscarMovimentacao(inputData);

        validarOrgaoSetorOrigem(movimentacao);

        Patrimonio.Filtro filtro = criarFiltro(inputData, movimentacao);

        ListaPaginada<Patrimonio> patrimonios = buscarPatrimonios(filtro);

        return outputDataConverter.to(patrimonios);
    }

    private void validarDadosEntrada(BuscarPatrimoniosParaSelecaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosParaSelecaoInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo")
            .validate(BuscarPatrimoniosParaSelecaoInputData::getPage, Objects::nonNull, "Ausência do número da página")
            .validate(BuscarPatrimoniosParaSelecaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Ausência da quantidade de itens por página")
            .get();
    }

    private Movimentacao buscarMovimentacao(BuscarPatrimoniosParaSelecaoInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarOrgaoSetorOrigem(Movimentacao movimentacao) {
        if (Objects.isNull(movimentacao.getOrgaoOrigem())) {
            throw new OrgaoOrigemNaoSelecionadoException();
        }

        if (Objects.isNull(movimentacao.getSetorOrigem())) {
            throw new SetorOrigemNaoSelecionadoException();
        }
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosParaSelecaoInputData inputData, Movimentacao movimentacao) {
        UnidadeOrganizacional orgaoOrigem = movimentacao.getOrgaoOrigem();
        UnidadeOrganizacional setorOrigem = movimentacao.getSetorOrigem();

        Patrimonio.Filtro filtro = filtroConverter.to(inputData);
        filtro.setOrgao(orgaoOrigem.getId());
        filtro.setSetor(setorOrigem.getId());

        return filtro;
    }

    private ListaPaginada<Patrimonio> buscarPatrimonios(Patrimonio.Filtro filtro) {
        return patrimonioDataProvider.buscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(filtro);
    }
}
