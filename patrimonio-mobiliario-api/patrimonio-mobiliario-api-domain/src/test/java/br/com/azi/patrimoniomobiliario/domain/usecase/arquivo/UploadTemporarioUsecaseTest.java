package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.UploadTemporarioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.UploadTemporarioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.UploadTemporarioUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.converter.UploadTemporarioOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class UploadTemporarioUsecaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemCamposObrigatorios() {

        UploadTemporarioInputData inputData = new UploadTemporarioInputData();
        UploadTemporarioUsecaseImpl usecase = new
                UploadTemporarioUsecaseImpl(
                Mockito.mock(SistemaDeArquivosIntegration.class),
                Mockito.mock(UploadTemporarioOutputDataConverter.class));

        usecase.executar(inputData);
    }

    @Test
    public void deveRealizarUpload() {
        UploadTemporarioInputData inputData = UploadTemporarioInputData
                .builder()
                .nome("arquivo.pdf")
                .contentType("application/pdf")
                .content(new byte[]{})
                .build();

        SistemaDeArquivosIntegration sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
        UploadTemporarioOutputDataConverter uploadTemporarioOutputDataConverter = Mockito.mock(UploadTemporarioOutputDataConverter.class);
        UploadTemporarioUsecaseImpl usecase = new
                UploadTemporarioUsecaseImpl(
                sistemaDeArquivosIntegration,
                uploadTemporarioOutputDataConverter);

        Mockito.when(sistemaDeArquivosIntegration.upload(any(Arquivo.class)))
                .thenReturn(Arquivo.builder().build());

        Mockito.when(uploadTemporarioOutputDataConverter.to(any(Arquivo.class)))
                .thenReturn(UploadTemporarioOutputData.builder().build());

        UploadTemporarioOutputData outputData = usecase.executar(inputData);

        Assert.assertNotNull(outputData);

        Mockito.verify(sistemaDeArquivosIntegration, Mockito.times(1)).upload(any(Arquivo.class));
        Mockito.verify(uploadTemporarioOutputDataConverter, Mockito.times(1)).to(any(Arquivo.class));
    }
}
