import comodato from './ComodatoApiClient'
import concedente from './ConcedenteApiClient'
import contaContabil from './ContaContabilApiClient'
import convenio from './ConvenioApiClient'
import devolucaoAlmoxarifado from './DevolucaoAlmoxarifadoApiClient'
import distribuicao from './DistribuicaoApiClient'
import empenho from './EmpenhoApiClient'
import entreEstoques from './EntreEstoquesApiClient'
import entreSetores from './EntreSetoresApiClient'
import estadoConservacao from './EstadoConservacaoApiClient'
import fornecedor from './FornecedorApiClient'
import fusoHorario from './FusoHorarioApiClient'
import incorporacao from './IncorporacaoApiClient'
import itens from './ItemCatalogoApiClient'
import itemIncorporacao from './ItemIncorporacaoApiClient'
import movimentacaointerna from './MovimentacaoInternaApiClient'
import movimentacaoInternaDocumento from './MovimentacaoInternaDocumentoApiClient'
import movimentacaoInternaItem from './MovimentacaoInternaItemApiClient'
import movimentacaoInternaVisualiacao from './MovimentacaoInternaVisualizacaoApiClient'
import naturezaDespesa from './NaturezaDespesaApiClient'
import notificacao from './NotificacaoApiClient'
import parametros from './ParametrosApiClient'
import patrimonio from './PatrimonioApiClient'
import produto from './ProdutoApiClient'
import reconhecimento from './ReconhecimentoApiClient'
import relatorio from './RelatorioApiClient'
import reservaGeral from './ReservaApiClient'
import reservaGlobal from './ReservaGlobalApiClient'
import reservaIntervaloGeral from './ReservaIntervaloApiClient'
import reservaIntervaloGlobal from './ReservaIntervaloGlobalApiClient'
import reservaIntervaloPorOrgao from './ReservaIntervaloPorOrgaoApiClient'
import reservaPorOrgao from './ReservaPorOrgaoApiClient'
import responsavel from './ResponsavelApiClient'
import rotulosPersonalizados from './RotulosPersonalizadosApiClient'
import temporaria from './TemporarioApiClient'
import unidadeOrganizacional from './UnidadeOrganizacionalApiClient'
import usuario from './UsuarioApiClient'

import parametroState from '@/core/store/configuracao/parametros/state'

const reservaPatrimonialGlobal = parametroState.parametros.reservaPatrimonialGlobal

export default {
    comodato,
    concedente,
    contaContabil,
    convenio,
    devolucaoAlmoxarifado,
    distribuicao,
    empenho,
    entreEstoques,
    entreSetores,
    estadoConservacao,
    fornecedor,
    fusoHorario,
    incorporacao,
    itens,
    itemIncorporacao,
    movimentacaointerna,
    movimentacaoInternaDocumento,
    movimentacaoInternaItem,
    movimentacaoInternaVisualiacao,
    naturezaDespesa,
    notificacao,
    parametros,
    patrimonio,
    produto,
    reconhecimento,
    relatorio,
    reservaGeral,
    reserva: reservaPatrimonialGlobal ? reservaGlobal : reservaPorOrgao,
    reservaIntervaloGeral,
    reservaIntervalo: reservaPatrimonialGlobal ? reservaIntervaloGlobal : reservaIntervaloPorOrgao,
    responsavel,
    rotulosPersonalizados,
    temporaria,
    unidadeOrganizacional,
    usuario
}
