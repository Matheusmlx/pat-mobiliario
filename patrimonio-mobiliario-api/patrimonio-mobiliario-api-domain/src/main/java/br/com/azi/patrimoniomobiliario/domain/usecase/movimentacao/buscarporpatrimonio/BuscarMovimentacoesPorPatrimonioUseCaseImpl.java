package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.converter.BuscarMovimentacoesPorPatrimonioOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarMovimentacoesPorPatrimonioUseCaseImpl implements BuscarMovimentacoesPorPatrimonioUseCase {

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final BuscarMovimentacoesPorPatrimonioOutputDataConverter outputDataConverter;

    @Override
    public BuscarMovimentacoesPorPatrimonioOutputData executar(BuscarMovimentacoesPorPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao.Filtro filtro = criarFiltro(inputData);
        List<MovimentacaoItem> movimentacoes = buscar(filtro);
        return outputDataConverter.to(movimentacoes);
    }

    private void validarDadosEntrada(BuscarMovimentacoesPorPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarMovimentacoesPorPatrimonioInputData::getPatrimonioId, Objects::nonNull, "O id do patrimônio é nulo")
            .get();
    }

    private Movimentacao.Filtro criarFiltro(BuscarMovimentacoesPorPatrimonioInputData inputData) {
        return Movimentacao.Filtro
            .builder()
            .patrimonioId(inputData.getPatrimonioId())
            .build();
    }

    private List<MovimentacaoItem> buscar(Movimentacao.Filtro filtro) {
        return movimentacaoItemDataProvider.buscarPorPatrimonio(filtro.getPatrimonioId());
    }

}
