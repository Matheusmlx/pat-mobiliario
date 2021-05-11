package br.com.azi.patrimoniomobiliario.domain.gateway.integration;


import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResponsabilidadeReservaPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResposabilidadeMovimentacaoInterna;

public interface SistemaDeRelatoriosIntegration {

    Arquivo gerarTermoGuardaResponsabilidadeMovimentacaoInterna(TermoGuardaResposabilidadeMovimentacaoInterna relatorio);

    Arquivo gerarMemorandoMovimentacaoInterna(MemorandoMovimentacaoInterna relatorio);

    Arquivo gerarTermoGuardaResponsabilidadeReservaPatrimonial(TermoGuardaResponsabilidadeReservaPatrimonial relatorio);
}
