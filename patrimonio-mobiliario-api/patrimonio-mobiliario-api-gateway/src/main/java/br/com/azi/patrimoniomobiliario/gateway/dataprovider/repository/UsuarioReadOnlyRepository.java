package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UsuarioEntity;
import org.springframework.data.repository.Repository;

public interface UsuarioReadOnlyRepository extends Repository<UsuarioEntity, Long> {

    UsuarioEntity findByEmail(String email);

    UsuarioEntity findById(Long id);
}
