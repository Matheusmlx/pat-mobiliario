package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.integration;


import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.SistemaDeRelatoriosIntegrationHalReportImpl;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerarmemorandomovimentacaointernapdf.GerarMemorandoMovimentacaoInternaPDFUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidademovimentacaointernapdf.GerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidadereservapatrimonialpdf.GerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-config.integration", name = "sistema-de-gestao-administrativa", havingValue = "hal-report", matchIfMissing = true)
public class SistemaDeRelatoriosIntegrationHalReportFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private GerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase gerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase;

    @Autowired
    private GerarMemorandoMovimentacaoInternaPDFUseCase gerarMemorandoMovimentacaoInternaPDFUseCase;

    @Autowired
    private GerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase gerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase;

    @Bean("SistemaDeRelatoriosIntegration")
    @Primary
    public SistemaDeRelatoriosIntegration createBean() {
        HalReportIntegrationProperties halProperties = getProperties();

        return new SistemaDeRelatoriosIntegrationHalReportImpl(
            halProperties,
            gerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase,
            gerarMemorandoMovimentacaoInternaPDFUseCase,
            gerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase
        );
    }

    public HalReportIntegrationProperties getProperties() {
        return HalReportIntegrationProperties
            .builder()
            .header(HalReportIntegrationProperties.Header
                .builder()
                .titulo1(patrimonioMobiliarioProperties.getRelatorio().getHeader().getTitulo1())
                .titulo2(patrimonioMobiliarioProperties.getRelatorio().getHeader().getTitulo2())
                .build())
            .footer(HalReportIntegrationProperties.Footer
                .builder()
                .rodape1(patrimonioMobiliarioProperties.getRelatorio().getFooter().getRodape1())
                .rodape2(patrimonioMobiliarioProperties.getRelatorio().getFooter().getRodape2())
                .build())
            .build();
    }
}
