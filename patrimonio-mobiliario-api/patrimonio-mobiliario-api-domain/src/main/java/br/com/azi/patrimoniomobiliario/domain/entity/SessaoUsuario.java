package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessaoUsuario {

    private Long id;
    private String login;
    private Long dominioId;
    private String dominioTipo;
    private String host;
    private List<String> permissoes;
    private List<Cookie> cookies;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cookie {
        private String nome;
        private String valor;
        private String dominio;
        private String path;
        private Boolean secure;
        private Boolean httpOnly;
    }
}

