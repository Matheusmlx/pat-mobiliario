<template>
    <v-container fluid>
        <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5 campo-input-edit">
            <v-col cols="12">
                <v-card class="documentos">
                    <v-expansion-panels active-class="" flat>
                        <v-expansion-panel>
                            <v-expansion-panel-header @click="abrirPanelDocumentos" class="documentos-panel-header">
                                <h4 class="session-title">Documentos</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-col cols="12">
                                    <v-btn disabled
                                           class="btn-disabled"
                                           color="grey" depressed>NOVO</v-btn>
                                </v-col>
                                <v-col cols="12" md="12" class="mt-0 pt-0">
                                    <v-simple-table class="simple-table">
                                        <thead>
                                        <tr>
                                            <th :key="titulo.text" class="font-weight-bold"
                                                v-for="titulo in documentosHeaders">
                                                {{getNomeRotulosPersonalizados(titulo.value, nomeTela)}}
                                            </th>
                                        </tr>
                                        </thead>

                                        <tbody id="listagemTabela">
                                        <tr :key="documento.id" v-for="(documento) in documentos">
                                            <td style="width: 15vw">
                                                <campo-de-texto-editavel
                                                    v-model="documento.numero"
                                                    disabled
                                                    name="Numero"
                                                    maxlength="50"
                                                    :counter="50"
                                                    placeholder="Informe o número"/>
                                            </td>
                                            <td>
                                                <campo-de-data-editavel
                                                    v-model="documento.data"
                                                    disabled
                                                    name="Data NL"/>
                                            </td>

                                            <td>
                                                <campo-de-valor-editavel
                                                    class="ma-0 pa-0"
                                                    v-model="documento.valor"
                                                    disabled
                                                    :validation-field="validateRequeridAzMoney"
                                                    name="Valor"
                                                    placeholder=" "/>
                                            </td>

                                            <td class="pb-5">
                                                <campo-de-select-editavel
                                                    v-model="documento.tipo"
                                                    disabled
                                                    name="Tipo"
                                                    :items="tiposDocumento"
                                                    placeholder=" "
                                                    no-data-text="Não há tipos com esse nome"/>
                                            </td>

                                            <td class="larguraMinima">
                                                <campo-de-arquivo-editavel
                                                    v-model="documento.uriAnexo"
                                                    :value="documento"
                                                    disabled
                                                    name="Anexo"
                                                    class="mt-4"
                                                    placeholder=" "/>
                                            </td>
                                            <botao-remover
                                                :color="'grey'"
                                                :stylus="'btn-disabled'"
                                                desabilitado
                                                icon="fas fa-minus-circle"/>
                                        </tr>
                                        </tbody>
                                    </v-simple-table>
                                </v-col>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import CampoDeTextoEditavel from '@/views/components/camposEditaveis/campo-de-texto-editavel'
    import CampoDeDataEditavel from '@/views/components/camposEditaveis/campo-de-data-editavel'
    import CampoDeValorEditavel from '@/views/components/camposEditaveis/campo-de-valor-editavel'
    import CampoDeArquivoEditavel from '@/views/components/camposEditaveis/campo-de-arquivo-editavel'
    import CampoDeSelectEditavel from '@/views/components/camposEditaveis/campo-de-select-editavel'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import {actionTypes} from '@/core/constants'
    import {mapActions, createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'VisualizarRegistroIncorporacaoDocumentosVisualizar',
        components: {
            CampoDeTextoEditavel,
            CampoDeDataEditavel,
            CampoDeValorEditavel,
            CampoDeArquivoEditavel,
            CampoDeSelectEditavel,
            BotaoRemover
        },
        data() {
            return {
                documentosHeaders: [
                    {value: 'numero'},
                    {value: 'data'},
                    {value: 'valor'},
                    {value: 'tipo'},
                    {value: 'uriAnexo'},
                ],
                documentos: [],
                tiposDocumento: [],
                docNovo: {},
                adicionando: false,
                estaEditando: [],
                validateRequeridAzMoney: 0,
                documentoObrigatorioId: null,
                incorporacaoId: null,
                nomeTela: 'DOCUMENTOS'
            }
        },
        async mounted() {
            this.setIncorporacaoId()
            await this.buscarTipoDocumentos()
            await this.buscarTodosDocumentos()
        },
        computed: {
            ...mapGetters([
                'getNomeRotulosPersonalizados',
            ]),
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS,
                actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO,
            ]),
            setaIdObrigatorio(id) {
                this.documentoObrigatorioId = id
            },
            abrirPanelDocumentos() {
                setTimeout(() => {
                    let options = {duration: 945, offset: -1, easing: 'linear',}
                    this.$vuetify.goTo('#listagemTabela', options)
                }, 250)
            },
            setIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            },
            async buscarTodosDocumentos() {
                this.documentos = await this.buscarDocumentos(this.incorporacaoId)
            },
            async buscarTipoDocumentos() {
                this.tiposDocumento = await this.buscarTipoDocumento()
            },
        }
    }
</script>

<style lang="stylus">

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
        padding 0px 16px 10px 16px !important
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
