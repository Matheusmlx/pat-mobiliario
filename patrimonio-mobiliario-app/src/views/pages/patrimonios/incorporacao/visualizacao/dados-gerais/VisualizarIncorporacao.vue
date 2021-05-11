<template>
    <div>
        <v-form class="az-form-content pb-0" ref="form">
            <v-container class="white form-dados-gerais-visualizacao" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.reconhecimento"
                            disabled
                            :placeholder="' '"
                            name="reconhecimento"
                            :items="reconhecimentos"
                            item-text="nome"
                            item-value="id"
                            :filter="filtroComboAutoComplete">
                            <template v-slot:label>
                                <label-personalizado campo="reconhecimento" :tela="nomeTela" apresentaTooltip :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.numProcesso"
                            disabled
                            :placeholder="' '"
                            name="número do processo"
                            maxlength="30">
                            <template v-slot:label>
                                <label-personalizado campo="numProcesso" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            style="max-height:35px;"
                            v-model="dadosDeEntrada.fornecedor"
                            disabled
                            :placeholder="' '"
                            :items="fornecedores"
                            :filter="filtroAutocompleteFornecedores"
                            item-text="cpfCnpj"
                            item-value="id"
                            name="CNPJ Fornecedor">
                            <template v-slot:label>
                                <label-personalizado campo="fornecedor" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-autocomplete>
                        <az-text-view
                            :text="dadosDeEntrada.fornecedor ? $options.filters.fornecedorFilter(dadosDeEntrada.fornecedor, fornecedores) : ''" class="mt-2"/>
                    </v-col>
                </v-row>
                <div class="empenhoBorder">
                    <empenho v-for="(empenho, index) in empenhos" :key="empenho.id"
                             :value="empenho"
                             :quantidadeEmpenhos="empenhos.length"
                             :primeiroEmpenho="index === 0"
                             :obrigatorio="empenhoObrigatorio"
                             :status-reconhecimento="!dadosDeEntrada.reconhecimento"
                             @buscarEmpenhos="buscaEmpenhos"/>
                </div>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.nota"
                            disabled
                            :placeholder="' '"
                            name="número da nota">
                            <template v-slot:label>
                                <label-personalizado campo="nota" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataNota"
                            is-disabled
                            :placeholderDate="'__/__/____'"
                            name-date="data nota"
                            date>
                            <template v-slot:label-date>
                                <label-personalizado campo="dataNota" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-money
                            v-if="dadosDeEntrada.id"
                            v-model="dadosDeEntrada.valorNota"
                            disabled
                            :placeholder="' '"
                            class="botaoExcluirMoney">
                            <template v-slot:label>
                                <label-personalizado campo="valorNota" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </az-money>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.orgao"
                            disabled
                            :placeholder="' '"
                            item-text="descricao"
                            item-value="id"
                            name="orgaoResponsavel"
                            :items="orgaos"
                            :filter="filtroComboAutoComplete">
                            <template v-slot:label>
                                <label-personalizado campo="orgao" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </v-autocomplete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.setor"
                            disabled
                            :placeholder="' '"
                            item-text="descricao"
                            item-value="id"
                            name="setor"
                            :items="setores">
                            <template v-slot:label>
                                <label-personalizado campo="setor" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao apresentaTooltip/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataRecebimento"
                            is-disabled
                            :placeholderDate="'__/__/____'"
                            name-date="recebimento"
                            date>
                            <template v-slot:label-date>
                                <label-personalizado campo="dataRecebimento" :tela="nomeTela" :permissaoEdicao="false" obrigatorioPorPadrao/>
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.numeroNotaLancamentoContabil"
                            disabled
                            :placeholder="' '"
                            name="número da NL"
                            v-mask="'####NL######'">
                            <template v-slot:label>
                                <label-personalizado campo="numeroNotaLancamentoContabil" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataNotaLancamentoContabil"
                            is-disabled
                            :placeholderDate="'__/__/____'"
                            name-date="data da NL"
                            date>
                            <template v-slot:label-date>
                                <label-personalizado campo="dataNotaLancamentoContabil" :tela="nomeTela" :permissaoEdicao="false" />
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" class="pt-0">
                        <p class="titulo_checkbox pb-0"><label-personalizado campo="origem" :tela="nomeTela" :apenasNome="true"/></p>
                        <v-row class="checkbox_input tamanhoCheckbox">
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemConvenio"
                                    disabled
                                    label="Convênio"
                                    class="tamanhoCheckbox"/>
                            </v-col>
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemFundo"
                                    disabled
                                    label="Fundo"
                                    class="checkbox__fundo"/>
                            </v-col>
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemProjeto"
                                    disabled
                                    label="Projeto"
                                    class="checkbox__projeto"/>
                            </v-col>
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemComodato"
                                    disabled
                                    label="Comodato"
                                    class="checkbox__comodato"/>
                            </v-col>

                        </v-row>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemConvenio">
                        <v-autocomplete
                            v-model="dadosDeEntrada.convenio"
                            disabled
                            :placeholder="' '"
                            name="convenio"
                            :items="convenios"
                            item-text="nome"
                            item-value="id"
                            hide-no-data
                            hide-selected
                            :filter="filtroComboAutoComplete">
                            <template v-slot:label>
                                <label-personalizado campo="convenio" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemFundo">
                        <v-autocomplete
                            v-model="dadosDeEntrada.fundo"
                            disabled
                            :placeholder="' '"
                            name="fundo"
                            :items="fundos"
                            item-text="descricao"
                            item-value="id">
                            <template v-slot:label>
                                <label-personalizado campo="fundo" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemProjeto">
                        <v-text-field
                            v-model="dadosDeEntrada.projeto"
                            disabled
                            :placeholder="' '"
                            name="projeto"
                            maxlength="100">
                            <template v-slot:label>
                                <label-personalizado campo="projeto" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemComodato">
                        <v-autocomplete
                            v-model="dadosDeEntrada.comodante"
                            disabled
                            :placeholder="' '"
                            :items="comodantes"
                            :filter="filtroComboAutoComplete"
                            item-text="nome"
                            item-value="id"
                            name="Comodante">
                            <template v-slot:label>
                                <label-personalizado campo="comodante" :tela="nomeTela" :permissaoEdicao="false"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>
        <acoes-botoes-continuar-voltar :controleContinuar="podeContinuar" @voltar="tratarEventoVoltar"
                                       @continuar="tratarEventoContinuar"/>
    </div>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import Empenho from '@/views/components/empenho/Empenho'
    import {createNamespacedHelpers, mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'dadosGerais',
        components: {Empenho, LabelPersonalizado, AcoesBotoesContinuarVoltar},
        data() {
            return {
                dadosDeEntrada: {},
                empenhos: [{incorporacaoId: this.$route.params.incorporacaoId}],
                reconhecimentos: [],
                fornecedores: [],
                comodantes: [],
                convenios: [],
                fundos: [],
                orgaos: [],
                setores: [],
                cnpjPesquisadoContemMascara: false,
                empenhoObrigatorio: false,
                notaObrigatorio: false,
                reconhecimentoInativo: false,
                fornecedorInativo: false,
                orgaoInativo: false,
                setorInativo: false,
                nomeTela: 'INCORPORACAO_DADOS_GERAIS',
                labelBtnCancel: 'Remover',
                cpfCnpj: ''
            }
        },
        async mounted() {
            await this.buscaIncorporacao()
            this.trataCamposReconhecimentoFornecedor()
            this.trataCampoComodante()
            await this.buscaOrgaos()
            await this.buscarSetores()
            this.trataCamposOrgaoSetor()
            this.verificarSePodeProsseguir()
            await this.buscarTodosFundos()
        },
        computed: {
            ...mapGetters([
                'getObrigatorioRotulosPersonalizados',
                'getIncorporacaoValidado'
            ]),
            verificaPermissaoRetroativa() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Retroativo'])
            },
            dataPermitida() {
                let date = new Date()
                return new Date(date.getFullYear(), date.getMonth(), 1)
            },
            podeContinuar() {
                if(this.empenhoObrigatorio){
                    if(!this.verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos()){
                        return false
                    }
                }
                return this.getIncorporacaoValidado(this.dadosDeEntrada,
                                                    this.dadosDeEntrada.origemConvenio,
                                                    this.dadosDeEntrada.origemFundo,
                                                    this.dadosDeEntrada.origemProjeto,
                                                    this.dadosDeEntrada.origemComodato,
                                                    this.notaObrigatorio,
                                                    this.nomeTela)
            }
        },
        filters: {
            fornecedorFilter(id, fornecedores) {
                const encontados = fornecedores.filter(f => f.id === id)
                return encontados.length > 0 ? encontados[0].nome : ''
            },
        },
        methods: {
            ...mapActions([
                actionTypes.COMUM.BUSCAR_FUNDOS,
                actionTypes.COMUM.BUSCAR_TODOS_ORGAOS,
                actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO,
                actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE,
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS,
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO,
                actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID,
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID
            ]),
            async buscaEmpenhos() {
                const resultado = await this.buscarTodosEmpenhos(this.$route.params.incorporacaoId)
                if (resultado && resultado.items.length > 0) {
                    this.empenhos = resultado.items
                }
                this.verificarSePodeProsseguir()
            },
            async buscaIncorporacao() {
                this.dadosDeEntrada = await this.buscarIncorporacaoPorId(this.$route.params.incorporacaoId)
                await this.buscarReconhecimentos()
                await this.buscarConvenio()
                await this.buscaEmpenhos()
            },
            trataCamposReconhecimentoFornecedor(){
                this.trataCampoReconhecimento()
                this.trataCampoFornecedor()
            },
            trataCamposOrgaoSetor(){
                this.trataCampoOrgao()
                this.trataCampoSetor()
            },
            trataCampoReconhecimento(){
                if(this.dadosDeEntrada && this.dadosDeEntrada.reconhecimento){
                    if(this.dadosDeEntrada.reconhecimento.situacao === 'INATIVO'){
                        this.dadosDeEntrada.reconhecimento.disabled = true
                        this.reconhecimentoInativo = true
                        this.reconhecimentos.unshift(this.dadosDeEntrada.reconhecimento)
                    }else if(this.dadosDeEntrada.reconhecimento.situacao === 'ATIVO'){
                        this.reconhecimentoInativo = false
                    }
                }
            },
            trataCampoFornecedor(){
                if(this.dadosDeEntrada && this.dadosDeEntrada.fornecedor) {
                    if (this.dadosDeEntrada.fornecedor.situacao === 'INATIVO') {
                        this.dadosDeEntrada.fornecedor.disabled = true
                        this.fornecedorInativo = true
                        this.fornecedores.unshift(this.dadosDeEntrada.fornecedor)
                    } else if(this.dadosDeEntrada.fornecedor.situacao === 'ATIVO') {
                        this.fornecedorInativo = false
                        this.fornecedores.push(this.dadosDeEntrada.fornecedor)
                    }
                }
            },
            trataCampoComodante(){
                if(this.dadosDeEntrada.comodante){
                    this.comodantes.push(this.dadosDeEntrada.comodante)
                }
            },
            trataCampoOrgao(){
                if(this.dadosDeEntrada && this.dadosDeEntrada.orgao) {
                    if (this.dadosDeEntrada.orgao.situacao === 'INATIVO') {
                        this.dadosDeEntrada.orgao.disabled = true
                        this.orgaoInativo = true
                        this.orgaos.unshift(this.dadosDeEntrada.orgao)
                    } else if(this.dadosDeEntrada.orgao.situacao === 'ATIVO'){
                        this.orgaoInativo = false
                    }
                }
            },
            trataCampoSetor(){
                if(this.dadosDeEntrada && this.dadosDeEntrada.setor) {
                    if (this.dadosDeEntrada.setor.situacao === 'INATIVO') {
                        this.dadosDeEntrada.setor.disabled = true
                        this.setorInativo = true
                        this.setores.unshift(this.dadosDeEntrada.setor)
                    } else if(this.dadosDeEntrada.setor.situacao === 'ATIVO'){
                        this.setorInativo = false
                    }
                }
            },
            async buscarConvenio() {
                if (this.dadosDeEntrada.convenio) {
                    let convenio = await this.buscarConvenioPorId(this.dadosDeEntrada.convenio)
                    this.dadosDeEntrada.convenio = convenio.id
                    this.convenios.push(convenio)
                }
            },
            async buscarReconhecimentos() {
                this.setMensagemLoading('Carregando...')
                const resultado = await this.buscarReconhecimentosAutoComplete()
                this.reconhecimentos = resultado.items
                this.verificaSeEmpenhoEOuNotaObrigatorio()
            },
            filtroComboAutoComplete(item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            formataCnpj(cnpj = '') {
                cnpj = cnpj.replace(/\D/g, '')
                cnpj = cnpj.replace(
                    /(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/,
                    '$1.$2.$3/$4-$5'
                )
                return cnpj
            },
            verificarSePodeProsseguir() {
                if (this.podeContinuar) {
                    this.$emit('habilitaPasso2')
                } else {
                    this.$emit('desabilitaPasso2')
                }
            },
            verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos() {
                return this.empenhos.every(empenho => empenho.numeroEmpenho && empenho.dataEmpenho && empenho.valorEmpenho && empenho.valorEmpenho!==0)
            },
            selecionarReconhecimento() {
                return this.reconhecimentos.filter(reconhecimento => reconhecimento.id === this.dadosDeEntrada.reconhecimento.id)
            },
            verificaSeEmpenhoEOuNotaObrigatorio() {
                if (this.dadosDeEntrada.reconhecimento !== null) {
                    const reconhecimentoSelecionado = this.selecionarReconhecimento()

                    if (reconhecimentoSelecionado.length > 0) {
                        this.empenhoObrigatorio = reconhecimentoSelecionado[0].empenho
                        this.notaObrigatorio = reconhecimentoSelecionado[0].notaFiscal
                    }
                }
                this.verificarSePodeProsseguir()
            },
            async buscaOrgaos() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarTodosOrgaos()
                this.habilitarLoadingGlobal()
                if (resultado) {
                    this.orgaos = resultado.items
                }
            },
            async selecionaOrgao() {
                this.dadosDeEntrada.setor = null
                this.setores = []
                if (this.dadosDeEntrada.orgao) {
                    const resultado = await this.buscarSetoresAlmoxarifado(this.dadosDeEntrada.orgao)
                    this.setores = resultado.items
                    await this.buscarTodosFundos()
                }
            },
            async buscarSetores() {
                if (this.dadosDeEntrada.orgao) {
                    const resultado = await this.buscarSetoresAlmoxarifado(this.dadosDeEntrada.orgao.id ? this.dadosDeEntrada.orgao.id : this.dadosDeEntrada.orgao)
                    this.setores = resultado.items
                }
            },
            async buscarTodosFundos() {
                if (this.dadosDeEntrada.orgao) {
                    const resultado = await this.buscarFundos(this.dadosDeEntrada.orgao.id ? this.dadosDeEntrada.orgao.id : this.dadosDeEntrada.orgao)
                    this.fundos = resultado.items
                }
            },
            filtroAutocompleteFornecedores(item, queryText) {
                const textOne = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()

                if (item.cpfCnpj) {
                    const textTwo = item.cpfCnpj
                    return textOne.indexOf(searchText) > -1 ||
                        textTwo.indexOf(searchText) > -1
                }
                return textOne.indexOf(searchText) > -1
            },
            tratarEventoVoltar() {
                this.$router.push({name: 'IncorporacaoListagem'})
            },
            tratarEventoContinuar() {
                this.$router.push({name: 'ItensIncorporacaoListagem', params: {incorporacaoId: this.$route.params.incorporacaoId}})
            },
        }
    }
</script>

<style scoped lang="stylus">
.form-dados-gerais-visualizacao
    min-height 62vh

.tamanhoCheckbox
    height 20px
    .checkbox__fundo
        margin-left 5px
    .checkbox__projeto
        margin-left -7px
    .checkbox__comodato
        margin-left -16px

.titulo_checkbox
    font-size 14px
    color #777777
    display inline-flex
    margin-left 5px
    font-weight bold
    margin-bottom 0!important

.empenhoBorder
    width 100%
    padding-top 8px
    margin-bottom 15px
    border-style solid
    border-radius 5px
    border-color #7777
    border-width 1px

>>>
    @media (max-width: 1280px)
        .tamanhoCheckbox
            label
                font-size 13px
            .v-input--selection-controls__input
                margin-right 0
                width 12px
                padding-right 4px
            .v-input--selection-controls__input:first-child
                margin-left 5px
            .v-input--selection-controls__ripple:before,
            .v-ripple__container
                top 3px
                bottom 3px
                left -5px
                right 10px
</style>
