package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Job {

    private Status status;

    private static Job job;

    private Job(){
        this.status = Status.PARADO;
    }

    public static Job getInstance(){
        if(job == null) job = new Job();
        return job;
    }

    public enum Status {
        EM_ANDAMENTO,
        PARADO
    }

}
