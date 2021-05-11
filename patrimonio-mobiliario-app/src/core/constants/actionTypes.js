export default {
    COMUM: {
        BUSCAR_LINK_EDITAR_USUARIO: 'buscarLinkEditarUsuario',
        BUSCAR_PRODUTO_POR_NOME: 'buscarProdutoPorNome',
        BUSCAR_USUARIO_LOGADO: 'buscarUsuarioLogado',
        BUSCAR_FORNECEDORES: 'buscarFornecedores',
        BUSCAR_FORNECEDOR_POR_ID: 'buscarFornecedorPorId',
        BUSCAR_TODOS_ORGAOS: 'buscarTodosOrgaos',
        BUSCAR_SETORES_ALMOXARIFADO: 'buscarSetoresAlmoxarifado',
        BUSCAR_SETORES_NAO_ALMOXARIFADO: 'buscarSetoresNaoAlmoxarifado',
        BUSCAR_SETORES_POSSUI_ACESSO: 'buscarSetoresPossuiAcesso',
        BUSCAR_NATUREZAS_DESPESA: 'buscarNaturezasDespesa',
        BUSCAR_FUNDOS: 'buscarFundos',
        BUSCAR_FUSO_HORARIO: 'buscarFusoHorario'
    },
    NOTIFICACAO: {
        BUSCAR_NOTIFICACOES: 'buscarNotificacoes',
        BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS: 'buscarQuantidadeNotificacoesNaoVisualizadas'
    },
    PATRIMONIO: {
        CADASTRAR_PATRIMONIO: 'cadastrarPatrimonio',
        ATUALIZAR_PATRIMONIO: 'atualizarPatrimonio',
        BUSCAR_TODOS_PATRIMONIOS_LISTAGEM: 'buscarTodosPatrimoniosListagem',
        BUSCAR_TODOS_PATRIMONIOS: 'buscarTodosTodosPatrimonios',
        BUSCAR_TODOS_PATRIMONIOS_REGISTRO: 'buscarTodosTodosPatrimoniosRegistro',
        BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS: 'buscarTodosPatrimoniosEstornados',
        BUSCAR_PATRIMONIO_POR_ID_FICHA: 'buscarPatrimonioPorIdFicha',
        BUSCAR_TODAS_MOVIMENTACOES: 'buscarTodasMovimentacoes',
        BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS: 'buscarTodosPatrimoniosDeTodosItens',
        ESTORNAR_PATRIMONIOS: 'estornarPatrimonios',
        BUSCAR_DEPRECIACOES: 'buscarDepreciacoes',
        INCORPORACAO: {
            FINALIZAR_INCORPORACAO: 'finalizarIncorporacao',
            CADASTRAR_INCORPORACAO: 'cadastrarIncorporacao',
            ATUALIZAR_INCORPORACAO: 'atualizarIncorporacao',
            BUSCAR_TODAS_INCORPORACOES: 'buscarTodasIncorporacoes',
            BUSCAR_INCORPORACAO_POR_ID: 'buscarIncorporacaoPorId',
            BUSCAR_REGISTRO_INCORPORACAO_POR_ID: 'buscarRegistroIncorporacaoPorId',
            DELETAR_INCORPORACAO: 'deletarIncorporacao',
            REABRIR_INCORPORACAO: 'reabrirIncorporacao',
            BUSCAR_SITUACAO_INCORPORACAO: 'buscarSituacaoIncorporacao',
            INCORPORACAO_ITEM: {
                BUSCAR_ITEM_INCORPORACAO: 'buscarItemIncorporacao',
                BUSCAR_ITEM_INCORPORACAO_REGISTRO: 'buscarItemIncorporacaoRegistro',
                BUSCAR_TODOS_ITENS_INCORPORACAO: 'buscarTodosItensIncorporacao',
                BUSCAR_REGISTRO_ITENS_INCORPORACAO: 'buscarRegistroItensIncorporacao',
                CADASTRAR_ITEM_INCORPORACAO: 'cadastrarItemIncorporacao',
                EDITAR_ITEM_INCORPORACAO: 'editarItemIncorporacao',
                DELETAR_ITEM_INCORPORACAO: 'deletarItemIncorporacao',
                IMAGEM: {
                    UPLOAD: 'uploadImagem',
                    DOWNLOAD: 'downloadImagem'
                },
                ESTADO_CONSERVACAO: {
                    BUSCAR_ESTADOS_CONSERVACAO: 'buscarEstadosConservacao'
                }
            },
            EMPENHO: {
                ATUALIZAR_EMPENHO: 'atualizarEmpenho',
                BUSCAR_TODOS_EMPENHOS: 'buscarTodosEmpenhos',
                INSERIR_EMPENHO: 'inserirEmpenho',
                REMOVER_EMPENHO: 'removerEmpenho',
            },
            DOCUMENTO: {
                BUSCAR_DOCUMENTOS: 'buscarDocumentos',
                ATUALIZAR_DOCUMENTO: 'atualizarDocumento',
                BUSCAR_TIPO_DOCUMENTO: 'buscarTipoDocumento',
                CADASTRAR_DOCUMENTO: 'cadastrarDocumento',
                DELETAR_DOCUMENTO: 'deletarDocumento',
                DOCUMENTO_UPLOAD:'documentoUpload',
                DOCUMENTO_DOWNLOAD:'documentoDownload'
            }
        }
    },
    CONFIGURACAO: {
        CONCEDENTE: {
            BUSCAR_TODOS_CONCEDENTES: 'buscarTodosConcedentes',
            BUSCAR_CONCEDENTES_DINAMICAMENTE: 'buscarConcedentesDinamicamente',
            BUSCAR_CONCEDENTE_POR_ID: 'buscarConcedentePorId',
            EDITAR_CONCEDENTE: 'editarConcedente',
            SALVAR_CONCEDENTE: 'salvarConcedente'
        },
        CONTA_CONTABIL: {
            BUSCAR_CONTA_CONTABIL_POR_ID: 'buscarContaContabilPorId',
            EDITAR_CONTA_CONTABIL: 'editarContaContabil',
            BUSCAR_TODAS_CONTAS_CONTABEIS: 'buscarTodasContasContabeis',
            SALVAR_CONTA_CONTABIL: 'salvarContaContabil'
        },
        CONVENIO: {
            BUSCAR_TODOS_CONVENIOS: 'buscarTodosConvenios',
            SALVAR_CONVENIO: 'salvarConvenio',
            EDITAR_CONVENIO: 'editarConvenio',
            BUSCAR_CONVENIO_POR_ID: 'buscarConvenioPorId',
            BUSCAR_CONVENIO_DINAMICAMENTE: 'buscarConvenioDinamicamente',
        },
        PARAMETROS: {
            BUSCAR_TODOS_PARAMETROS: 'buscarTodosParametros'
        },
        RECONHECIMENTO: {
            EDITAR_RECONHECIMENTO: 'editarReconhecimento',
            BUSCAR_TODOS_RECONHECIMENTOS: 'buscarTodosReconhecimentos',
            BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE: 'buscarReconhecimentosAutoComplete',
            INSERIR_RECONHECIMENTO: 'inserirReconhecimento'
        },
        RESERVA: {
            BUSCAR_TODAS_RESERVAS_LISTAGEM: 'buscarTodasReservasListagem',
            INTERVALO: {
                BAIXAR_TERMO_DE_GUARDA: 'baixarTermoDeGuarda'
            },
            GLOBAL: {
                SALVAR_RESERVA: 'salvarReserva',
                EDITAR_RESERVA: 'editarReserva',
                BUSCAR_RESERVA_POR_ID: 'buscarReservaPorId',
                BUSCAR_PROXIMO_NUMERO_DISPONIVEL: 'buscarProximoNumeroDisponivel',
                BUSCAR_TODOS_ORGAOS_PAGINADO: 'buscarTodosOrgaosPaginado',
                FINALIZAR_RESERVA: 'finalizarReserva',
                VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO:'verificarReservaPossuiNumeroUtilizado',
                DELETAR_RESERVA:'deletarReserva',
                INTERVALO: {
                    BUSCAR_INTERVALO: 'buscarIntervalo',
                    SALVAR_INTERVALO: 'salvarIntervalo',
                    BUSCAR_TODOS_INTERVALOS_LISTAGEM: 'buscarTodosIntervalosListagem',
                    REMOVER_INTERVALO_POR_ID: 'removerIntervaloPorId',
                    EXISTE_EM_ELABORACAO: 'existeIntervalosEmElaboracao',
                    BUSCAR_PROXIMO_NUMERO: 'buscarProximoNumero',
                    VALIDAR_INTERVALO: 'validarIntervalo'
                }
            },
            POR_ORGAO: {
                BUSCAR_INTERVALO_POR_ORGAO: 'buscarIntervaloPorOrgao',
                BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO: 'buscarProximoNumeroDisponivelPorOrgao',
                FINALIZAR_RESERVA_POR_ORGAO: 'finalizarReservaPorOrgao',
            }
        },
        ROTULOS_PERSONALIZADOS: {
            GET_ALL_ROTULOS_PERSONALIZADOS: 'getAllRotulosPersonlizados',
        }
    },
    ITEM_CATALOGO: {
        BUSCAR_TODOS_ITENS_CATALOGO: 'buscarTodosItensCatalogo',
        BUSCAR_ITEM_CATALOGO_POR_ID: 'buscarItemCatalogoPorId'
    },
    MOVIMENTACAO_INTERNA: {
        CADASTRAR_MOVIMENTACAO_INTERNA: 'cadastrarMovimentacaoInterna',
        BUSCAR_TIPO_MOVIMENTACAO_INTERNA: 'buscarTipoMovimentacaoInterna',
        REMOVER_MOVIMENTACAO_INTERNA_COMPLETA: 'removerMovimentacaoInternaCompleta',
        REMOVER_MOVIMENTACAO_INTERNA_VAZIA: 'removerMovimentacaoInternaVazia',
        EDITAR_TIPO_MOVIMENTACAO_INTERNA: 'editarTipoMovimentacaoInterna',
        BUSCAR_TODAS_MOVIMENTACOES_INTERNAS: 'buscarTodasMovimentacoesInternas',
        BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA: 'buscarSituacaoMovimentacaoInterna',
        ITEM: {
            BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA: 'buscarTodosItensAdicionadosMovimentacaoInterna',
            DELETAR_ITEM_MOVIMENTACAO_INTERNA: 'deletarItemMovimentacaoInterna',
            BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA: 'buscarTodosItensParaSelecaoMovimentacaoInterna',
            ADICIONAR_ITENS_MOVIMENTACAO_INTERNA: 'adicionarItensMovimentacaoInterna',
            BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA: 'buscarQuantidadeItensAdicionadosMovimentacaoInterna'
        },
        DISTRIBUICAO: {
            ATUALIZAR_DISTRIBUICAO: 'atualizarDistribuicao',
            BUSCAR_DISTRIBUICAO_POR_ID: 'buscarDistribuicaoPorId',
            FINALIZAR_DISTRIBUICAO: 'finalizarDistribuicao'
        },
        TEMPORARIA: {
            ATUALIZAR_TEMPORARIA:'atualizarTemporaria',
            BUSCAR_TEMPORARIA_POR_ID:'buscarTemporariaPorId',
            DEVOLVER_TODOS_PATRIMONIOS:'devolverTodosPatrimonios',
            BUSCAR_INFORMACAO_TOOLTIP: 'buscarInformacaoTooltip',
            ENVIAR_TEMPORARIA:'enviarTemporaria'
        },
        ENTRE_SETORES: {
            ATUALIZAR_ENTRE_SETORES: 'atualizarEntreSetores',
            BUSCAR_ENTRE_SETORES_POR_ID: 'buscarEntreSetoresPorId',
            FINALIZAR_ENTRE_SETORES: 'finalizarEntreSetores'
        },
        ENTRE_ESTOQUES: {
            ATUALIZAR_ENTRE_ESTOQUES: 'atualizarEntreEstoques',
            BUSCAR_ENTRE_ESTOQUES_POR_ID: 'buscarEntreEstoquesPorId',
            FINALIZAR_ENTRE_ESTOQUES: 'finalizarEntreEstoques'
        },
        DEVOLUCAO_ALMOXARIFADO: {
            ATUALIZAR_DEVOLUCAO_ALMOXARIFADO: 'atualizarDevolucaoAlmoxarifado',
            BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID: 'buscarDevolucaoAlmoxarifadoPorId',
            FINALIZAR_DEVOLUCAO_ALMOXARIFADO: 'finalizarDevolucaoAlmoxarifado'
        },
        DOCUMENTO: {
            CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA: 'cadastrarDocumentoMovimentacaoInterna',
            ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA: 'atualizarDocumentoMovimentacaoInterna',
            BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA: 'buscarDocumentosMovimentacaoInterna',
            BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA: 'buscarTipoDocumentoMovimentacaoInterna',
            DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA: 'deletarDocumentoMovimentacaoInterna'
        },
        RELATORIO: {
            GERAR_TERMO_DE_RESPONSABILDADE:'gerarTermoDeResponsabilida',
            GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO:'gerarMemorandoMovimentacaoEmElaboracao',
            GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA:'gerarMemorandoMovimentacaoFinalizada'
        },
        VISUALIZACAO: {
            BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO: 'buscarMovimentacaoInternaVisualizacaoRegistro',
            ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO: 'atualizarMovimentacaoInternaVisualizacaoRegistro',
            BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO: 'buscarTodosItensAdicionadosVisualizacaoRegistro',
            DEVOLUCAO_ITEM: {
                BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO: 'buscarTodosItensParaDevolucao',
                DEVOLVER_ITENS_PARCIAL: 'devolverItensParcial'
            }
        }
    },
    RESPONSAVEL: {
        BUSCAR_TODOS_RESPONSAVEIS: 'buscarTodosResponsaveis'
    },
    COMODATO: {
        BUSCAR_COMODANTES_DINAMICAMENTE: 'buscarComodantesDinamicamente'
    }
}
