package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter.BuscarMovimentacoesInternasOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarMovimentacoesInternasUseCaseImpl implements BuscarMovimentacoesInternasUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final BuscarMovimentacoesInternasFiltroConverter filtroConverter;

    private final BuscarMovimentacoesInternasOutputDataConverter outputDataConverter;

    @Override
    public BuscarMovimentacoesInternasOutputData executar(BuscarMovimentacoesInternasInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao.Filtro filtro = criarFiltro(inputData);

        ListaPaginada<Movimentacao> movimentacoes = buscar(filtro);

        return outputDataConverter.to(movimentacoes);
    }

    private void validarDadosEntrada(BuscarMovimentacoesInternasInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarMovimentacoesInternasInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Ausência da quantidade de registros por página")
            .validate(BuscarMovimentacoesInternasInputData::getPage, page -> Objects.nonNull(page) && page > 0, "Ausência da página atual")
            .get();
    }

    private Movimentacao.Filtro criarFiltro(BuscarMovimentacoesInternasInputData inputData) {
        Movimentacao.Filtro filtro = filtroConverter.to(inputData);
        filtro.setUsuarioId(sessaoUsuarioDataProvider.get().getId());
        filtro.setFuncoes(List.of(
            PermissaoMobiliarioConstants.NORMAL.getDescription(),
            PermissaoMobiliarioConstants.CONSULTA.getDescription()
        ));
        return filtro;
    }

    private ListaPaginada<Movimentacao> buscar(Movimentacao.Filtro filtro) {
        return movimentacaoDataProvider.buscarMovimentacaoInternaPorFiltro(filtro);
    }
}
