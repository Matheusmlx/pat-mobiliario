export default {
    todosItens: {
        filtros: {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
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
    itemCatalogo: {
        codigo: null,
        descricao: null,
        fornecedor: {
            nome: ''
        }
    }
}
