package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.EmpenhoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EmpenhoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.EmpenhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmpenhoDataProviderImpl implements EmpenhoDataProvider {

    @Autowired
    private EmpenhoRepository repository;

    @Autowired
    private EmpenhoConverter converter;

    @Override
    public Optional<Empenho> buscarPorId(Long id) {
        Optional<EmpenhoEntity> empenho = repository.findById(id);
        return empenho.map(converter::to);
    }

    @Override
    public Empenho salvar(Empenho empenho) {
        EmpenhoEntity entidade = repository.save(converter.from(empenho));
        return converter.to(entidade);
    }

    @Override
    public List<Empenho> buscarPorIncorporacao(Empenho.Filtro filtro) {
        List<EmpenhoEntity> entidadesEncontradas = buscarComOrdenacao(filtro);

        return entidadesEncontradas
                .stream()
                .map(converter::to)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private List<EmpenhoEntity> buscarComOrdenacao(Empenho.Filtro filtro) {
        return repository.findAllByIncorporacao(filtro.getIncorporacaoId());
    }

    public Long retornaQuantidadePorIncorporacaoId(Long incorporacaoId){
       return repository.countByIncorporacao_Id(incorporacaoId);
    }

}
