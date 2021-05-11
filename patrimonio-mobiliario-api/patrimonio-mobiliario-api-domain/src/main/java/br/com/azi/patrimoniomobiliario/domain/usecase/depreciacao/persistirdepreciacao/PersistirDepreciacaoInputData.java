package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PersistirDepreciacaoInputData {
    private final List<PersistirDepreciacaoInputData.Item> itens;

    @Getter
    @AllArgsConstructor
    public static class Item {
        private final Patrimonio patrimonio;
        private final List<Depreciacao> depreciacoes;
    }
}
