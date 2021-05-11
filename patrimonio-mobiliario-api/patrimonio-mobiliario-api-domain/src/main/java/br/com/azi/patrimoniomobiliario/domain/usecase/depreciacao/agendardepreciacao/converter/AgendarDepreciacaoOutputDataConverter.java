package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.converter;


import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.AgendarDepreciacaoOutputData;

import java.time.LocalDate;

public class AgendarDepreciacaoOutputDataConverter {

    public AgendarDepreciacaoOutputData to(LocalDate data, String cronExpression) {
        AgendarDepreciacaoOutputData agendarDepreciacaoOutputData = new AgendarDepreciacaoOutputData();
        agendarDepreciacaoOutputData.setData(data);
        agendarDepreciacaoOutputData.setCron(cronExpression);
        return agendarDepreciacaoOutputData;
    }

}
