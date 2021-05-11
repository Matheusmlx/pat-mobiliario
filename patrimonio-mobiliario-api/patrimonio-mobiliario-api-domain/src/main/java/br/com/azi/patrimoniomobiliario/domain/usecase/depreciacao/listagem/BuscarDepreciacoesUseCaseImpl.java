package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem.converter.BuscarDepreciacoesOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
@AllArgsConstructor
public class BuscarDepreciacoesUseCaseImpl implements BuscarDepreciacoesUseCase {

    private DepreciacaoDataProvider depreciacaoDataProvider;

    private BuscarDepreciacoesOutputDataConverter converter;

    @Override
    public BuscarDepreciacoesOutputData executar(BuscarDepreciacoesInputData inputData) {
        validarDadosEntrada(inputData);
        List<Depreciacao> depreciacaoList = buscarDepreciacoes(inputData);

        return converter.to(depreciacaoList);
    }

    private void validarDadosEntrada(BuscarDepreciacoesInputData inputData) {
        Validator.of(inputData).validate(BuscarDepreciacoesInputData::getId, Objects::nonNull, "O id é nulo").get();
    }

    private List<Depreciacao> buscarDepreciacoes(BuscarDepreciacoesInputData inputData) {
        return depreciacaoDataProvider.buscarDepreciacoesPorPatrimonioId(inputData.getId());
    }
}
