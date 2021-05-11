export default {
    tipo: {
        DEPRECIAVEL: 'Depreciável',
        NAO_DEPRECIAVEL: 'Não depreciavel'
    },
    todasContasContabeis: {
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
            sortBy: ['codigo']
        },
        linhasPorPagina: [10, 25, 50, 100]
    },
    contaContabil: {}
}
