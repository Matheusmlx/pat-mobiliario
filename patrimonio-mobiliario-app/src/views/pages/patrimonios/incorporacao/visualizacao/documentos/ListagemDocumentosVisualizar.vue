<template>
    <v-container fluid>
        <listagem-documentos-vazia v-if="documentos.length === 0"/>

        <div v-else class="incorporacao-documentos pt-5">
            <div class="d-flex pr-5 justify-end">
                <v-btn
                    disabled
                    class="btn-disabled"
                    color="primary"
                    depressed>
                    Novo
                </v-btn>
            </div>
            <div :key="documento.id" v-for="(documento, index) in documentos">
                <listagem-documentos-item
                    v-model="documentos[index]"
                    :tipoDocumento="tiposDocumento"
                    :index="index"
                    class="ml-9"
                    @baixarAnexo="baixarAnexo"
                  />
            </div>
        </div>

        <listagem-documentos-acoes :patrimonio="patrimonio" @voltar="tratarEventoVoltar"/>
    </v-container>
</template>

<script>
    import ListagemDocumentosAcoes from './ListagemDocumentosAcoesVisualizar'
    import ListagemDocumentosVazia from './ListagemDocumentosVaziaVisualizar'
    import ListagemDocumentosItem from './ListagemDocumentosItemVisualizar'
    import {actionTypes} from '@/core/constants'
    import {mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        data() {
            return {
                documentos: [],
                tiposDocumento: [],
                patrimonio: {},
                documentoVazio: true,
                showDesfazer: false,
                incorporacaoId: null,
            }
        },
        components: {ListagemDocumentosAcoes, ListagemDocumentosItem, ListagemDocumentosVazia},
        async mounted() {
            this.setIncorporacaoId()
            await this.buscarTipoDocumentos()
            await this.buscarDocumentosIncorporacao()
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            },
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS
            ]),
            async baixarAnexo(anexo) {
                await this.documentoDownload(anexo)
            },
            async buscarTipoDocumentos() {
                const resultado = await this.buscarTipoDocumento()
                if(resultado) {
                    this.tiposDocumento = this.ordenaTipoDocumentos(resultado)
                }
            },
            ordenaTipoDocumentos(resultado){
                return resultado.sort(function(a,b) {
                    if(a.descricao < b.descricao){
                        return -1
                    }else if(a.descricao > b.descricao){
                        return 1
                    }
                    return 0
                })
            },
            async buscarDocumentosIncorporacao() {
                this.documentos = await this.buscarDocumentos(this.incorporacaoId)
            },
            setIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            },
            tratarEventoVoltar() {
                if (this.verificaPermissaoEdicao) {
                    this.$router.push({
                        name: 'ItensIncorporacaoListagem',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                } else{
                    this.$router.push({
                        name: 'VisualizarItensIncorporacao',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                }
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .incorporacao-documentos
        min-height 62vh
</style>


