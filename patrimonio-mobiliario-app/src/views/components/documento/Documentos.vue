<template>
    <v-container fluid>
        <documentos-vazio
            v-if="documentos.length === 0"
            @novoDocumento="novoDocumento"/>

        <div v-else class="incorporacao-documentos pt-5">
            <div class="d-flex pr-5 justify-end">
                <v-btn
                    :disabled="!permissaoEdicao"
                    color="primary"
                    depressed
                    @click="novoDocumento">
                    Novo
                </v-btn>
            </div>
            <div :key="documento.id" v-for="(documento, index) in documentos">
                <documentos-item
                    v-model="documentos[index]"
                    :tipoDocumento="tiposDocumento"
                    :index="index"
                    :permissao-edicao="permissaoEdicao"
                    :config-botao-remover="configBotaoRemover"
                    class="ml-9"
                    @removerDocumento="tratarEventoRemoverDocumento"
                    @salvar="tratarEventoSalvar"
                    @salvarUri="tratarEventoSalvarUri(documentos[index],index)"/>
            </div>
        </div>
    </v-container>
</template>

<script>
    import DocumentosVazio from './DocumentosVazio'
    import DocumentosItem from './DocumentosItem'

    export default {
        data() {
            return {
                documentos: [],
                tiposDocumento: [],
                documentoVazio: true
            }
        },
        components: {DocumentosItem, DocumentosVazio},
        props: {
            permissaoEdicao: {
                type: Boolean,
                required: true
            },
            configBotaoRemover: {
                type: Object
            },
            donoDocumento: {
                type: String,
                required: true
            },
            donoDocumentoId: {
                type: Number,
                required: true
            },
            actions: {
                type: Object,
                required: true
            },
            mutations: {
                type: Object,
                required: true
            }
        },
        async mounted() {
            await this.buscarTipoDocumentos()
            await this.buscarDocumentosDono()
        },
        methods: {
            async buscarTipoDocumento() {
                return await this.$store.dispatch(this.actions.buscarTipoDocumento)
            },
            async buscarDocumentos(donoDocumentoId) {
                return await this.$store.dispatch(this.actions.buscarDocumentos, donoDocumentoId)
            },
            async cadastrarDocumento(doc) {
                await this.$store.dispatch(this.actions.cadastrarDocumento, doc)
            },
            async atualizarDocumento(doc) {
                await this.$store.dispatch(this.actions.atualizarDocumento, doc)
            },
            async enviarDocumento(doc) {
                return await this.$store.dispatch(this.actions.enviarDocumento, doc)
            },
            async deletarDocumento(doc) {
                await this.$store.dispatch(this.actions.deletarDocumento, doc)
            },
            async removerDocumentos(doc) {
                await this.$store.commit(this.mutations.removerDocumentos, doc)
            },
            async tratarEventoSalvarUri(documento, index) {
                this.desabilitarLoadingGlobal()
                if (documento.uriAnexo) {
                    this.documentos[index].uriAnexo = await this.enviarDocumento(documento)
                }
                this.habilitarLoadingGlobal()
            },
            async novoDocumento() {
                if (this.quantidadeDeDocumentosExcedida()) {
                    this.mostrarNotificacaoErro('A quantidade máxima de documentos cadastrados foi atingida.')
                } else {
                    if (this.validarCadastrosFinalizados()) {
                        this.documentos.push({
                            [this.donoDocumento]: this.donoDocumentoId,
                            numero: null,
                            valor: null,
                            tipo: null,
                            data: null,
                            uriAnexo: null
                        })
                        this.documentoVazio = false
                    } else {
                        this.mostrarNotificacaoAviso('Finalize o cadastro para cadastrar novos documentos')
                    }
                }
            },
            quantidadeDeDocumentosExcedida() {
                return this.documentos.length === 30
            },
            validarCadastrosFinalizados() {
                return this.documentoVazio
            },
            async tratarEventoRemoverDocumento(documento) {
                if (typeof documento.numero != 'undefined' && typeof documento.id != 'undefined') {
                    await this.deletarDocumento(documento)
                } else {
                    this.removerDocumentos(documento)
                    this.documentoVazio = true
                }
            },
            async buscarTipoDocumentos() {
                const resultado = await this.buscarTipoDocumento()
                if (resultado) {
                    this.tiposDocumento = this.ordenaTipoDocumentos(resultado)
                }
            },
            ordenaTipoDocumentos(resultado) {
                return resultado.sort(function (a, b) {
                    if (a.descricao < b.descricao) {
                        return -1
                    } else if (a.descricao > b.descricao) {
                        return 1
                    } else {
                        return 0
                    }
                })
            },
            async buscarDocumentosDono() {
                this.documentos = await this.buscarDocumentos(this.donoDocumentoId)
            },
            async tratarEventoSalvar(documento) {
                this.desabilitarLoadingGlobal()
                if (documento.valor === 0) {
                    documento.valor = null
                }
                if (typeof documento.id == 'undefined') {
                    await this.cadastrarDocumento(documento)
                    this.documentoVazio = true
                    this.buscarDocumentosDono()
                } else {
                    await this.atualizarDocumento(documento)
                    this.documentoVazio = true
                }
                this.habilitarLoadingGlobal()
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .incorporacao-documentos
        min-height 62vh

</style>


