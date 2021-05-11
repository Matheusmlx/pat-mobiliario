export default {
    resultadoBuscaTodosPatrimonios: {
        paginacao: {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['numero'],
            defaultSortBy: ['numero']
        }
    },
    resultadoBuscaTodosPatrimoniosRegistro: {
        filtros: {
            situacao: {
                value: 'ATIVO'
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
    resultadoBuscaTodosPatrimoniosEstornados: {
        paginacao: {
            page: 1,
            rowsPerPage: 5,
            sortDesc: [false],
            sortBy: ['numero'],
            defaultSortBy: ['numero']
        }
    },
    resultadoBuscaTodosPatrimoniosListagem: {
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
            sortBy: ['orgao'],
            defaultSortBy: ['orgao']
        }
    },
    resultadoBuscaTodosPatrimoniosDeTodosItens:{
        paginacao: {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['numero'],
            defaultSortBy: ['numero']
        },
        filtros: {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: ''
            }
        }
    },
    estorno: {motivo: null, patrimoniosId: []}
}
