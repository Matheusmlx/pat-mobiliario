package br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PESSOA", schema = "COMUM_SIGA")
public class FornecedorEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "PE_ID", insertable = false, updatable = false)
    private Long id;

    @Column(name = "PE_NOME_RAZAOSOCIAL", insertable = false, updatable = false)
    private String razaoSocial;

    @Column(name = "PE_NOME_FANTASIA", insertable = false, updatable = false)
    private String nomeFantasia;

    @Column(name = "PE_CPF_CNPJ", insertable = false, updatable = false)
    private String cpfCnpj;

    @Column(name = "PE_SITUACAO", insertable = false, updatable = false)
    private String situacao;

}

