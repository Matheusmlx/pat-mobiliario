export default {
    resultadoBuscaTodosItensAdicionadosMovimentacaoInterna: {
        filtros: {},
        paginacao: {
            page: 1,
            rowsPerPage: 10,
            sortDesc: []
        }
    },
    resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
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
            sortBy: ['numero'],
            defaultSortBy: ['numero']
        }
    },
}
