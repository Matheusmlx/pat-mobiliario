package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.usecase.gerartermoguardaresponsabilidademovimentacaointernapdf;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResposabilidadeMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.ConfiguracaoRelatorio;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.utils.ReportHTMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class GerarTermoGuardaResposabilidadeMovimentacaoInternaPDFUseCase {

    private static final String NOME_RELATORIO = "termo_guarda_responsabilidade";

    private static final String CAMINHO_TEMPLATE = "movimentacao/interna/termoguarda";

    private static final String LOGO_ATRIBUTO = "logoRelatorio";

    private static final String [] SETOR_DESTINO_ALMOXARIFADO = {"Devolução Almoxarifado","Entre Estoques"};

    @Autowired
    private ReportHTMLUtils reportHTMLUtils;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private ProdutoAtributoDataProvider produtoAtributoDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, TermoGuardaResposabilidadeMovimentacaoInterna termo) {
        Map<String, Object> dataSource = configurarDataSouce(integrationProperties, termo);
        ConfiguracaoRelatorio configuracaoRelatorio = configurarRelatorio(dataSource);

        return gerarRelatorio(configuracaoRelatorio);
    }

    private Map<String, Object> configurarDataSouce(HalReportIntegrationProperties integrationProperties, TermoGuardaResposabilidadeMovimentacaoInterna relatorio){
        NumberFormat df = NumberFormat.getCurrencyInstance();
        String setorDestinoFormatado = getLabelSetorDestino(relatorio.getTipo());
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("properties", integrationProperties);
        parametros.put("relatorio", relatorio);
        parametros.put("dataHoraUsuario", getDataHoraEUsuarioNome());
        parametros.put("df",df);
        parametros.put("setorDestinoFormatado",setorDestinoFormatado);

        carregarLogo(parametros, LOGO_ATRIBUTO);

        return parametros;
    }

    private String getLabelSetorDestino(String tipoMovimentacao) {
        if(Arrays.stream(SETOR_DESTINO_ALMOXARIFADO).anyMatch(tipoMovimentacao::equals)){
            return "Almoxarifado Destino";
        }
        return "Setor Destino";
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
        return reportHTMLUtils.construirConfiguracaoPadrao(NOME_RELATORIO, CAMINHO_TEMPLATE, dataSource)
            .setNomeTemplateHeader("header-default")
            .setNomeTemplateFooter("footer-default");
    }

    private Arquivo gerarRelatorio(ConfiguracaoRelatorio configuracaoRelatorio){
        try {
            byte[] content = reportHTMLUtils.baixar(configuracaoRelatorio);

            return Arquivo
                .builder()
                .contentType("application/pdf")
                .nome(String.format("%s.pdf", NOME_RELATORIO))
                .content(content)
                .build();
        } catch (Exception e) {
            throw new RelatorioException("Não foi possivel realizar a geração do relatório.", e);
        }
    }
}
