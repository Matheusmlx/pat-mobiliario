<template>
  <panel-documentos :anexo-upload="anexoUpload"
                    :doc-novo="docNovo"
                    :documentos="documentos"
                    :tipos-documento="tiposDocumento"
                    @criarNovoDocumento="tratarEventoCriarNovoDocumento"
                    @buscarTodosDocumentos="tratarEventoBuscarTodosDocumentos"
                    @buscarTipoDocumentos="tratarEventoBuscarTipoDocumentos"
                    @salvarDocumento="tratarEventoSalvarDocumento"
                    @editarDocumento="tratarEventoEditarDocumento"
                    @uploadAnexo="tratarEventoUploadAnexo"
                    @removerDocumento="tratarEventoRemoverDocumento"
                    @resetarDocumentoNovo="tratarEventoResetarDocumentoNovo"/>
</template>

<script>
    import { mapActions } from 'vuex'
    import { actionTypes } from '@/core/constants'
    import PanelDocumentos from '@/views/components/panel/panel-documentos/PanelDocumentos'

    export default {
        name: 'VisualizarRegistroIncorporacaoDocumentos',
        components: { PanelDocumentos },
        props: {
            incorporacaoId: {
                type: Number,
                required: true
            }
        },
        data () {
            return {
                documentos: [],
                tiposDocumento: [],
                docNovo: {},
                anexoUpload: '',
            }
        },
        watch: {
            async incorporacaoId() {
                this.documentos = await this.buscarDocumentos(this.incorporacaoId)
                this.docNovo = {}
                this.anexoUpload = ''
            }
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD,
            ]),
            tratarEventoCriarNovoDocumento () {
                this.docNovo = { incorporacao: this.incorporacaoId }
            },
            async tratarEventoBuscarTodosDocumentos () {
                this.documentos = await this.buscarDocumentos(this.incorporacaoId)
            },
            async tratarEventoBuscarTipoDocumentos () {
                this.tiposDocumento = await this.buscarTipoDocumento()
            },
            async tratarEventoSalvarDocumento (documento) {
                await this.cadastrarDocumento(documento)
                await this.tratarEventoBuscarTodosDocumentos()
            },
            async tratarEventoEditarDocumento (documento) {
                await this.atualizarDocumento(documento)
            },
            async tratarEventoUploadAnexo (anexo) {
                this.anexoUpload = await this.documentoUpload(anexo)
            },
            async tratarEventoRemoverDocumento (documento) {
                await this.deletarDocumento(documento)
            },
            tratarEventoResetarDocumentoNovo () {
                this.docNovo = {}
            },
        }
    }
</script>

<style lang="stylus" scoped>

.session-title
  font-size 15px
  font-weight bold
  color #777 !important

.documentos
  border solid 1px #e7e7e7 !important
  border-radius 5px !important
  box-shadow none

.documentos-panel
  border-radius 5px !important

  .v-expansion-panel-content__wrap
    padding 0 10px 16px !important

.documentos-panel-header
  border-bottom solid 1px #e7e7e7 !important
  background-color #F6F6F6
  border-radius 5px !important
  min-height 20px !important
  padding-bottom 10px
  padding-top 10px

.alturaRolagem
  overflow-y: auto

.larguraMinima
  padding 0px 16px 0px 16px !important
  width 204px

.tamanhoPermitidoClass
  color #a5a5a5
  margin-top -10px
  font-size 12px

.campo-input-edit
  .v-text-field
    .v-label
      color #777 !important
      font-weight bold
      font-size 13px
      overflow unset
      pointer-events auto
      top 0

    input:-webkit-autofill
      background-color white !important

    input
      font-size 13px
      color #888 !important

    .v-label--active
      -webkit-transform translateY(-18px)
      transform translateY(-18px)

    .v-select__selection--comma
      font-size 13px
      color #888 !important
</style>
