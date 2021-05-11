package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidadereservapatrimonialpdf;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResponsabilidadeReservaPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.ConfiguracaoRelatorio;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.utils.ReportHTMLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GerarTermoGuardaResponsabilidadeReservaPatrimonialPDFUseCase {

    private static final String NOME_RELATORIO = "termoguardaresponsabilidadereserva";

    private static final String CAMINHO_RELATORIO = "reserva/termoguarda";

    private static final String LOGO_ATRIBUTO = "logoRelatorio";

    private final ReportHTMLUtils reportHTMLUtils;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final ProdutoAtributoDataProvider produtoAtributoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, TermoGuardaResponsabilidadeReservaPatrimonial memorando) {
        Map<String, Object> dataSource = configurarDataSouce(integrationProperties, memorando);
        ConfiguracaoRelatorio configuracaoRelatorio = configurarRelatorio(dataSource);

        return gerarRelatorio(configuracaoRelatorio);
    }

    private Map<String, Object> configurarDataSouce(HalReportIntegrationProperties integrationProperties, TermoGuardaResponsabilidadeReservaPatrimonial relatorio) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("properties", integrationProperties);
        parametros.put("relatorio", relatorio);
        parametros.put("dataHoraUsuario", getDataHoraEUsuarioNome());

        carregarLogo(parametros, LOGO_ATRIBUTO);

        return parametros;
    }

    private String getDataHoraEUsuarioNome() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return data + " " + usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome();
    }

    private void carregarLogo(Map<String, Object> parametros, String logo) {
        String valor = produtoAtributoDataProvider.getValor(logo);
        Arquivo arquivo = sistemaDeArquivosIntegration.download(valor);
        reportHTMLUtils.carregadorImage(parametros, arquivo.getContent(), logo);
    }

    private ConfiguracaoRelatorio configurarRelatorio(Map<String, Object> dataSource) {
        return reportHTMLUtils.construirConfiguracaoPadrao(NOME_RELATORIO, CAMINHO_RELATORIO, dataSource)
            .setNomeTemplateHeader("header-default")
            .setNomeTemplateFooter("footer-default");
    }

    private Arquivo gerarRelatorio(ConfiguracaoRelatorio configuracaoRelatorio) {
        try {
            byte[] content = reportHTMLUtils.baixar(configuracaoRelatorio);

            return Arquivo
                .builder()
                .contentType("application/pdf")
                .nome(String.format("%s.pdf", NOME_RELATORIO))
                .content(content)
                .build();
        } catch (Exception e) {
            throw new RelatorioException("Não foi possivel realizar a geração do termo de guarda e responsabilidade.", e);
        }
    }

}
