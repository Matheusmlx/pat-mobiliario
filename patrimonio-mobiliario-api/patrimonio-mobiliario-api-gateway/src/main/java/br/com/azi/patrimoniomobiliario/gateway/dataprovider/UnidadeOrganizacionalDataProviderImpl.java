package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.UnidadeOrganizacionalConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.UnidadeOrganizacionalReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UnidadeOrganizacionalDataProviderImpl implements UnidadeOrganizacionalDataProvider {

    @Autowired
    private UnidadeOrganizacionalReadOnlyRepository unidadeOrganizacionalReadOnlyRepository;

    @Autowired
    private UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Override
    public UnidadeOrganizacional buscarUnidadeOrganizacionalPorNome(String nome) {
        return unidadeOrganizacionalConverter.to(unidadeOrganizacionalReadOnlyRepository.findByNome(nome));
    }

    @Override
    public UnidadeOrganizacional buscarUnidadeOrganizacionalPorId(Long id) {
        return unidadeOrganizacionalConverter.to(unidadeOrganizacionalReadOnlyRepository.findById(id));
    }

    @Override
    public List<UnidadeOrganizacional> buscarSetoresAlmoxarifadoPorOrgao(Long id) {
        UnidadeOrganizacional orgao = buscarUnidadeOrganizacionalPorId(id);
        Long usuario = sessaoUsuarioDataProvider.get().getId();

        List<UnidadeOrganizacionalEntity> entidadesEncontradas = unidadeOrganizacionalReadOnlyRepository.buscarSetoresAlmoxarifado(usuario, orgao.getCodHierarquia());

        return entidadesEncontradas
            .stream()
            .map(unidadeOrganizacionalConverter::to)
            .collect(Collectors.toList());
    }
}
