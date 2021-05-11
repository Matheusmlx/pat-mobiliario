package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.persistirdepreciacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PersistirDepreciacaoUseCaseTest {

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private DepreciacaoDataProvider depreciacaoDataProvider;

    private PersistirDepreciacaoUseCase useCase;

    @Before
    public void inicializarUseCase() {
        useCase = new PersistirDepreciacaoUseCaseImpl(patrimonioDataProvider, depreciacaoDataProvider);
    }

    @Test
    public void devePersistirQuandoHouverDepreciacao() {
        Patrimonio patrimonio = Patrimonio.builder().id(1L).build();

        List<Depreciacao> depreciacoes = List.of(
            Depreciacao.builder()
                .patrimonio(patrimonio)
                .mesReferencia("01/2021")
                .build()
        );

        PersistirDepreciacaoInputData inputData = PersistirDepreciacaoInputData.builder()
            .itens(List.of(
                new PersistirDepreciacaoInputData.Item(patrimonio, depreciacoes)
            ))
            .build();

        useCase.executar(inputData);

        verify(patrimonioDataProvider, times(1)).atualizar(patrimonio);
        verify(depreciacaoDataProvider, times(1)).salvar(depreciacoes);
    }

    @Test
    public void naoDevePersistirQuandoNaoHouverDepreciacao() {
        Patrimonio patrimonio = Patrimonio.builder().id(1L).build();

        PersistirDepreciacaoInputData inputData = PersistirDepreciacaoInputData.builder()
            .itens(List.of(
                new PersistirDepreciacaoInputData.Item(patrimonio, Collections.emptyList())
            ))
            .build();

        useCase.executar(inputData);

        verifyZeroInteractions(patrimonioDataProvider);
        verifyZeroInteractions(depreciacaoDataProvider);
    }
}
