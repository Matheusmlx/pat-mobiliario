package br.com.azi.patrimoniomobiliario.domain.constant.permissoes;

public enum PermissaoMobiliarioConstants {
    NORMAL("Mobiliario.Normal"), CONSULTA("Mobiliario.Consulta");
    private final String description;

    PermissaoMobiliarioConstants(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
