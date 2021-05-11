export default {
    todosOrgaos: {
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
            sortBy: ['situacao']
        }
    },
    rota: {
        origem: 'ReservaListagem'
    }
}
