export default {
    LOKI: {
        DISABLE_GLOBAL_LOADING: 'disableGlobalLoading',
        ENABLE_GLOBAL_LOADING: 'enableGlobalLoading',
        ENABLE_AUTO_SAVING: 'ENABLE_AUTO_SAVING',
        DISABLE_AUTO_SAVING: 'DISABLE_AUTO_SAVING',
        RESET_AUTO_SAVE: 'RESET_AUTO_SAVE',
        SET_AUTO_SAVED_MESSAGE: 'SET_AUTO_SAVED_MESSAGE',
        SET_SAVING_MESSAGE: 'SET_SAVING_MESSAGE',
        SET_GLOBAL_LOADING: 'setGlobalLoading',
        SET_LOADING_MESSAGE: 'setLoadingMessage',
        SET_NOTIFICATION: 'setNotification',
        SHOW_ALERT: 'showAlert',
        SET_MENU_ACTIONS: 'SET_MENU_ACTIONS',
        SET_TIMEZONE: 'setTimezone',
        SET_NOTIFICATION_CONFIG: 'setNotificationConfig'
    },
    COMUM: {
        SET_MENU_AVATAR: 'setMenuAvatar',
        SET_PRODUTO: 'setProduto',
        SET_USUARIO_LOGADO: 'setUsuarioLogado',
        SET_RETRAIR_MENU: 'setRetrairMenu',
        SET_ROTA_ORIGEM: 'setRotaOrigem',
        SET_EXPANDIR_MENU: 'setExpandirMenu',
        SET_HABILITAR_SALVAMENTO_AUTOMATICO: 'setHabilitarSalvamentoAutomatico',
        SET_DESABILITAR_SALVAMENTO_AUTOMATICO: 'setDesabilitarSalvamentoAutomatico',
        SET_LINK_ARQUIVO: 'setLinkArquivo',
        SET_PARAMETROS: 'setParametros',
        MOSTRAR_FALHA_SALVAMENTO_AUTOMATICO: 'mostrarFalhaSalvamentoAutomatico'
    },
    NOTIFICACAO: {
        ACRESCENTA_PAGE_NOTIFICACAO: 'acrescentaPageNotificacao',
        RESETA_PAGE_NOTIFICACAO: 'resetaPageNotificacao',
        SET_NOTIFICACAO_ITENS: 'setNotificacaoItens',
        RESETA_NOTIFICACAO_ITENS: 'resetaNotificacaoItens',
        OCULTA_NO_NOTIFICATION_TEXT: 'ocultaNoNotificationText',
        VOLTA_NO_NOTIFICATION_TEXT_PADRAO: 'voltaNoNotificationTextPadrao'
    },
    PATRIMONIO:{
        SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS: 'setPaginacaoBuscaTodosPatrimonios',
        RESETA_PAGE_PATRIMONIO: 'resetaPagePatrimonio',
        SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO: 'setPaginacaoBuscaTodosPatrimoniosRegistro',
        RESETA_PAGE_PATRIMONIO_REGISTRO: 'resetaPagePatrimonioRegistro',
        SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM: 'setFiltrosBuscaTodosPatrimoniosListagem',
        SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM: 'setPaginacaoBuscaTodosPatrimoniosListagem',
        SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS: 'setPaginacaoBuscaTodosPatrimoniosEstornados',
        RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS: 'resetaPageBuscaTodosPatrimoniosEstornados',
        RESETA_PAGE_PATRIMONIO_LISTAGEM: 'resetaPagePatrimonioListagem',
        SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS: 'setPaginacaoBuscaTodosPatrimoniosDeTodosItens',
        SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS: 'setFiltrosBuscaTodosPatrimoniosDeTodosItens',
        RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS: 'resetaPageBuscaTodosPatrimoniosDeTodosItens',
        SET_ESTORNO: 'setEstorno',
        INCORPORACAO:{
            SET_FILTROS_BUSCA_TODAS_INCORPORACOES: 'setFiltrosBuscaTodasIncorporacoes',
            SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES: 'setPaginacaoBuscaTodasIncorporacoes',
            SET_INCORPORACAO: 'setIncorporacao',
            SET_SITUACAO_INCORPORACAO: 'setSituacaoIncorporacao',
            RESETA_PAGE_INCORPORACAO: 'resetaPageIncorporacao',
            BUSCAR_REGISTRO_INCORPORACAO: 'buscarRegistroIncorporacao',
            INCORPORACAO_ITEM:{
                SET_INCORPORACAO_ITEM: 'setIncorporacaoItem',
                SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO: 'setFiltrosBuscaTodosItensIncorporacao',
                SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO: 'setPaginacaoBuscaTodosItensIncorporacao',
                SET_PAGINACAO_BUSCA_ITENS_INCORPORADOS: 'setPaginacaoBuscaItensIncorporados',
                RESETA_PAGE_INCORPORACAO_ITEM: 'resetaPageIncorporacaoItem',
                RESETA_PAGE_BUSCA_ITENS_INCORPORADOS: 'resetaPageBuscaItensIncorporados',
                ATIVAR_SALVAMENTO_AUTOMATICO: 'ativarSalvamentoAutomatico',
                DESATIVAR_SALVAMENTO_AUTOMATICO: 'desativarSalvamentoAutomatico'
            },
            DOCUMENTO:{
                SET_DOCUMENTOS: 'setDocumentos',
                REMOVER_DOCUMENTOS: 'removerDocumentos'
            }
        }
    },
    CONFIGURACAO:{
        CONTA_CONTABIL:{
           SET_CONTA_CONTABIL: 'setContaContabil',
           SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS:'setFiltrosBuscaTodasContasContabeis',
           SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS:'setPaginacaoBuscaTodasContasContabeis',
           SET_LIMPAR_FILTROS_BUSCA_CONTA:'setLimparFiltrosBuscaConta',
            RESETA_PAGE_CONTA_CONTABIL: 'resetaPageContaContabil'
        },
        CONVENIO: {
            SET_AUTOCOMPLETE_NOMES_DOS_CONVENIOS: 'setAutocompleteNomesDosConvenios',
            SET_CONVENIO: 'setConvenio',
            SET_FILTROS_BUSCA_TODOS_CONVENIOS: 'setFiltrosBuscaTodosConvenios',
            SET_PAGINACAO_BUSCA_TODOS_CONVENIOS: 'setPaginacaoBuscaTodosConvenios',
            SET_RESUMO_DO_CONVENIO: 'setResumoDoConvenio',
            RESETA_PAGE_CONVENIO: 'resetaPageConvenio'
        },
        CONCEDENTE: {
            SET_CONCEDENTE: 'setConcedente',
            SET_FILTROS_BUSCA_TODOS_CONCEDENTES: 'setFiltrosBuscaTodosConcedentes',
            SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES: 'setPaginacaoBuscaTodosConcedentes',
            RESETA_PAGE_CONCEDENTE: 'resetaPageConcedente'
        },
        RECONHECIMENTO: {
            SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS: 'setFiltrosBuscaTodosReconhecimentos',
            SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS:'setPaginacaoBuscaTodosReconhecimentos',
            RESETA_PAGE_RECONHECIMENTO: 'resetaPageReconhecimento'
        },
        RESERVA: {
            SET_FILTROS_BUSCA_TODAS_RESERVAS: 'setFiltrosBuscaTodasReservas',
            SET_PAGINACAO_BUSCA_TODAS_RESERVAS: 'setPaginacaoBuscaTodasReservas',
            RESETA_PAGE_RESERVA: 'resetaPageReserva',
            GLOBAL: {
                SET_FILTRO_BUSCA_ORGAOS_RESERVA: 'setFiltroBuscaOrgaosReserva',
                SET_PAGINACAO_BUSCA_ORGAOS_RESERVA: 'setPaginacaoBuscaOrgaosReserva',
                RESETA_PAGE_ORGAOS_RESERVA: 'resetaPageOrgaosReserva',
                INTERVALO: {
                    SET_PAGINACAO_BUSCA_TODOS_INTERVALOS: 'setPaginacaoBuscaTodosIntervalos',
                    SET_RESULTADO_BUSCA_TODOS_INTERVALOS: 'setResultadoBuscaTodosIntervalos',
                    RESETA_PAGE_RESERVA_INTERVALO: 'resetaPageReservaIntervalo',
                    SET_EXISTE_EM_ELABORACAO: 'setExisteEmElaboracao'
                }
            },
            POR_ORGAO: {
                INTERVALO: {}
            }
        }
    },
    ITEM_CATALOGO: {
        SET_FILTROS_BUSCA_TODOS_ITENS_CATALOGO: 'setFiltrosBuscaTodosItensCatalogo',
        SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO: 'setPaginacaoBuscaTodosItensCatalogo',
        SET_RESUMO_DO_ITEM_CATALOGO: 'setResumoDoItemCatalogo',
        RESETA_PAGE_CATALOGO: 'resetaPageCatalogo',
        SET_ORDENACAO_ITENS_CATALOGO: 'setOrdenacaoItensCatalogo',
        SET_ITEM_CATALOGO: 'setItemCatalogo'
    },
    MOVIMENTACAO_INTERNA: {
        SET_MOVIMENTACAO_INTERNA: 'setMovimentacaoInterna',
        SET_SITUACAO_MOVIMENTACAO_INTERNA: 'setSituacaoMovimentacaoInterna',
        RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS: 'resetaPageBuscaTodasMovimentacoesInternas',
        SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS: 'setFiltrosBuscaTodasMovimentacoesInternas',
        SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS: 'setPaginacaoBuscaTodasMovimentacoesInternas',
        ITEM: {
            RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA: 'resetaPageBuscaTodosItensAdicionadosMovimentacaoInterna',
            SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA:'setPaginacaoBuscaTodosItensAdicionadosMovimentacaoInterna',
            RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA: 'resetaPageBuscaTodosItensParaSelecaoMovimentacaoInterna',
            SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA: 'setFiltrosBuscaTodosItensParaSelecaoMovimentacaoInterna',
            SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA: 'setPaginacaoBuscaTodosItensParaSelecaoMovimentacaoInterna',
            BUSCAR_ITENS_ADICIONADOS: 'buscarItensAdicionados'
        },
        DOCUMENTO: {
            SET_DOCUMENTOS_MOVIMENTACAO_INTERNA: 'setDocumentosMovimentacaoInterna',
            REMOVER_DOCUMENTO_MOVIMENTACAO_INTERNA: 'removerDocumentoMovimentacaoInterna'
        },
        VISUALIZACAO: {
            SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO: 'setPaginacaoBuscaTodosItensVisualizacao',
            RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO: 'resetaPageBuscaTodosItensVisualizacao',
            DEVOLUCAO_ITEM: {
                SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO: 'setPaginacaoBuscaTodosItensParaDevolucao',
                RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO: 'resetaPageBuscaTodosItensParaDevolucao',
                SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO: 'setFiltrosBuscaTodosItensParaDevolucao'
            }
        }
    }

}
