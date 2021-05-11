package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UsuarioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.UsuarioReadOnlyRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class SessaoUsuarioDataProviderImpl implements SessaoUsuarioDataProvider {

    private UsuarioReadOnlyRepository usuarioReadOnlyRepository;

    private String login;

    private String dominioId;

    private String dominioTipo;

    private String host;

    private List<String> permissoes;

    private List<SessaoUsuario.Cookie> cookies;

    @Override
    public SessaoUsuario get() {
        UsuarioEntity usuario = usuarioReadOnlyRepository.findByEmail(this.login);
        SessaoUsuario sessaoUsuario = SessaoUsuario
            .builder()
            .id(usuario.getId())
            .login(this.login)
            .dominioTipo(this.dominioTipo)
            .host(this.host)
            .permissoes(this.permissoes)
            .cookies(this.cookies)
            .build();

        if (Objects.nonNull(this.dominioId) && !this.dominioId.isEmpty()) {
            sessaoUsuario.setDominioId(Long.valueOf(this.dominioId));
        }

        return sessaoUsuario;
    }

    @Override
    public String getLogin() {
        return this.login;
    }
}
