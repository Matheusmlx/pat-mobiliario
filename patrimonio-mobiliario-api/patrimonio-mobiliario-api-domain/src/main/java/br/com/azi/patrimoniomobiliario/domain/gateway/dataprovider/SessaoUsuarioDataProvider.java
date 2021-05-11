package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;

public interface SessaoUsuarioDataProvider {

    SessaoUsuario get();

    String getLogin();
}
