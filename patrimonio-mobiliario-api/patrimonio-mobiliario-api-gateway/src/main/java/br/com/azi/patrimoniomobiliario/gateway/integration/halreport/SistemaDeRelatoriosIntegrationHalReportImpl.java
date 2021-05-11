package br.com.azi.patrimoniomobiliario.gateway.integration.halreport;


import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResponsabilidadeReservaPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResposabilidadeMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerarmemorandomovimentacaointernapdf.GerarMemorandoMovimentacaoInternaPDFUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidademovimentacaointernapdf.GerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidadereservapatrimonialpdf.GerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SistemaDeRelatoriosIntegrationHalReportImpl implements SistemaDeRelatoriosIntegration {

    private HalReportIntegrationProperties integrationProperties;

    private GerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase gerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase;

    private GerarMemorandoMovimentacaoInternaPDFUseCase gerarMemorandoMovimentacaoInternaPDFUseCase;

    private GerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase gerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase;

    @Override
    public Arquivo gerarTermoGuardaResponsabilidadeMovimentacaoInterna(TermoGuardaResposabilidadeMovimentacaoInterna relatorio) {
        return gerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase.executar(integrationProperties, relatorio);
    }

    @Override
    public Arquivo gerarMemorandoMovimentacaoInterna(MemorandoMovimentacaoInterna relatorio) {
        return gerarMemorandoMovimentacaoInternaPDFUseCase.executar(integrationProperties, relatorio);
    }

    @Override
    public Arquivo gerarTermoGuardaResponsabilidadeReservaPatrimonial(TermoGuardaResponsabilidadeReservaPatrimonial relatorio) {
        return gerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase.executar(integrationProperties, relatorio);
    }
}
