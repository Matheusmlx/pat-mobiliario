<template>
    <div class="movimentacao-edicao-documentos">
        <documentos
            :permissao-edicao="verificaPermissaoEdicao"
            :dono-documento="'movimentacao'"
            :dono-documento-id="movimentacaoInternaId"
            :actions="actions"
            :mutations="mutations"/>

        <acoes-botoes-finalizar-voltar :desabilitado="!verificaPermissaoEdicao"
                                       :em-processamento="movimentacaoEmProcessamento"
                                       :textoButton="retornarTextoButton()"
                                       texto-em-processamento="A movimentação está em processamento e não pode ser finalizada"
                                       texto-bloqueio="Você não possui permissão para finalizar."
                                       @finalizar="tratarEventoFinalizar"
                                       @voltar="tratarEventoVoltar"/>
    </div>
</template>

<script>

    import {mapActions, mapState} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import Documentos from '@/views/components/documento/Documentos'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcoesBotoesFinalizarVoltar from '@/views/components/acoes/AcoesBotoesFinalizarVoltar'

    export default {
        name: 'MovimentacaoInternaEdicaoDocumentos',
        components: {Documentos, AcoesBotoesFinalizarVoltar},
        data() {
            return {
                tipoMovimentacao: null,
                actions: {
                    cadastrarDocumento: actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA,
                    atualizarDocumento: actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA,
                    enviarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD,
                    deletarDocumento: actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA,
                    buscarTipoDocumento: actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA,
                    buscarDocumentos: actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA,
                },
                mutations: {
                    removerDocumentos: mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.REMOVER_DOCUMENTO_MOVIMENTACAO_INTERNA
                }
            }
        },
        async mounted() {
            await this.buscarTipoMovimentacao()
        },
        computed: {
            ...mapState({situacaoMovimentacao: state => state.movimentacaointerna.situacaoMovimentacaoInterna}),
            movimentacaoEmProcessamento() {
                return this.situacaoMovimentacao === 'EM_PROCESSAMENTO'
            },
            movimentacaoInternaId() {
                return parseInt(this.$route.params.movimentacaoInternaId)
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO,
                actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES,
                actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES,
                actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO
            ]),
            async buscarTipoMovimentacao() {
                this.tipoMovimentacao = await this.buscarTipoMovimentacaoInterna(this.movimentacaoInternaId)
            },
            retornarTextoButton() {
                if (this.tipoMovimentacao === 'TEMPORARIA') {
                    return 'Enviar'
                }
                return 'Finalizar'
            },
            tratarEventoVoltar() {
                if (this.verificaPermissaoEdicao) {
                    this.$router.push({
                        name: 'MovimentacaoInternaEdicaoItens',
                        params: {movimentacaoInternaId: this.movimentacaoInternaId},
                    })
                } else {
                    this.$router.push({
                        name: 'IncorporacaoListagem'
                    })
                }
            },
            async tratarEventoFinalizar() {
                let dadosFinalizacao = null
                if (this.tipoMovimentacao === 'DISTRIBUICAO') {
                    dadosFinalizacao = await this.finalizarDistribuicao(this.movimentacaoInternaId)
                } else if (this.tipoMovimentacao === 'ENTRE_SETORES') {
                    dadosFinalizacao = await this.finalizarEntreSetores(this.movimentacaoInternaId)
                } else if (this.tipoMovimentacao === 'ENTRE_ESTOQUES') {
                    dadosFinalizacao = await this.finalizarEntreEstoques(this.movimentacaoInternaId)
                } else if (this.tipoMovimentacao === 'DEVOLUCAO_ALMOXARIFADO') {
                    dadosFinalizacao = await this.finalizarDevolucaoAlmoxarifado(this.movimentacaoInternaId)
                } else if (this.tipoMovimentacao === 'TEMPORARIA') {
                    dadosFinalizacao = await this.enviarTemporaria(this.movimentacaoInternaId)
                }
                if (dadosFinalizacao && dadosFinalizacao.situacao) {
                    await this.tratarRedirecionamentoAposFinalizacao(dadosFinalizacao)
                }
            },
            async tratarRedirecionamentoAposFinalizacao(dadosFinalizacao) {
                if (dadosFinalizacao.situacao === 'EM_PROCESSAMENTO') {
                    if (this.tipoMovimentacao === 'DISTRIBUICAO') {
                        this.redirecionarMovimentacaoEmProcessamento('distribuição')
                    } else if (this.tipoMovimentacao === 'ENTRE_SETORES') {
                        this.redirecionarMovimentacaoEmProcessamento('movimentação entre setores')
                    } else if (this.tipoMovimentacao === 'ENTRE_ESTOQUES') {
                        this.redirecionarMovimentacaoEmProcessamento('movimentação entre estoques')
                    } else if (this.tipoMovimentacao === 'DEVOLUCAO_ALMOXARIFADO') {
                        this.redirecionarMovimentacaoEmProcessamento('devolução para almoxarifado')
                    } else if (this.tipoMovimentacao === 'TEMPORARIA') {
                        this.redirecionarMovimentacaoEmProcessamento('movimentação temporária')
                    }
                } else {
                    this.mostrarNotificacaoSucessoDefault()
                    await this.$router.replace({
                        name: 'VisualizarRegistroMovimentacaoInterna',
                        params: {movimentacaoInternaId: this.movimentacaoInternaId}
                    })
                }
            },
            redirecionarMovimentacaoEmProcessamento(tipoMovimentacao) {
                this.mostrarNotificacaoSucessoComConfirmacao({
                                                                 titulo: 'Tudo certo, ' + tipoMovimentacao + ' em processamento',
                                                                 subTitulo: 'Sua ' + tipoMovimentacao + ' foi adicionada na fila para processamento e iremos ' +
                                                                     'lhe notificar assim que estiver terminada.',
                                                                 textoBotao: 'Voltar para a listagem',
                                                             },
                                                             (result) => {
                                                                 if (result.isConfirmed) {
                                                                     this.$router.replace({name: 'MovimentacaoInternaListagem'})
                                                                 }
                                                             })
            }
        }
    }
</script>

<style scoped lang="stylus">
    .movimentacao-edicao-documentos
        min-height 70vh

    .movimentacao-interna-documentos
        min-height 62vh
</style>
