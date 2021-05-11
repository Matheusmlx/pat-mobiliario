package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoOutputData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarItemMovimentacaoOutputDataConverterTest {

    @Test
    public void deveConverterAListaDeItensDeMovimentacao() {
        final Movimentacao movimentacao = Movimentacao.builder().id(1L).build();

        final Patrimonio patrimonio1 = Patrimonio.builder().id(1L).build();
        final Patrimonio patrimonio2 = Patrimonio.builder().id(2L).build();
        final Patrimonio patrimonio3 = Patrimonio.builder().id(3L).build();

        final List<MovimentacaoItem> itensMovimentacao = Arrays.asList(
            MovimentacaoItem.builder().movimentacao(movimentacao).patrimonio(patrimonio1).build(),
            MovimentacaoItem.builder().movimentacao(movimentacao).patrimonio(patrimonio2).build(),
            MovimentacaoItem.builder().movimentacao(movimentacao).patrimonio(patrimonio3).build()
        );

        final CadastrarItemMovimentacaoOutputDataConverter converter = new CadastrarItemMovimentacaoOutputDataConverter();
        final CadastrarItemMovimentacaoOutputData outputData = converter.to(itensMovimentacao);

        assertEquals(outputData.getItens().size(), 3);

        assertEquals(outputData.getItens().get(0).getMovimentacaoId(), Long.valueOf(1));
        assertEquals(outputData.getItens().get(0).getPatrimonioId(), Long.valueOf(1));

        assertEquals(outputData.getItens().get(1).getMovimentacaoId(), Long.valueOf(1));
        assertEquals(outputData.getItens().get(1).getPatrimonioId(), Long.valueOf(2));

        assertEquals(outputData.getItens().get(2).getMovimentacaoId(), Long.valueOf(1));
        assertEquals(outputData.getItens().get(2).getPatrimonioId(), Long.valueOf(3));
    }
}
