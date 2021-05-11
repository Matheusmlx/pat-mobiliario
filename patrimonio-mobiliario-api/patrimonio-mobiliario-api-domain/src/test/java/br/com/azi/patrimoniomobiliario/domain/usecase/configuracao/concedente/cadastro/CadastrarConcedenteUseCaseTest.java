package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.converter.CadastrarConcedenteOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjIncorretoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class CadastrarConcedenteUseCaseTest  {

    @Test
    public void deveCadastrarConcedenteComCpf() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("438.018.270-38")
            .nome("Mário de Freitas")
            .situacao("ATIVO")
            .build();

        Mockito.when(dataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("438.018.270-38")
                .tipoPessoa(Concedente.Pessoa.FISICA)
                .nome("Mário de Freitas")
                .situacao(Concedente.Situacao.ATIVO)
                .inclusaoSistema(true)
                .build());

       CadastrarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("438.018.270-38", outputData.getCpfCnpj());
        Assert.assertEquals("FISICA", outputData.getTipoPessoa());
        Assert.assertEquals("Mário de Freitas", outputData.getNome());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(true,outputData.getInclusaoSistema());
    }

    @Test
    public void deveCadastrarConcedenteComCnpj() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("67.446.247/0001-07")
            .nome("Mário de Freitas Advocacia")
            .situacao("ATIVO")
            .build();

        Mockito.when(dataProvider.salvar(any(Concedente.class)))
            .thenReturn(Concedente
                .builder()
                .id(1L)
                .cpfCnpj("67.446.247/0001-07")
                .tipoPessoa(Concedente.Pessoa.JURIDICA)
                .nome("Mário de Freitas Advocacia")
                .situacao(Concedente.Situacao.ATIVO)
                .inclusaoSistema(true)
                .build());

        CadastrarConcedenteOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("67.446.247/0001-07", outputData.getCpfCnpj());
        Assert.assertEquals("JURIDICA", outputData.getTipoPessoa());
        Assert.assertEquals("Mário de Freitas Advocacia", outputData.getNome());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(true,outputData.getInclusaoSistema());
    }

    @Test(expected = CpfCnpjIncorretoException.class)
    public void deveRetornarErroCpfInvalido() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("438.018.270-11")
            .nome("Mário de Freitas")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);;
    }

    @Test(expected = CpfCnpjIncorretoException.class)
    public void deveRetornarErroCnpjInvalido() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("39.100.710/0001-00")
            .nome("Mário de Freitas")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);;
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemCpf() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .nome("Mário de Freitas")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);;
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemConcedente() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("39.100.710/0001-00")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);;
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemSituacao() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("39.100.710/0001-00")
            .nome("Mário de Freitas")
            .build();

        useCase.executar(inputData);;
    }

    @Test(expected = CpfCnpjCadastradoException.class)
    public void deveRetornarErroAoInserirCpfJaCadastrado() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        CadastrarConcedenteUseCaseImpl useCase = new CadastrarConcedenteUseCaseImpl(
            dataProvider,
            new CadastrarConcedenteOutputDataConverter()
        );

        Mockito.when(dataProvider.existePorCpf(any(String.class)))
            .thenReturn(true);

        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("438.018.270-11")
            .nome("Mário de Freitas")
            .situacao("ATIVO")
            .build();

        useCase.executar(inputData);;
    }
}
