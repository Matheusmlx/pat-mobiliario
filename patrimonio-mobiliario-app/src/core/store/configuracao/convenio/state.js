export default {
    dadosGerais: {},
    conveniosValidos: [],
    resultadoBuscaTodosConvenios: {
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
    resumoConvenio: {},
    rota: {
        origem: 'ConvenioListagem'
    }
}
