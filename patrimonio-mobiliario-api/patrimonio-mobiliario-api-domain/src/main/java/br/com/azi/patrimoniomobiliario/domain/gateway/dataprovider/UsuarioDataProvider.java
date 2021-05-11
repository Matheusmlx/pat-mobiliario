package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;

public interface UsuarioDataProvider {
    Usuario buscarUsuarioPorLogin(String login);

    Usuario buscarUsuarioPorSessao(SessaoUsuario sessaoUsuario);
}
