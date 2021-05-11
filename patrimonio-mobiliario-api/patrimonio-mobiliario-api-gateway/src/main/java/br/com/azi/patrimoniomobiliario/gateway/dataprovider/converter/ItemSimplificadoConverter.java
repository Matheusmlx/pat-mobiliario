package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemSimplificadoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class ItemSimplificadoConverter  extends GenericConverter<ItemSimplificadoEntity, ItemSimplificado> {
}
