package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.config;

import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniomobiliario.gateway.integration.halreport.utils.FilenameUtils;
import com.google.common.net.MediaType;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import java.io.InputStream;
import java.util.Objects;

public class ITextRelatorioPDFUserAgent extends ITextUserAgent {

    private static final String LOCAL_RECURSO_CSS = "relatorios/html/css/";

    private ITextRenderer defaultRenderer;
    private ResourceConfig.ClasspathResourceLoader classpathResourceLoader;

    public ITextRelatorioPDFUserAgent(ITextRenderer defaultRenderer, ResourceConfig.ClasspathResourceLoader classpathResourceLoader) {
        super(new ITextOutputDevice(defaultRenderer.getDotsPerPoint()));
        this.defaultRenderer = defaultRenderer;
        this.classpathResourceLoader = classpathResourceLoader;
    }

    public void aplicar() {
        SharedContext sharedContext = defaultRenderer.getSharedContext();
        this.setSharedContext(sharedContext);
        sharedContext.setUserAgentCallback(this);
    }

    @Override
    protected InputStream openStream(String uri) {
        try {
            if(Objects.isNull(uri)) {
                return null;
            }
            return classpathResourceLoader.getResource(redirecionar(uri)).getInputStream();
        } catch (Exception e) {
            throw new RelatorioException("Erro ao carregar Recurso para gerar PDF.", e);
        }
    }

    private static String redirecionar(String uri) {
        String nomeRecurso = FilenameUtils.getName(uri);
        String extensao = FilenameUtils.getExtension(nomeRecurso);

        if (MediaType.CSS_UTF_8.subtype().equals(extensao))
            return LOCAL_RECURSO_CSS + nomeRecurso;

        throw new RelatorioException("Recurso do tipo " + extensao + " não suportado", null);
    }

}
