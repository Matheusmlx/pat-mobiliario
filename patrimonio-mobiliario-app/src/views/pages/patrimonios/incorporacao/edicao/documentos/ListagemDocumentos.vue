<template>
    <v-container fluid>
        <documentos
            :permissao-edicao="verificaPermissaoEdicao"
            :dono-documento="'incorporacao'"
            :dono-documento-id="incorporacaoId"
            :actions="actions"
            :mutations="mutations"/>

        <acoes-botoes-finalizar-voltar :desabilitado="!verificaPermissaoEdicao || possuiItensNaoFinalizados"
                                       :em-processamento="incorporacaoEmProcessamento"
                                       texto-em-processamento="A incorporação está em processamento e não pode ser finalizada"
                                       :texto-bloqueio="retornaTextoBloqueio()"
                                       @finalizar="tratarEventoFinalizar"
                                       @voltar="tratarEventoVoltar"/>
    </v-container>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapState} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcoesBotoesFinalizarVoltar from '@/views/components/acoes/AcoesBotoesFinalizarVoltar'
    import Documentos from '@/views/components/documento/Documentos'

    export default {
        data() {
            return {
                documentos: [],
                tiposDocumento: [],
                possuiItensNaoFinalizados: false,
                documentoVazio: true,
                tituloAlerta: 'Tudo certo, incorporação em processamento',
                subTituloAlerta: 'Sua incorporação foi adicionada na fila para processamento e iremos lhe notificar assim que estiver terminada.',
                textoBotaoAlerta: 'Voltar para a listagem',
                actions: {
                    cadastrarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO,
                    atualizarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO,
                    enviarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD,
                    deletarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO,
                    buscarTipoDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO,
                    buscarDocumentos: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS,
                },
                mutations: {
                    removerDocumentos: mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS
                }
            }
        },
        components: {Documentos, AcoesBotoesFinalizarVoltar},
        async mounted() {
            await this.buscarItensIncorporacao()
        },
        computed: {
            ...mapState({situacaoIncorporacao: state => state.incorporacao.situacaoIncorporacao}),
            incorporacaoEmProcessamento() {
                return this.situacaoIncorporacao === 'EM_PROCESSAMENTO'
            },
            incorporacaoId() {
                return parseInt(this.$route.params.incorporacaoId)
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            },
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO
            ]),
            async tratarEventoFinalizar() {
                if (!this.possuiItensNaoFinalizados) {
                    try {
                        const dadosFinalizacao = await this.finalizarIncorporacao(this.incorporacaoId)
                        if (dadosFinalizacao.situacao === 'EM_PROCESSAMENTO') {
                            this.mostrarNotificacaoSucessoComConfirmacao({
                                titulo: this.tituloAlerta,
                                subTitulo: this.subTituloAlerta,
                                textoBotao: this.textoBotaoAlerta
                            }, (result) => {
                                if (result.isConfirmed) {
                                    this.$router.replace({name: 'IncorporacaoListagem'})
                                }
                            })
                        } else {
                            this.mostrarNotificacaoSucessoDefault()
                            await this.$router.replace({
                                name: 'VisualizarRegistroIncorporacao',
                                params: {incorporacaoId: this.incorporacaoId}
                            })
                        }
                    } catch (err) {
                        this.mostrarNotificacaoErro(err.response.data.message)
                    }
                }
            },
            async buscarItensIncorporacao() {
                const resultado = await this.buscarTodosItensIncorporacao(this.incorporacaoId)
                if (resultado) {
                    this.possuiItensNaoFinalizados = !!resultado.items.find(element => element.situacao === 'EM_ELABORACAO')
                }
            },
            tratarEventoVoltar() {
                if (this.verificaPermissaoEdicao) {
                    this.$router.replace({
                        name: 'ItensIncorporacaoListagem',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                } else {
                    this.$router.replace({
                        name: 'VisualizarItensIncorporacao',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                }
            },
            retornaTextoBloqueio() {
                if (!this.verificaPermissaoEdicao) {
                    return 'Você não possui permissão para finalizar.'
                }
                if (this.possuiItensNaoFinalizados) {
                    return 'Todos os itens do passo 2 devem estar finalizados.'
                }
                return ''
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .incorporacao-documentos
        min-height 62vh

</style>


