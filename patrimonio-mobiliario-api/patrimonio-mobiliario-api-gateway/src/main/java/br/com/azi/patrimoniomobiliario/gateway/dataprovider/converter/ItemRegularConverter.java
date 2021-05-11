package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemRegular;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemRegularEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class ItemRegularConverter extends GenericConverter<ItemRegularEntity, ItemRegular> {}
