package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ReservaIntervaloNumeroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloNumeroEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ReservaIntervaloNumeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservaIntervaloNumeroDataProviderImpl implements ReservaIntervaloNumeroDataProvider {

    private final ReservaIntervaloNumeroRepository repository;

    private final ReservaIntervaloNumeroConverter converter;

    public List<ReservaIntervaloNumero> salvar(List<ReservaIntervaloNumero> reservaIntervaloNumeros) {
        List<ReservaIntervaloNumeroEntity> entidades = reservaIntervaloNumeros.stream()
            .map(converter::from)
            .collect(Collectors.toList());

        List<ReservaIntervaloNumeroEntity> entidadesSalva = repository.saveAll(entidades);
        return entidadesSalva.stream().map(converter::to).collect(Collectors.toList());
    }

    @Override
    public ReservaIntervaloNumero salvar(ReservaIntervaloNumero reservaIntervaloNumero) {
        ReservaIntervaloNumeroEntity entidade = repository.save(converter.from(reservaIntervaloNumero));
        return converter.to(entidade);
    }

    @Override
    public boolean existePorIntervalo(Long reservaIntervaloId) {
        return repository.existsByReservaIntervaloId(reservaIntervaloId);
    }

    @Override
    public boolean existePorIntervalo(List<Long> reservaIntervaloIds) {
        return repository.existsByReservaIntervaloIdIn(reservaIntervaloIds);
    }

    @Override
    public boolean existeNumeroUtilizadoPorIntervalo(List<Long> reservaIntervaloIds) {
        return repository.existsByReservaIntervaloIdInAndUtilizadoTrue(reservaIntervaloIds);
    }

    @Override
    @Modifying
    @Transactional
    public void removerPorIntervaloId(List<Long> reservaIntervaloIds) {
        repository.deleteByReservaIntervaloIdIn(reservaIntervaloIds);
    }

    @Override
    public List<ReservaIntervaloNumero> buscarPorIntervalo(Long reservaIntervaloId) {
        return repository.findAllByReservaIntervaloId(reservaIntervaloId)
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Boolean existeUtilizadoPorIntervalo(List<Long> reservaIntervalosId) {
        return repository.existsByReservaIntervaloIdInAndUtilizadoIsTrue(reservaIntervalosId);
    }
}
