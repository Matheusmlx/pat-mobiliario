export default {
    rota: {
        origem: 'MovimentacaoInternaListagem'
    },
    movimentacaoInterna: {},
    situacaoMovimentacaoInterna: null,
    tiposMovimentacaoLinha1: [
        {
            id: 1,
            label: 'DISTRIBUIÇÃO',
            nome: 'DISTRIBUICAO',
            icon: 'send',
            selecionado: false
        },
        {
            id: 2,
            label: 'ENTRE SETORES',
            nome: 'ENTRE_SETORES',
            icon: 'fas fa-long-arrow-alt-right',
            selecionado: false
        },
        {
            id: 3,
            label: 'TEMPORÁRIA',
            nome: 'TEMPORARIA',
            icon: 'fas fa-exchange-alt',
            selecionado: false
        }
    ],
    tiposMovimentacaoLinha2: [
        {
            id: 4,
            label: 'ENTRE ESTOQUES',
            nome: 'ENTRE_ESTOQUES',
            icon: 'fas fa-box',
            selecionado: false
        },
        {
            id: 5,
            label: 'DEVOLUÇÃO ALMOXARIFADO',
            nome: 'DEVOLUCAO_ALMOXARIFADO',
            icon: 'fas fa-box',
            selecionado: false
        }
    ],
    resultadoBuscaTodasMovimentacoesInternas: {
        filtros: {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: ''
            }
        },
        paginacao: {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao'],
            defaultSortBy: ['situacao']
        }
    },
}
