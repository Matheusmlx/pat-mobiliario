package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remover;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.RemoverIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.RemoverIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception.IncorporacaoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception.IncorporacaoNaoPodeSerExcluidoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RemoverIncorporacaoUseCaseTest {

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Test(expected = IncorporacaoNaoEncontradoException.class)
    public void deveFalharQuandoIncorporacaoNaoExistir() {

        RemoverIncorporacaoInputData inputData = new RemoverIncorporacaoInputData(1L);
        RemoverIncorporacaoUseCaseImpl usecase = new RemoverIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider
        );

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test
    public void deveRemoverIncorporacao() {

        RemoverIncorporacaoInputData inputData = new RemoverIncorporacaoInputData(1L);
        RemoverIncorporacaoUseCaseImpl usecase = new RemoverIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider
        );

        LocalDateTime dataHoraRecebimento = LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(18);
        LocalDateTime dataNota = LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(18);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .dataRecebimento(dataHoraRecebimento)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .numProcesso("31415")
                .justificativa("criando incorporacao")
                .fornecedor(
                    Fornecedor.builder()
                        .id(1L)
                        .cpfCnpj("12345678912345")
                        .nome("TESTE")
                        .build()
                )
                .nota("7")
                .valorNota(BigDecimal.valueOf(7))
                .dataNota(dataNota)
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(50L)
                    .build())
                .convenio(Convenio.builder().id(10L).build())
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder().id(27L).build())
                .empenho(Empenho.builder().id(2L).build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).build())
                .notaLancamentoContabil(
                    NotaLancamentoContabil
                        .builder()
                        .id(2L)
                        .numero("1234NL123456")
                        .dataAlteracao(LocalDateTime.now())
                        .build())
                .build()));


        usecase.executar(inputData);

        Mockito.verify(incorporacaoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(incorporacaoDataProvider, Mockito.times(1)).remover(any(Long.class));
        Mockito.verify(notaLancamentoContabilDataProvider, Mockito.times(1)).remover(2L);
    }

    @Test(expected = IncorporacaoNaoPodeSerExcluidoException.class)
    public void deveFalharQuandoIncorporacaoEstiverFinalizado() {
        RemoverIncorporacaoInputData inputData = new RemoverIncorporacaoInputData(1L);
        RemoverIncorporacaoUseCaseImpl usecase = new RemoverIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider
        );

        LocalDateTime dataHoraRecebimento = LocalDateTime.now().withMonth(Month.JULY.getValue()).withDayOfMonth(18);
        LocalDateTime dataNota = LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(18);

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(1L)
                .codigo("1554asdf")
                .dataRecebimento(dataHoraRecebimento)
                .situacao(Incorporacao.Situacao.FINALIZADO)
                .numProcesso("31415")
                .justificativa("criando incorporacao")
                .fornecedor(
                    Fornecedor.builder()
                        .id(1L)
                        .cpfCnpj("12345678912345")
                        .nome("TESTE")
                        .build()
                )
                .nota("7")
                .valorNota(BigDecimal.valueOf(7))
                .dataNota(dataNota)
                .orgao(UnidadeOrganizacional
                    .builder()
                    .id(50L)
                    .build())
                .convenio(Convenio.builder().id(10L).build())
                .usuario(Usuario.builder().id(5L).build())
                .reconhecimento(Reconhecimento.builder().id(27L).build())
                .empenho(Empenho.builder().id(2L).build())
                .naturezaDespesa(NaturezaDespesa.builder().id(7L).build())
                .build()));


        usecase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoBuscaSemId() {

        RemoverIncorporacaoInputData inputData = new RemoverIncorporacaoInputData(null);
        RemoverIncorporacaoUseCaseImpl usecase = new RemoverIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            notaLancamentoContabilDataProvider
        );

        usecase.executar(inputData);
    }
}
