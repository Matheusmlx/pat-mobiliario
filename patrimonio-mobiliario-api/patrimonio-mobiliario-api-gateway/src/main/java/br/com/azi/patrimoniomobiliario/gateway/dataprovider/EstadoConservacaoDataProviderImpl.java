package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.EstadoConservacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EstadoConservacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.EstadoConservacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstadoConservacaoDataProviderImpl implements EstadoConservacaoDataProvider {

    @Autowired
    EstadoConservacaoRepository repository;

    @Autowired
    EstadoConservacaoConverter converter;

    @Override
    public List<EstadoConservacao> buscarEstadosConservacao() {
        List<EstadoConservacaoEntity> entidades = repository.findAllByOrderByPrioridadeAsc();
        return entidades.stream().map(entidade -> converter.to(entidade)).collect(Collectors.toList());
    }

    @Override
    public EstadoConservacao obterMelhorEstadoConservacao() {
        EstadoConservacaoEntity entidade = repository.findFirstByOrderByPrioridadeAsc();
        return converter.to(entidade);
    }
}
