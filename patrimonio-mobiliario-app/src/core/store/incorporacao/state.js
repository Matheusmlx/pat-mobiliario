export default {
    resultadoBuscaTodasIncorporacoes: {
        incorporacao: {
            tipo: null,
        },
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
    incorporacao: { },
    situacaoIncorporacao:{},
    rota: {
        origem: 'IncorporacaoListagem'
    },
    buscarRegistro:false
}
