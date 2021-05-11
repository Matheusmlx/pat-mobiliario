package br.com.azi.hal.core.generic.entity.listener;

import br.com.azi.hal.core.generic.entity.BaseObject;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class AuditListener {

    private static final String USUARIO_JOB = "JobSystem";

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @PreUpdate
    public void auditUpdate(BaseObject entity) {
        try {
            if (StringUtils.isNotEmpty(sessaoUsuarioDataProvider.getLogin())) {
                entity.setUsuarioAlteracao(sessaoUsuarioDataProvider.getLogin());
            } else {
                entity.setUsuarioAlteracao(USUARIO_JOB);
            }
        } catch (Exception e) {
            entity.setUsuarioAlteracao(USUARIO_JOB);
        }

        entity.setDataAlteracao(LocalDateTime.now());
    }

    @PrePersist
    public void auditPersist(BaseObject entity) {
        try {
            if (StringUtils.isNotEmpty(sessaoUsuarioDataProvider.getLogin())) {
                entity.setUsuarioCadastro(sessaoUsuarioDataProvider.getLogin());
            } else {
                entity.setUsuarioCadastro(USUARIO_JOB);
            }
        } catch (Exception e) {
            entity.setUsuarioCadastro(USUARIO_JOB);
        }

        entity.setDataCadastro(LocalDateTime.now());
        entity.setDataAlteracao(LocalDateTime.now());
    }
}
