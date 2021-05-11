package br.com.azi.hal.core.generic.entity;

import br.com.azi.hal.core.generic.entity.listener.AuditListener;
import lombok.Data;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class BaseObject implements Serializable {

    protected LocalDateTime dataCadastro;

    protected LocalDateTime dataAlteracao;

    protected String usuarioCadastro;

    protected String usuarioAlteracao;
}
