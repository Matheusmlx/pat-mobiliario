package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ResponsavelDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ResponsavelConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponsavelDataProviderImpl implements ResponsavelDataProvider {

    @Autowired
    private ResponsavelRepository repository;

    @Autowired
    private ResponsavelConverter converter;

    @Override
    public List<Responsavel> buscarResponsaveis() {
        return repository
            .findAllByOrderByNome()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }
}
