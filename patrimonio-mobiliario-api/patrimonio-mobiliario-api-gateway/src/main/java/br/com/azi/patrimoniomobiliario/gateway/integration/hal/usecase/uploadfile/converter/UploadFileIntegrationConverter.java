package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.entity.UploadFileIntegrationResponse;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class UploadFileIntegrationConverter extends GenericConverter<UploadFileIntegrationResponse, Arquivo> {

    @Override
    public Arquivo to(UploadFileIntegrationResponse response) {
        Arquivo destino = super.to(response);

        destino.setNome(response.getName());

        return destino;
    }
}
