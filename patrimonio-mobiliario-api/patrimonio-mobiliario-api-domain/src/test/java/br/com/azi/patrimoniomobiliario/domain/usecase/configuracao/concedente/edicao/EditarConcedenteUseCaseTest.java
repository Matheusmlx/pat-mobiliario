package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.converter.EditarConcedenteOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.exception.ConcedenteNaoExisteException;
import br.com.azi.patrimoniomobiliario.utils.string.exception.CpfCnpjIncorretoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class EditarConcedenteUseCaseTest {

    @Test
    public void deveEditarConcedente() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169077")
            .nome("Atualizado")
            .situacao("INATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build()));

        Mockito.when(concedenteDataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Atualizado")
                .situacao(Concedente.Situacao.INATIVO)
                .build());

        EditarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("27358169077", outputData.getCpfCnpj());
        Assert.assertEquals("Atualizado", outputData.getNome());
        Assert.assertEquals("INATIVO", outputData.getSituacao());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNomeEhNulo() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169077")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = ConcedenteNaoExisteException.class)
    public void deveFalharQuandoConcedenteNaoExiste() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169077")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarConcedente() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169077")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build()));

        Mockito.when(concedenteDataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build());

        EditarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals( Long.valueOf(1), outputData.getId());
        Assert.assertEquals("27358169077", outputData.getCpfCnpj());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
    }

    @Test(expected = CpfCnpjIncorretoException.class)
    public void deveFalharQuandoCpfInvalido() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169076")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = CpfCnpjIncorretoException.class)
    public void deveFalharQuandoCnpjInvalido() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("42976622000107")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("42976622000108")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = CpfCnpjCadastradoException.class)
    public void deveFalharQuandoCpfJaCadastrado() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("53742012000158")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existePorCpf(any(String.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("42976622000108")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void deveMudarTipoPessoaParaJuridica() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("53742012000158")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existePorCpf(any(String.class)))
            .thenReturn(false);

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .tipoPessoa(Concedente.Pessoa.FISICA)
                .build()));

        Mockito.when(concedenteDataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("53742012000158")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .tipoPessoa(Concedente.Pessoa.JURIDICA)
                .build());

        EditarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals("JURIDICA", outputData.getTipoPessoa());
    }

    @Test
    public void deveMudarTipoPessoaParaFisica() {
        ConcedenteDataProvider concedenteDataProvider = Mockito.mock(ConcedenteDataProvider.class);

        EditarConcedenteUseCase useCase = new EditarConcedenteUseCaseImpl(
            concedenteDataProvider,
            new EditarConcedenteOutputDataConverter()
        );

        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("27358169077")
            .nome("Teste")
            .situacao("ATIVO")
            .build();

        Mockito.when(concedenteDataProvider.existePorCpf(any(String.class)))
            .thenReturn(false);

        Mockito.when(concedenteDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(concedenteDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("53742012000158")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .tipoPessoa(Concedente.Pessoa.JURIDICA)
                .build()));

        Mockito.when(concedenteDataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("27358169077")
                .nome("Teste")
                .situacao(Concedente.Situacao.ATIVO)
                .tipoPessoa(Concedente.Pessoa.FISICA)
                .build());

        EditarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals("FISICA", outputData.getTipoPessoa());
    }
}
