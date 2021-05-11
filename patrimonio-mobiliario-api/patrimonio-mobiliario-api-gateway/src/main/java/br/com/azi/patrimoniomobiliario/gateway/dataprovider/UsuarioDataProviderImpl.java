package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.UsuarioConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.UsuarioReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDataProviderImpl implements UsuarioDataProvider {

    @Autowired
    private UsuarioReadOnlyRepository usuarioReadOnlyRepository;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Override
    public Usuario buscarUsuarioPorLogin(String login) {
        return usuarioConverter.to(usuarioReadOnlyRepository.findByEmail(login));
    }

    @Override
    public Usuario buscarUsuarioPorSessao(SessaoUsuario sessaoUsuario) {
        return this.buscarUsuarioPorLogin(sessaoUsuario.getLogin());
    }
}
