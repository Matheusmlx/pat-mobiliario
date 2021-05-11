package br.com.azi.patrimoniomobiliario.integrationtest.arquivo;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UploadArquivoTemporarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Transactional
    @Sql({"/datasets/usuario.sql"})
    public void deveRealizarUploadDeArquivo() throws Exception{

        createMockRequisicaoUpload();

        String fileName = "teste.pdf";
        File file = new File("repo1:patrimoniomobiliario/teste.pdf");

        file.delete();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",fileName,
            "text/plain", "test data".getBytes());

        MockHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.fileUpload("/v1/arquivos")
                .file(mockMultipartFile)
                .cookie(AuthenticationHelper.getCookies())
            .headers(AuthenticationHelper.getHeaders())
            .contentType(MediaType.MULTIPART_FORM_DATA);


        mockMvc.perform(builder)
            .andExpect(status().is2xxSuccessful());

    }

    private void createMockRequisicaoUpload() {
        String response = FileHelper.getJson("setup", "upload-arquivo.json");

        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }
}
