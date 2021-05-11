<template>
    <v-container fluid>
        <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5 campo-input-edit">
            <v-col cols="12">
                <v-card flat class="documentos">
                    <v-expansion-panels active-class="" flat>
                        <v-expansion-panel>
                            <v-expansion-panel-header @click="abrirPanelDocumentos" class="documentos-panel-header">
                                <h4 class="session-title">Documentos</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-col cols="12">
                                    <v-btn
                                           @click="novoDocumento()|abrirPanelDocumentos()"
                                           color="grey" dark depressed>
                                        NOVO
                                    </v-btn>
                                </v-col>
                                <v-col cols="12" md="12" class="mt-0 pt-0">
                                    <v-simple-table class="simple-table">
                                        <thead>
                                        <tr>
                                            <th :key="titulo.text" class="font-weight-bold"
                                                v-for="titulo in documentosHeaders">
                                                {{getNomeRotulosPersonalizados(titulo.value,nomeTela)}}
                                                <span v-if="titulo.obrigatorio" class="ml-1 red--text">*</span>
                                            </th>
                                        </tr>
                                        </thead>

                                        <tbody id="listagemTabela">
                                        <tr :key="documento.id" v-for="(documento) in documentos">
                                            <td style="width: 15vw">
                                                <campo-de-texto-editavel
                                                    v-model="documento.numero"
                                                    :idObjeto="documento.id"
                                                    :estaAdicionando="adicionando || estaEditando && documento.id !== documentoObrigatorioId"
                                                    :name="'Numero'"
                                                    maxlength="50"
                                                    :counter="50"
                                                    :validate="!adicionando ? 'required' : ''"
                                                    placeholder="Informe o número"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando($event, documento)"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>
                                            <td>
                                                <campo-de-data-editavel
                                                    v-model="documento.data"
                                                    :idObjeto="documento.id"
                                                    :estaAdicionando="adicionando || estaEditando && documento.id !== documentoObrigatorioId"
                                                    :required="(!adicionando && valorDataObrigatorio) ||
                                                    getObrigatorioRotulosPersonalizados('data', nomeTela)"
                                                    :editar="!!(!adicionando && valorDataObrigatorio && documento.id === documentoObrigatorioId)"
                                                    :name="'Data NL'"
                                                    @verificaObrigatoriedade="verificarCamposObrigatorios(documento)"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando($event, documento)"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td>
                                                <campo-de-valor-editavel
                                                    class="ma-0 pa-0"
                                                    v-model="documento.valor"
                                                    :idObjeto="documento.id"
                                                    :estaAdicionando="adicionando || estaEditando && documento.id !== documentoObrigatorioId"
                                                    :required="(!adicionando && valorDataObrigatorio) ||
                                                    getObrigatorioRotulosPersonalizados('valor', nomeTela)"
                                                    :validation-field="validateRequeridAzMoney"
                                                    :editar="!!(!adicionando && valorDataObrigatorio && documento.id === documentoObrigatorioId)"
                                                    :name="'valor'+documento.id"
                                                    placeholder="Informe o valor"
                                                    @verificaObrigatoriedade="verificarCamposObrigatorios(documento)"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando($event, documento)"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td>
                                                <campo-de-select-editavel
                                                    v-model="documento.tipo"
                                                    :idObjeto="documento.id"
                                                    :estaAdicionando="adicionando || estaEditando && documento.id !== documentoObrigatorioId"
                                                    :name="'Tipo'"
                                                    :items="tiposDocumento"
                                                    placeholder="Selecione"
                                                    no-data-text="Não há tipos com esse nome"
                                                    :validate="!adicionando ? 'required' : ''"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando($event, documento)"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td class="larguraMinima">
                                                <campo-de-arquivo-editavel
                                                    v-model="documento.uriAnexo"
                                                    :value="documento"
                                                    :idObjeto="documento.id"
                                                    :estaAdicionando="adicionando || estaEditando && documento.id !== documentoObrigatorioId"
                                                    :validate="!adicionando &&
                                                    getObrigatorioRotulosPersonalizados('uriAnexo', nomeTela) ? 'required' : ''"
                                                    :name="'uriAnexo'"
                                                    placeholder="Selecione o Arquivo"
                                                    @input="tratarEventoSalvarAnexo(documento)"
                                                    @setaEditando="setaEditando($event, documento)"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>
                                            <botao-remover
                                                :color="'grey'"
                                                @remover="tratarEventoDeletarDocumento(documento)"
                                                icon="fas fa-minus-circle"/>
                                        </tr>

                                        <tr v-if="adicionando">
                                            <td>
                                                <v-text-field
                                                    v-model="docNovo.numero"
                                                    v-validate="'required'"
                                                    maxlength="50"
                                                    name="Numero"
                                                    :counter="50"
                                                    placeholder="Informe o número"
                                                    :error-messages="errors.collect('Numero')"
                                                    @blur="tratarEventoSalvar(docNovo)"/>
                                            </td>
                                            <td>
                                                <az-date
                                                    v-model="docNovo.data"
                                                    :is-required="valorDataObrigatorio ||
                                                    getObrigatorioRotulosPersonalizados('data', nomeTela)"
                                                    date
                                                    placeholderDate="__/__/____"
                                                    v-mask="'##/##/####'"
                                                    name-date="Data"
                                                    @input="tratarEventoSalvar(docNovo)"/>
                                            </td>
                                            <td @focusout="tratarEventoSalvar(docNovo)">
                                                <az-money
                                                    v-model="docNovo.valor"
                                                    :maxLength="18"
                                                    :name="'valor'+validateRequeridAzMoney"
                                                    :required="valorDataObrigatorio ||
                                                    getObrigatorioRotulosPersonalizados('valor', nomeTela)"
                                                    requeridMessage="O campo é obrigatório"
                                                    :validation-field="validateRequeridAzMoney"
                                                    placeholder="Informe o valor"/>
                                            </td>
                                            <td>
                                                <v-autocomplete
                                                    v-model="docNovo.tipo"
                                                    :items="tiposDocumento"
                                                    item-text="descricao"
                                                    item-value="id"
                                                    name="Tipo"
                                                    no-data-text="Não há tipos com esse nome"
                                                    placeholder="Selecione"
                                                    :error-messages="errors.collect('Tipo')"
                                                    v-validate="'required'"
                                                    @input="tratarEventoSalvar(docNovo)"/>
                                            </td>
                                            <td class="pb-2">
                                                <campo-de-arquivo-novo
                                                    v-model="docNovo.uriAnexo"
                                                    :value="docNovo"
                                                    :validate="getObrigatorioRotulosPersonalizados('uriAnexo', nomeTela) ? 'required' : ''"
                                                    name="uriAnexo"
                                                    placeholder="Selecione o Arquivo"
                                                    @input="tratarEventoSalvarAnexo(docNovo)"/>
                                            </td>
                                            <botao-remover
                                                :color="'grey'"
                                                class="mt-5"
                                                @remover="tratarEventoDeletarDocumentoNovo"
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
    import CampoDeArquivoNovo from '@/views/components/camposEditaveis/campo-de-arquivo-novo'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'PanelDocumentos',
        components: {
            CampoDeTextoEditavel,
            CampoDeDataEditavel,
            CampoDeValorEditavel,
            CampoDeArquivoEditavel,
            CampoDeSelectEditavel,
            CampoDeArquivoNovo,
            BotaoRemover
        },
        props:['anexoUpload','documentos','tiposDocumento','docNovo'],
        data() {
            return {
                documentosHeaders: [
                    {value: 'numero', obrigatorio: false},
                    {value: 'data', obrigatorio: false},
                    {value: 'valor', obrigatorio: false},
                    {value: 'tipo', obrigatorio: false},
                    {value: 'uriAnexo', obrigatorio: false},
                ],
                adicionando: false,
                editando: [],
                valorDataObrigatorio: false,
                validateRequeridAzMoney: 0,
                documentoObrigatorioId: null,
                nomeTela: 'DOCUMENTOS',
                anexo:{}
            }
        },
        async mounted() {
            await this.buscarTipoDocumentos()
            await this.buscarTodosDocumentos()
        },
        computed: {
            ...mapGetters([
                'getNomeRotulosPersonalizados',
                'getObrigatorioRotulosPersonalizados',
                'getDocumentoTodosObrigatoriosValidado',
                'getDocumentoNumeroTipoObrigatorioValidado'
            ]),
            estaEditando(){
                return this.editando.length !== 0
            }
        },
        watch:{
            async anexoUpload(val){
                this.anexo.uriAnexo = val
                await this.tratarEventoSalvar(this.anexo)
            }
        },
        methods: {
            setaIdObrigatorio(id) {
                this.documentoObrigatorioId = id
            },
            async verificarCamposObrigatorios(doc) {
                const documentoEncontrado = this.tiposDocumento.find(element => element.id === doc.tipo)

                if (documentoEncontrado &&
                    (documentoEncontrado.descricao === 'Nota Fiscal' || documentoEncontrado.descricao === 'Nota de Empenho')) {
                    this.valorDataObrigatorio = true
                    this.validateRequeridAzMoney++
                    await this.$validator._base.validateAll()
                } else {
                    this.valorDataObrigatorio = false
                }

                return true
            },
            setaEditando(value, documento) {
                this.editando.push(value)
                this.setaIdObrigatorio(documento.id)
                this.verificarCamposObrigatorios(documento)
                this.verificaObrigatoriedadeLabels()
            },
            retiraEditando(value) {
                const index = this.editando.indexOf(value)
                this.editando.splice(index, 1)
                this.verificaObrigatoriedadeLabels()
            },
            abrirPanelDocumentos() {
                setTimeout(() => {
                    let options = {duration: 945, offset: -1, easing: 'linear',}
                    this.$vuetify.goTo('#listagemTabela', options)
                }, 250)
            },
            async novoDocumento() {
                if (!this.validaSeEstaEditando()) {
                    if (this.validarCadastrosFinalizados(this.docNovo)) {
                        this.validateRequeridAzMoney = 0
                        this.valorDataObrigatorio = false
                        this.adicionando = true
                        await this.$emit('criarNovoDocumento')
                    } else {
                        this.mostrarNotificacaoAviso('Finalize o cadastro para cadastrar novos documentos')
                    }
                }
                this.verificaObrigatoriedadeLabels()
            },
            validarCadastrosFinalizados(obj) {
                return Object.keys(obj).length === 0
            },
            validaSeEstaEditando() {
                if (this.estaEditando) {
                    this.mostrarNotificacaoAviso('Finalize a edição do documento antes de criar um novo')
                    return true
                }
                return false
            },
            estaAdicionando() {
                this.mostrarNotificacaoAviso('Finalize o cadastro para realizar a edição')
            },
            async buscarTodosDocumentos() {
                await this.$emit('buscarTodosDocumentos')
            },
            async buscarTipoDocumentos() {
                await this.$emit('buscarTipoDocumentos')
                this.tiposDocumento = this.ordenaTipoDocumentos(this.tiposDocumento)
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
            verificaObrigatoriedadeLabels(){
                if(this.estaEditando || this.adicionando){
                    if(this.valorDataObrigatorio){
                        this.apresentaObrigatoriedadeLabelsExcetoUriAnexo()
                    }else if(!this.valorDataObrigatorio){
                        this.apresentaObrigatoriedadeLabelsNumeroTipo()
                        this.verificaObrigatoriedadeLabelValor()
                        this.verificaObrigatoriedadeLabelData()
                    }
                    this.verificaObrigatoriedadeLabelUriAnexo()
                }else{
                    this.escondeObrigatoriedadeLabels()
                }
            },
            apresentaObrigatoriedadeLabelsExcetoUriAnexo(){
                this.documentosHeaders[0].obrigatorio = true
                this.documentosHeaders[1].obrigatorio = true
                this.documentosHeaders[2].obrigatorio = true
                this.documentosHeaders[3].obrigatorio = true
            },
            apresentaObrigatoriedadeLabelsNumeroTipo(){
                this.documentosHeaders[0].obrigatorio = true
                this.documentosHeaders[3].obrigatorio = true
            },
            verificaObrigatoriedadeLabelData(){
                this.documentosHeaders[1].obrigatorio = !!this.getObrigatorioRotulosPersonalizados('data', this.nomeTela)
            },
            verificaObrigatoriedadeLabelValor(){
                this.documentosHeaders[2].obrigatorio = !!this.getObrigatorioRotulosPersonalizados('valor', this.nomeTela)
            },
            verificaObrigatoriedadeLabelUriAnexo(){
                this.documentosHeaders[4].obrigatorio = !!this.getObrigatorioRotulosPersonalizados('uriAnexo', this.nomeTela)
            },
            escondeObrigatoriedadeLabels(){
                this.documentosHeaders[0].obrigatorio = false
                this.documentosHeaders[1].obrigatorio = false
                this.documentosHeaders[2].obrigatorio = false
                this.documentosHeaders[3].obrigatorio = false
                this.documentosHeaders[4].obrigatorio = false
            },
            async tratarEventoSalvar(documento) {
                if (this.getObrigatorioRotulosPersonalizados('valor', this.nomeTela)){
                    this.validateRequeridAzMoney++
                }
                if (documento.valor === undefined || documento.valor === 0) {
                    this.docNovo.valor = null
                }

                this.setaIdObrigatorio(documento.id)

                if (await this.verificarCamposObrigatorios(documento)) {
                    this.verificaObrigatoriedadeLabels()
                    documento = this.retiraEspacosVazios(documento)

                    this.desabilitarLoadingGlobal()

                    const estaValidado =  await this.$validator._base.validateAll()

                    if (estaValidado && this.verificarValidacaoCamposObrigatorios(documento)) {
                        if (typeof documento.id == 'undefined') {
                            await this.$emit('salvarDocumento', documento)
                            this.$emit('resetarDocumentoNovo')
                            this.adicionando = false
                            this.verificaObrigatoriedadeLabels()
                        } else {
                            await this.$emit('editarDocumento', documento)
                        }
                    }
                    this.habilitarLoadingGlobal()
                }
            },
            verificarValidacaoCamposObrigatorios(documento) {
                return this.valorDataObrigatorio
                    ? (this.getDocumentoTodosObrigatoriosValidado(documento, this.nomeTela))
                    : (this.getDocumentoNumeroTipoObrigatorioValidado(documento, this.nomeTela))
            },
            retiraEspacosVazios(documento) {
                if (documento.numero) {
                    documento.numero = documento.numero.trim()
                }
                return documento
            },
            async tratarEventoSalvarAnexo(anexo) {
                this.desabilitarLoadingGlobal()
                if (anexo.uriAnexo) {
                    this.anexo = anexo
                    await this.$emit('uploadAnexo', this.anexo)
                }
                this.habilitarLoadingGlobal()
            },
            async tratarEventoDeletarDocumento(documento) {
                this.desabilitarLoadingGlobal()
                const estaEditandoDocumento = this.editando.find(element => element.id === documento.id)
                if(estaEditandoDocumento) this.retiraEditando(estaEditandoDocumento)
                await this.$emit('removerDocumento', documento)
                this.habilitarLoadingGlobal()
                this.verificaObrigatoriedadeLabels()
            },
            async tratarEventoDeletarDocumentoNovo() {
                this.adicionando = false
                this.$emit('resetarDocumentoNovo')
                this.verificaObrigatoriedadeLabels()
            }
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
