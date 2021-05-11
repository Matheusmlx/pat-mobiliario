package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ComodanteEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class ComodanteConverter extends GenericConverter<ComodanteEntity, Comodante> {
}

