package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReconhecimentoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class ReconhecimentoConverter extends GenericConverter<ReconhecimentoEntity, Reconhecimento> {
}
