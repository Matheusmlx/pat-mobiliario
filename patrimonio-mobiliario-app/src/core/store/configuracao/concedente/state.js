export default {
    todosConcedentes: {
        filtros: {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            },
            situacao:{
                value: null
            }
        },
        paginacao: {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao'],
            defaultSortBy: ['situacao']
        }
    }
}
