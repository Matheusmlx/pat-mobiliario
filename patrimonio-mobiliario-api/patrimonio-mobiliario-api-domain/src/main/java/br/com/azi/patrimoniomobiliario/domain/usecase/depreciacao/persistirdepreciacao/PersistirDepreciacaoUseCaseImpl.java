package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersistirDepreciacaoUseCaseImpl implements PersistirDepreciacaoUseCase {

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final DepreciacaoDataProvider depreciacaoDataProvider;

    @Override
    public void executar(PersistirDepreciacaoInputData inputData) {
        for (PersistirDepreciacaoInputData.Item itemDepreciacao : inputData.getItens()) {
            if (gerouDepreciacao(itemDepreciacao)) {
                patrimonioDataProvider.atualizar(itemDepreciacao.getPatrimonio());
                depreciacaoDataProvider.salvar(itemDepreciacao.getDepreciacoes());
            }
        }
    }

    private boolean gerouDepreciacao(PersistirDepreciacaoInputData.Item itemDepreciacao) {
        return !itemDepreciacao.getDepreciacoes().isEmpty();
    }

}
