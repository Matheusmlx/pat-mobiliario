package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemIncorporacao {
    private Long id;
    private Long idIncorporacao;
    private String codigo;
    private String descricao;
    private String marca;
    private String modelo;
    private String fabricante;
    private BigDecimal valorTotal;
    private Integer quantidade;
    private NumeracaoPatrimoninal numeracaoPatrimonial;
    private String uriImagem;
    private String imagem;
    private String anoFabricacaoModelo;
    private Combustivel combustivel;
    private Categoria categoria;
    private Situacao situacao;
    private String tipoBem;
    private ConfiguracaoDepreciacao configuracaoDepreciacao;
    private NaturezaDespesa naturezaDespesa;
    private ContaContabil contaContabil;
    private EstadoConservacao estadoConservacao;
    private Incorporacao incorporacao;

    public enum NumeracaoPatrimoninal{
        RESERVA,
        AUTOMATICA
    }

    public enum Combustivel{
        GASOLINA,
        ALCOOL,
        GAS,
        FLEX,
        ELETRICO,
        DIESEL,
        QUEROSENE
    }

    public enum Categoria{
        NENHUM,
        PASSEIOS,
        PICAPES,
        UTILITARIOS,
        VANS,
        EMBARCACOES,
        ESPORTIVOS,
        MOTOCICLETAS,
        AERONAVES
    }

    public enum Situacao{
        EM_ELABORACAO,
        FINALIZADO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends  FiltroBase{
        Long incorporacaoId;
        String conteudo;
    }
}
