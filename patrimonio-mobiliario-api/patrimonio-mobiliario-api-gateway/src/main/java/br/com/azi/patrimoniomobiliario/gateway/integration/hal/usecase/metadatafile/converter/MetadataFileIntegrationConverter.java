package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.entity.MetadataFileIntegrationResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MetadataFileIntegrationConverter {

    public Arquivo to(MetadataFileIntegrationResponse response) {
        Arquivo destino = Arquivo.builder().build();
        BeanUtils.copyProperties(response, destino);
        return destino;
    }
}
