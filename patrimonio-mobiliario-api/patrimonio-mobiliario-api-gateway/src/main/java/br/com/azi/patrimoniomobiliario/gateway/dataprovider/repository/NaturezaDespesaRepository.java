package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;


import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NaturezaDespesaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaturezaDespesaRepository extends CrudRepository<NaturezaDespesaEntity,Long> {

    @Query(value =  "SELECT * " +
        "from comum_siga.tb_material_servico MS" +
        " INNER join comum_siga.tb_matser_subelem MSS ON MS.ms_id = MSS.ms_id" +
        " INNER join comum_siga.tb_contratacao_subelem CS ON CS.ns_id = MSS.ns_id" +
        " INNER join comum_siga.tb_natureza_despesa ND ON ND.es_id = CS.es_id AND ND.nd_situacao = 'ATIVO'" +
        " INNER join comum_siga.tb_conta_contabil CC ON ND.cc_id = CC.cc_id AND CC.cc_situacao = 'ATIVO'" +
        " INNER join pat_mobiliario.tb_config_conta_contabil CCC ON CC.cc_id = CCC.cc_id" +
        " WHERE MS.MS_ID = ?1 ORDER BY ND.nd_despesa", nativeQuery = true)
    List<NaturezaDespesaEntity> buscaNaturezaDespesaPorIdMaterialServico(Long materialServicoId);

}



