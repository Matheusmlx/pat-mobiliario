package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UnidadeOrganizacionalReadOnlyRepository extends Repository<UnidadeOrganizacionalEntity, Long> {

    UnidadeOrganizacionalEntity findByNome(String nome);

    UnidadeOrganizacionalEntity findById(Long id);

    @Query(value = "SELECT new UnidadeOrganizacionalEntity (orgao.id, orgao.nome, orgao.sigla, orgao.codigoHierarquia)  " +
        "FROM UnidadeOrganizacionalEntity orgao, DominioEntity dm " +
        "WHERE orgao.tipoAdministracao IS NULL " +
        "AND orgao.situacao = 'ATIVO' " +
        "AND dm.chaveCliente = orgao.id " +
        "AND dm.usuario.id = ?1 " +
        "AND dm.tipoCliente = 'ESTRUTURA_ORGANIZACIONAL' " +
        "AND orgao.almoxarifado = true " +
        "AND orgao.codigoHierarquia LIKE ?2% " +
        "ORDER BY orgao.sigla, orgao.nome ASC")
    List<UnidadeOrganizacionalEntity> buscarSetoresAlmoxarifado(Long usuarioId, String codigoHierarquiaAncestral);
}
