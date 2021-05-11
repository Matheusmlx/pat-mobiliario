package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.NaturezaDespesaConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NaturezaDespesaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.NaturezaDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NaturezaDespesaDataProviderImpl implements NaturezaDespesaDataProvider {

    @Autowired
    private NaturezaDespesaRepository repository;

    @Autowired
    private NaturezaDespesaConverter converter;

    @Override
    public List<NaturezaDespesa> buscarPorFiltro(Long materialServicoId) {
        List<NaturezaDespesaEntity> entidadesEncontradas = repository.buscaNaturezaDespesaPorIdMaterialServico(materialServicoId);

        return entidadesEncontradas
                .stream()
                .map(converter::to)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NaturezaDespesa> buscarPorId(Long id) {
        Optional<NaturezaDespesaEntity> encontrada = repository.findById(id);
        return encontrada.map(naturezaDespesaEntity -> converter.to(naturezaDespesaEntity));
    }
}
