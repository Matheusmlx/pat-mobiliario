package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;

import java.util.List;
import java.util.Optional;

public interface PatrimonioDataProvider {
    void salvar(List<Patrimonio> patrimonios);

    Patrimonio atualizar(Patrimonio patrimonio);

    void atualizarTodos(List<Patrimonio> patrimonios);

    Optional<Patrimonio> buscarPorId(Long id);

    ListaPaginada<Patrimonio> buscarPatrimoniosIncorporados(Patrimonio.Filtro filtro);

    ListaPaginada<Patrimonio> buscarPatrimoniosEstornados(Patrimonio.Filtro filtro);

    ListaPaginada<Patrimonio> buscarPatrimoniosPorItensIncorporacao(Patrimonio.Filtro filtro);

    ListaPaginada<Patrimonio> buscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(Patrimonio.Filtro filtro);

    Optional<Patrimonio> buscarPatrimonioComMaiorNumero();

    Optional<Patrimonio> buscarPatrimonioComMaiorNumeroPorOrgao(Long orgaoId);

    Boolean existeEmIntervalo(Patrimonio.Filtro filtro);

    ListaPaginada<Patrimonio> buscarPorFiltro(Patrimonio.Filtro filtro);

    List<Patrimonio> buscarTodos(Long itemIncorporacaoId);

    Boolean existe(Long id);

    Boolean existePorChassi(String chassi, Long id);

    Boolean existePorNumeroSerie(String numeroSerie, Long id);

    Boolean existePorMotor(String motor, Long id);

    Boolean existePorPlaca(String placa, Long id);

    Boolean existePorRenavam(String renavam, Long id);

    Long quantidadePorItemIncorporacao(Long itemIncorporacaoId);

    void excluiUltimosPatrimonios(Long qtdPatrimoniosASeremExcluidos, Long itemIncorporacaoId);

    List<Patrimonio> buscarPatrimoniosPorItemIncorporacaoId(Long itemIncorporacaoId);

    List<Patrimonio> buscarPatrimoniosAtivosPorIncorporacaoId(Long itemIncorporacaoId, List<Long> patrimoniosExcecao);

    Long buscarQuantidadePatrimoniosPorIncorporacaoId(Long incorporacaoId);

    Long buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(Long incorporacaoId);

    List<Patrimonio> buscarTodosPatrimonios(List<Long> patrimoniosId);

    Long buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(Long incorporacaoId);

    List<Patrimonio> buscarPatrimoniosAtivosPorOrgaoSetorQueNaoEstaoEmOutraMovimentacao(List<Long> patrimoniosNaoConsiderar, Long orgaoId, Long setorId);

    List<Patrimonio> buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(List<Long> patrimoniosId, Long orgaoId, Long setorId);

    ListaPaginada<Patrimonio> buscarPatrimoniosAguardandoDevolucaoPorMovimentacao(Patrimonio.Filtro filtro, Long movimentacaoId);

    List<Patrimonio> buscarPatrimoniosPorIncorporacao(Patrimonio.Filtro filtro);

    List<Patrimonio> buscarPatrimoniosPorIncorporacao(Long incorporacaoId);

    void bloquearEntidade();

}
