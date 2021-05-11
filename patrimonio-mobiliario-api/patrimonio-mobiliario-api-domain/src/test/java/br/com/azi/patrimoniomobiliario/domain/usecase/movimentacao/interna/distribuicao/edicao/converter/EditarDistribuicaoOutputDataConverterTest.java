package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.EditarDistribuicaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EditarDistribuicaoOutputDataConverterTest {

    private static final LocalDateTime dataNotaContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);

    @Test
    public void deveConverterMovimentacaoEmEditarDistribuicaoOutputData() {
        final EditarDistribuicaoOutputDataConverter converter = new EditarDistribuicaoOutputDataConverter();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(1L)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder()
                .id(1L)
                .sigla("OO")
                .nome("Órgão origem")
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorOrigem(UnidadeOrganizacional.builder()
                .id(2L)
                .sigla("SO")
                .nome("Setor origem")
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .setorDestino(UnidadeOrganizacional.builder()
                .id(3L)
                .sigla("SD")
                .nome("Setor destino")
                .situacao(UnidadeOrganizacional.Situacao.ATIVO)
                .build())
            .motivoObservacao("Motivo")
            .numeroProcesso("12345")
            .notaLancamentoContabil(NotaLancamentoContabil.builder()
                .id(1L)
                .numero("2020NL123456")
                .dataLancamento(dataNotaContabil)
                .build())
            .build();

        final EditarDistribuicaoOutputData outputDataEsperado = EditarDistribuicaoOutputData.builder()
            .id(distribuicao.getId())
            .situacao(EditarDistribuicaoOutputData.Situacao.EM_ELABORACAO)
            .orgao(1L)
            .setorOrigem(2L)
            .setorDestino(3L)
            .motivoObservacao(distribuicao.getMotivoObservacao())
            .numeroProcesso(distribuicao.getNumeroProcesso())
            .numeroNotaLancamentoContabil("2020NL123456")
            .dataNotaLancamentoContabil(DateValidate.formatarData(dataNotaContabil))
            .build();

        final EditarDistribuicaoOutputData outputData = converter.to(distribuicao);

        assertEquals(outputData, outputDataEsperado);
    }
}
