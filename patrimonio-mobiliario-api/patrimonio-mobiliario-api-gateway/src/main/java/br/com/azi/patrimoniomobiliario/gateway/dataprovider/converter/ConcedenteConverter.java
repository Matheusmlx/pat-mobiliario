package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConcedenteEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class ConcedenteConverter extends GenericConverter<ConcedenteEntity, Concedente> {
}
