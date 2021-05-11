export default {
    resultadoBuscaTodosItensIncorporacao: {
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
            sortBy: ['codigo'],
            defaultSortBy: ['codigo']
        }
    },
    buscaRegistroItensIncorporacao: {
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
            sortBy: ['codigo'],
            defaultSortBy: ['codigo']
        }
    },
    dadosGerais: {},
    autoSave: {
        show: false
    },
    rotaModal:{
        origem: 'ItensIncorporacaoListagem'
    }
}
