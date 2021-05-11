package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastro;


import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.converter.CadastrarItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.exception.IncorporacaoNaoEstaEmElaboracaoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarItemIncorporacaoUseCaseTest {

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    private CadastrarItemIncorporacaoUseCaseImpl useCase;

    @Before
    public void inicializar() {
        useCase = new CadastrarItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            incorporacaoDataProvider,
            estadoConservacaoDataProvider,
            new CadastrarItemIncorporacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarDados() {
        useCase.executar(new CadastrarItemIncorporacaoInputData());;
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQUandoNaoExistirIncorporacao() {
        CadastrarItemIncorporacaoInputData inputData = CadastrarItemIncorporacaoInputData
            .builder()
            .descricao("teste item")
            .codigo("001")
            .incorporacaoId(1L)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverEmModoElaboracao() {
        final CadastrarItemIncorporacaoInputData inputData = CadastrarItemIncorporacaoInputData
            .builder()
            .incorporacaoId(1L)
            .descricao("teste item")
            .codigo("001")
            .build();

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .build();

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(incorporacao));

        useCase.executar(inputData);
    }

    @Test
    public void deveCadastrarItemIncorporacaoQuandoIncorporacaoEstiverEmElaboracao() {
        CadastrarItemIncorporacaoInputData inputData = CadastrarItemIncorporacaoInputData
            .builder()
            .descricao("teste item")
            .codigo("001")
            .incorporacaoId(1L)
            .build();

        Mockito.when(estadoConservacaoDataProvider.obterMelhorEstadoConservacao()).thenReturn(
            EstadoConservacao
                .builder()
                .id(1L)
                .build());

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(ItemIncorporacao
                .builder()
                .id(1L)
                .incorporacao(Incorporacao.builder() .id(1L) .build())
                .descricao("teste item")
                .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
                .codigo("001")
                .build());

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(
                    Incorporacao
                        .builder()
                        .id(1L)
                        .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                        .reconhecimento(Reconhecimento
                            .builder()
                            .execucaoOrcamentaria(true)
                            .situacao(Reconhecimento.Situacao.ATIVO)
                            .build())
                        .build()
                ));

        CadastrarItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("001", outputData.getCodigo());
        Assert.assertEquals("teste item", outputData.getDescricao());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }

    @Test
    public void deveCadastrarItemIncorporacaoQuandoIncorporacaoEstiverComErroProcessamento() {
        CadastrarItemIncorporacaoInputData inputData = CadastrarItemIncorporacaoInputData
            .builder()
            .descricao("teste item")
            .codigo("001")
            .incorporacaoId(1L)
            .build();

        Mockito.when(estadoConservacaoDataProvider.obterMelhorEstadoConservacao()).thenReturn(
            EstadoConservacao
                .builder()
                .id(1L)
                .build());

        Mockito.when(itemIncorporacaoDataProvider.salvar(any(ItemIncorporacao.class)))
            .thenReturn(ItemIncorporacao
                .builder()
                .id(1L)
                .incorporacao(Incorporacao.builder() .id(1L) .build())
                .descricao("teste item")
                .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
                .codigo("001")
                .build());

        Mockito.when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(
                Optional.of(
                    Incorporacao
                        .builder()
                        .id(1L)
                        .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
                        .reconhecimento(Reconhecimento
                            .builder()
                            .execucaoOrcamentaria(true)
                            .situacao(Reconhecimento.Situacao.ATIVO)
                            .build())
                        .build()
                ));

        CadastrarItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("001", outputData.getCodigo());
        Assert.assertEquals("teste item", outputData.getDescricao());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }

}
