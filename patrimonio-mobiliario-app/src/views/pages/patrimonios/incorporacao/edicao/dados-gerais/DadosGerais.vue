<template>
    <div>
        <v-form class="az-form-content pb-0" ref="form">
            <v-container class="white form-dados-gerais" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.reconhecimento"
                            ref="autoCompleteReconhecimento"
                            name="reconhecimento"
                            placeholder="Selecione o reconhecimento"
                            v-validate="'required'"
                            :items="reconhecimentos"
                            item-text="nome"
                            item-value="id"
                            :filter="filtroComboAutoComplete"
                            :error-messages="errors.collect('reconhecimento')"
                            @change="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="reconhecimento" :tela="nomeTela" apresentaTooltip obrigatorioPorPadrao/>
                            </template>
                            <template v-slot:item="data">
                                <label :style="obterLarguraAutoComplete('autoCompleteReconhecimento')"
                                       class="auto-complete-item-text text-truncate cursor__pointer">{{data.item.nome}}</label>
                            </template>
                            <template v-slot:append-outer v-if="reconhecimentoInativo">
                                <tooltip-campos-inativos mensagem="Este reconhecimento foi inativado, por favor selecione outro!"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.numProcesso"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            name="número do processo"
                            placeholder="Informe o número"
                            maxlength="30"
                            v-validate="getObrigatorioRotulosPersonalizados('numProcesso', nomeTela)?'required|max:30' : 'max:30'"
                            :error-messages="errors.collect('número do processo')"
                            @change="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="numProcesso" :tela="nomeTela"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            style="max-height:35px;"
                            v-model="dadosDeEntrada.fornecedor"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            :items="fornecedores"
                            :loading="estaBuscandoFornecedores"
                            :return-object="false"
                            :search-input.sync="buscaFornecedorDinamicamente"
                            ref="autoCompleteFornecedor"
                            :filter="filtroAutocompleteFornecedores"
                            item-text="cpfCnpj"
                            item-value="id"
                            v-validate="getObrigatorioRotulosPersonalizados('fornecedor', nomeTela)? 'required' : ''"
                            name="CNPJ Fornecedor"
                            :error-messages="errors.collect('CNPJ Fornecedor')"
                            no-data-text="Não há fornecedores com este CPF/CNPJ"
                            placeholder="Informe a razão social ou o CPF/CNPJ"
                            @blur="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="fornecedor" :tela="nomeTela"/>
                            </template>
                            <template slot="item" slot-scope="data">
                                <span :style="obterLarguraAutoComplete('autoCompleteFornecedor')"
                                      class="text-truncate">{{ formataCnpj(data.item.cpfCnpj) }} - {{ data.item.nome }}</span>
                            </template>
                            <template v-slot:append-outer v-if="fornecedorInativo">
                                <tooltip-campos-inativos mensagem="Este fornecedor foi inativado, por favor selecione outro!"/>
                            </template>
                            <template v-slot:append v-if="dadosDeEntrada.fornecedor">
                                <v-tooltip top>
                                    <template v-slot:activator="{ on }">
                                        <v-icon v-on="on" class="pr-3 pt-1" small @click="removerFornecedor">fas fa-times</v-icon>
                                    </template>
                                    {{ labelBtnCancel }}
                                </v-tooltip>
                            </template>
                        </v-autocomplete>
                        <az-text-view
                            :text="dadosDeEntrada.fornecedor ? $options.filters.fornecedorFilter(dadosDeEntrada.fornecedor, fornecedores) : ''" class="mt-2"/>
                    </v-col>
                </v-row>
                <div class="empenhoBorder">
                    <empenho v-for="(empenho, index) in empenhos" :key="empenho.id"
                             :value="empenho"
                             :idIncorporacao="dadosDeEntrada.id"
                             :quantidadeEmpenhos="empenhos.length"
                             :primeiroEmpenho="index === 0"
                             :obrigatorio="empenhoObrigatorio"
                             :status-reconhecimento="!dadosDeEntrada.reconhecimento"
                             @adicionarNovoEmpenho="adicionarNovoEmpenho"
                             @buscarEmpenhos="buscaEmpenhos"
                             @removerEmpenho="removerEmpenho"
                             @atualizarIncorporacao="editarIncorporacao"/>
                </div>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.nota"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            name="número da nota"
                            placeholder="Informe o número"
                            maxlength="30"
                            v-validate="notaObrigatorio ? 'required|max:30' : 'max:30'"
                            :error-messages="errors.collect('número da nota')"
                            @change="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="nota" :tela="nomeTela" :permissaoEdicao="notaObrigatorio" obrigatorioPorPadrao/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataNota"
                            :is-disabled="!this.dadosDeEntrada.reconhecimento"
                            name-date="data nota"
                            date
                            :is-required="notaObrigatorio"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            @input="editarIncorporacao"
                            :placeholderDate="(dadosDeEntrada.dataNota) ? ' ' : placeholders.date"
                            :error-messages="errors.collect('data nota')">
                            <template v-slot:label-date>
                                <label-personalizado campo="dataNota" :tela="nomeTela" :permissaoEdicao="notaObrigatorio" obrigatorioPorPadrao/>
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-money
                            v-if="dadosDeEntrada.id"
                            v-model="dadosDeEntrada.valorNota"
                            :maxLength="18"
                            :event-submit="'blur'"
                            :required="notaObrigatorio"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            class="botaoExcluirMoney"
                            placeholder="Informe o valor"
                            @input="atualizarValorInserido($event)">
                            <template v-slot:label>
                                <label-personalizado campo="valorNota" :tela="nomeTela" :permissaoEdicao="notaObrigatorio" obrigatorioPorPadrao/>
                            </template>
                        </az-money>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.orgao"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há órgãos com este nome"
                            name="orgaoResponsavel"
                            ref="autoCompleteOrgao"
                            v-validate="'required'"
                            :items="orgaos"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            placeholder="Selecione o órgão responsável"
                            :filter="filtroComboAutoComplete"
                            :error-messages="!dadosDeEntrada.orgao ? errors.collect('orgaoResponsavel') : ''"
                            @change="selecionaOrgao">
                            <template v-slot:label>
                                <label-personalizado campo="orgao" :tela="nomeTela" obrigatorioPorPadrao/>
                            </template>
                            <template v-slot:item="data">
                                <label
                                    :style="obterLarguraAutoComplete('autoCompleteOrgao')"
                                    class="uo__autocomplete cursor__pointer text-truncate">
                                    {{data.item.descricao}}
                                </label>
                            </template>
                            <template v-slot:append-outer v-if="orgaoInativo">
                                <tooltip-campos-inativos mensagem="Este órgão foi inativado, por favor selecione outro!"/>
                            </template>
                        </v-autocomplete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="dadosDeEntrada.setor"
                            item-text="descricao"
                            item-value="id"
                            name="setor"
                            ref="autoCompleteSetor"
                            no-data-text="Não há setores com este nome"
                            :placeholder="dadosDeEntrada.orgao ? 'Selecione o setor' : ' '"
                            :disabled="!dadosDeEntrada.orgao"
                            :items="setores"
                            :error-messages="!dadosDeEntrada.setor ? errors.collect('setor') : ''"
                            v-validate="dadosDeEntrada.orgao ? 'required' : ''"
                            @change="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="setor" :tela="nomeTela" :permissaoEdicao="!!dadosDeEntrada.orgao"
                                                     obrigatorioPorPadrao apresentaTooltip/>
                            </template>
                            <template v-slot:item="data">
                                <label
                                    :style="obterLarguraAutoComplete('autoCompleteSetor')"
                                    class="uo__autocomplete cursor__pointer text-truncate">
                                    {{data.item.descricao}}
                                </label>
                            </template>
                            <template v-slot:append-outer v-if="setorInativo">
                                <tooltip-campos-inativos mensagem="Este setor foi inativado, por favor selecione outro!"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataRecebimento"
                            :is-disabled="!this.dadosDeEntrada.reconhecimento"
                            name-date="recebimento"
                            date
                            v-validate="'required'"
                            @input="editarIncorporacao"
                            :min-date="verificaPermissaoRetroativa ? '' : moment(dataPermitida).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            :placeholderDate="(dadosDeEntrada.dataRecebimento) ? ' ' : placeholders.date"
                            :is-required="true"
                            :error-messages="errors.collect('recebimento')">
                            <template v-slot:label-date>
                                <label-personalizado campo="dataRecebimento" :tela="nomeTela" obrigatorioPorPadrao/>
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.numeroNotaLancamentoContabil"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            name="número da NL"
                            :placeholder="placeholders.numeroNotaLancamentoContabil"
                            maxlength="12"
                            v-mask="'####NL######'"
                            v-validate="getObrigatorioRotulosPersonalizados('numeroNotaLancamentoContabil', nomeTela)?'required|max:12' : 'max:12'"
                            :error-messages="errors.collect('número da NL')"
                            @change="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="numeroNotaLancamentoContabil" :tela="nomeTela"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataNotaLancamentoContabil"
                            :is-disabled="!this.dadosDeEntrada.reconhecimento"
                            name-date="data da NL"
                            date
                            :is-required="getObrigatorioRotulosPersonalizados('dataNotaLancamentoContabil', nomeTela)"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            @input="editarIncorporacao"
                            :placeholderDate="(dadosDeEntrada.dataNotaLancamentoContabil) ? ' ' : placeholders.date"
                            :error-messages="errors.collect('data da NL')">
                            <template v-slot:label-date>
                                <label-personalizado campo="dataNotaLancamentoContabil" :tela="nomeTela"/>
                            </template>
                        </az-date>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" class="pt-0">
                        <p class="titulo_checkbox pb-0"><label-personalizado campo="origem" :tela="nomeTela" :apenasNome="true"/></p>
                        <v-row class="checkbox_input tamanhoCheckbox">
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemConvenio"
                                    @change="editarIncorporacao"
                                    label="Convênio"
                                    :disabled="!this.dadosDeEntrada.reconhecimento"
                                    class="tamanhoCheckbox"/>
                            </v-col>
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemFundo"
                                    label="Fundo"
                                    @change="selecionaFundo"
                                    :disabled="!this.dadosDeEntrada.reconhecimento"
                                    class="checkbox__fundo"/>
                            </v-col>
                           <v-col cols="3">
                               <v-checkbox
                                   v-model="dadosDeEntrada.origemProjeto"
                                   label="Projeto"
                                   @change="editarIncorporacao"
                                   :disabled="!this.dadosDeEntrada.reconhecimento"
                                   class="checkbox__projeto"/>
                           </v-col>
                            <v-col cols="3">
                                <v-checkbox
                                    v-model="dadosDeEntrada.origemComodato"
                                    label="Comodato"
                                    @change="editarIncorporacao"
                                    :disabled="!this.dadosDeEntrada.reconhecimento"
                                    class="checkbox__comodato"/>
                            </v-col>
                        </v-row>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemConvenio">
                        <v-autocomplete
                            v-model="dadosDeEntrada.convenio"
                            name="convenio"
                            placeholder="Pesquise pelo nome do convênio"
                            v-validate="(dadosDeEntrada.origemConvenio && getObrigatorioRotulosPersonalizados('convenio', nomeTela)) ? 'required' : ''"
                            :items="convenios"
                            item-text="nome"
                            item-value="id"
                            hide-no-data
                            hide-selected
                            @change="editarIncorporacao"
                            :filter="filtroComboAutoComplete"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            :search-input.sync="buscarConvenioDinamicamente"
                            :error-messages="errors.collect('convenio')">
                            <template v-slot:label>
                                <label-personalizado campo="convenio" :tela="nomeTela"/>
                            </template>
                            <template v-slot:item="data">
                                <label class="auto-complete-item-text">{{data.item.nome}}</label>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemFundo">
                        <v-autocomplete
                            v-model="dadosDeEntrada.fundo"
                            name="fundo"
                            :placeholder="dadosDeEntrada.orgao ? 'Selecione o fundo' : ' '"
                            v-validate="(dadosDeEntrada.origemFundo && getObrigatorioRotulosPersonalizados('fundo', nomeTela)) ? 'required' : ''"
                            :items="fundos"
                            item-text="descricao"
                            item-value="id"
                            :disabled="!dadosDeEntrada.orgao || !this.dadosDeEntrada.reconhecimento"
                            @change="editarIncorporacao"
                            :error-messages="errors.collect('fundo')">
                            <template v-slot:label>
                                <label-personalizado campo="fundo" :tela="nomeTela"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemProjeto">
                        <v-text-field
                            v-model="dadosDeEntrada.projeto"
                            name="projeto"
                            maxlength="100"
                            placeholder="Informe o projeto"
                            v-validate="(dadosDeEntrada.origemProjeto && getObrigatorioRotulosPersonalizados('projeto', nomeTela)) ? 'required' : ''"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            @change="editarIncorporacao"
                            :error-messages="errors.collect('projeto')">
                            <template v-slot:label>
                                <label-personalizado campo="projeto" :tela="nomeTela"/>
                            </template>
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12" v-if="dadosDeEntrada.origemComodato">
                        <v-autocomplete
                            style="max-height:47px;"
                            v-model="dadosDeEntrada.comodante"
                            :disabled="!this.dadosDeEntrada.reconhecimento"
                            :items="comodantes"
                            :loading="estaBuscandoComodantes"
                            :return-object="false"
                            :search-input.sync="buscarComodantesDinamicamente"
                            :filter="filtroComboAutoComplete"
                            item-text="nome"
                            item-value="id"
                            v-validate="!!(dadosDeEntrada.origemComodato && getObrigatorioRotulosPersonalizados('comodante', nomeTela)) ? 'required' : ''"
                            name="Comodante"
                            :error-messages="errors.collect('Comodante')"
                            no-data-text="Não existe comodante cadastrado com este nome"
                            placeholder="Pesquise pelo nome do comodante"
                            @blur="editarIncorporacao">
                            <template v-slot:label>
                                <label-personalizado campo="comodante" :tela="nomeTela"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>
        <acoes-botoes-continuar-voltar :controleContinuar="podeContinuar" @voltar="tratarEventoVoltar" @continuar="tratarEventoContinuar"/>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import Empenho from '@/views/components/empenho/Empenho'
    import {createNamespacedHelpers, mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import TooltipCamposInativos from '@/views/components/tooltip/TooltipCamposInativos'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import Axios from 'axios'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'dadosGerais',
        components: {TooltipCamposInativos, Empenho, LabelPersonalizado, AcoesBotoesContinuarVoltar},
        data() {
            return {
                dadosDeEntrada: {},
                incorporacao: {},
                empenhos: [{incorporacaoId: this.$route.params.incorporacaoId}],
                reconhecimentos: [],
                fornecedores: [],
                comodantes: [],
                convenios: [],
                fundos: [],
                orgaos: [],
                setores: [],
                buscaFornecedorDinamicamente: null,
                buscarConvenioDinamicamente: null,
                buscarComodantesDinamicamente: null,
                estaBuscandoFornecedores: false,
                estaBuscandoConvenios: false,
                estaBuscandoComodantes: false,
                cnpjPesquisadoContemMascara: false,
                fornecedorSelecionado: true,
                empenhoObrigatorio: false,
                notaObrigatorio: false,
                reconhecimentoInativo: false,
                fornecedorInativo: false,
                orgaoInativo: false,
                setorInativo: false,
                incorporacaoId: null,
                nomeTela: 'INCORPORACAO_DADOS_GERAIS',
                labelBtnCancel: 'Remover',
                cpfCnpj: '',
                rotaRedirecionamento: '',
                placeholders: {
                    date: '__/__/____',
                    numeroNotaLancamentoContabil: '____NL_____'
                }
            }
        },
        async mounted() {
            this.setaIncorporacaoId()
            await this.buscaIncorporacao()
            this.trataCamposReconhecimentoFornecedor()
            this.trataCampoComodante()
            this.trataCamposOrgaoSetor()
            this.verificarSePodeProsseguir()
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
            fornecedorFilter(fornecedor, fornecedores) {
                if (typeof fornecedor == 'object') {
                    return fornecedor.nome
                } else {
                    const encontrados = fornecedores.filter(f => f.id === fornecedor)
                    return encontrados.length > 0 ? encontrados[0].nome : ''
                }
            },
        },
        watch: {
            buscarConvenioDinamicamente(val) {
                if (val) {
                    if (this.estaBuscandoConvenios) return
                    if (val.length > 1) {
                        this.desabilitarLoadingGlobal()
                        this.estaBuscandoConvenios = true

                        this.$store.dispatch(actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE, val)
                            .then(resultado => {
                                this.convenios = resultado.items
                                this.estaBuscandoConvenios = false
                                this.habilitarLoadingGlobal()
                            })
                    }
                } else {
                    this.dadosDeEntrada.convenio = null
                    this.convenios = []
                }
            },
            buscaFornecedorDinamicamente(val) {
                if(!this.dadosDeEntrada.fornecedor) {
                    if (val !== this.cpfCnpj) {
                        this.fornecedorSelecionado = false
                    }
                    if (val) {
                        if (this.estaBuscandoFornecedores) return

                        if (val.length > 1) {
                            this.desabilitarLoadingGlobal()
                            this.estaBuscandoFornecedores = true

                            this.$store
                                .dispatch(actionTypes.COMUM.BUSCAR_FORNECEDORES, val)
                                .then(resultado => {
                                    this.fornecedores = resultado.items

                                    this.estaBuscandoFornecedores = false
                                    this.habilitarLoadingGlobal()
                                })
                        }
                    } else {
                        this.dadosDeEntrada.fornecedor = null
                        this.fornecedores = []
                    }
                }
            },
            buscarComodantesDinamicamente(val) {
                if (val) {
                    if (this.estaBuscandoComodantes) return
                    if (val.length > 1) {
                        this.desabilitarLoadingGlobal()
                        this.estaBuscandoComodantes = true

                        this.$store.dispatch(actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE, val)
                            .then(resultado => {
                                this.comodantes = resultado.items
                                this.estaBuscandoComodantes = false
                                this.habilitarLoadingGlobal()
                            })
                    }
                } else {
                    this.dadosDeEntrada.comodante = null
                    this.comodantes = []
                }
            },
            'dadosDeEntrada.reconhecimento'() {
                this.verificaSeEmpenhoEOuNotaObrigatorio()
            }
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
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID,
                actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO
            ]),
            adicionarNovoEmpenho() {
                if(this.empenhos.length<10){
                    const empenhoVazio = {incorporacaoId: this.incorporacaoId}
                    this.empenhos.unshift(empenhoVazio)
                }
            },
            removerEmpenho(empenhoId) {
                this.empenhos = this.empenhos.filter(entity => entity.id !== empenhoId)
                this.verificarSePodeProsseguir()
            },
            async buscaEmpenhos() {
                const resultado = await this.buscarTodosEmpenhos(this.incorporacaoId)
                if (resultado && resultado.items.length > 0) {
                    this.empenhos = resultado.items
                }
                this.verificarSePodeProsseguir()
            },
            async buscaIncorporacao() {
                this.dadosDeEntrada = await this.buscarIncorporacaoPorId(this.incorporacaoId)
                this.incorporacao = Object.assign({}, this.dadosDeEntrada)
                await Axios.all([
                    this.buscarReconhecimentos(),
                    this.buscarConvenio(),
                    this.buscaEmpenhos(),
                    this.buscarTodosFundos(),
                    this.buscarUnidadesOrganizacionais()
                ])
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
            async editarIncorporacao() {
                this.desabilitarLoadingGlobal()
                this.verificarSeFornecedorSelecionado()
                try {
                    const dadosAtualizados = await this.atualizarIncorporacao(this.dadosDeEntrada)
                    this.setarDadosAtualizados(dadosAtualizados)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                    await this.buscaIncorporacao()
                }
                this.atualizaDadosAposEdicao(this.dadosDeEntrada)
                this.verificarSePodeProsseguir()
                this.trataCamposReconhecimentoFornecedor()
                this.trataCamposOrgaoSetor()
            },
            atualizaDadosAposEdicao(resultado){
                this.dadosDeEntrada.fornecedor = resultado.fornecedor
                this.dadosDeEntrada.reconhecimento = resultado.reconhecimento
                this.dadosDeEntrada.orgao = resultado.orgao
                this.dadosDeEntrada.setor = resultado.setor
                this.dadosDeEntrada.projeto = resultado.projeto
                this.dadosDeEntrada.comodante = resultado.comodante
            },
            filtroComboAutoComplete(item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            verificarSeFornecedorSelecionado() {
                if (this.dadosDeEntrada.fornecedor !== null) {
                    this.fornecedorSelecionado = true
                }
            },
            atualizarValorInserido(valor) {
                this.dadosDeEntrada.valorNota = valor
                this.editarIncorporacao()
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
            async buscarUnidadesOrganizacionais() {
                await this.buscarOrgaos()
                await this.buscarSetores()
                if(this.orgaos) {
                    if(this.verificaSeArrayContemApenasUmOrgao(this.orgaos) || this.verificaSeArrayContemApenasUmSetor(this.setores)) {
                        this.selecionaOrgaoSeArrayConterApenasUmOrgao(this.orgaos)
                        this.selecionaSetorSeArrayConterApenasUmSetor(this.setores)
                        await this.editarIncorporacao()
                    }
                }
            },
            async selecionaOrgao(orgao) {
                this.desabilitarLoadingGlobal()
                this.dadosDeEntrada.setor = null
                this.setores = []
                if(orgao) {
                    const [setores, fundos] = await Axios.all([
                        this.buscarSetoresAlmoxarifado(orgao),
                        this.buscarFundos(orgao)
                    ])

                    this.setores = setores.items
                    this.selecionaSetorSeArrayConterApenasUmSetor(this.setores)
                    this.fundos = fundos.items
                    this.selecionaFundoSeArrayConterApenasUmFundo(this.fundos)

                    await this.editarIncorporacao()
                }
            },
            async selecionaFundo() {
                this.selecionaFundoSeArrayConterApenasUmFundo(this.fundos)
                await this.editarIncorporacao()
            },
            async buscarSetores() {
                if(this.dadosDeEntrada.orgao) {
                    const resultado = await this.buscarSetoresAlmoxarifado(this.dadosDeEntrada.orgao.id ? this.dadosDeEntrada.orgao.id : this.dadosDeEntrada.orgao)
                    this.setores = resultado.items
                }
            },
            async buscarOrgaos() {
                const resultado = await this.buscarTodosOrgaos()
                this.orgaos = resultado.items
            },
            async buscarTodosFundos() {
                if (this.dadosDeEntrada.orgao) {
                    const resultado = await this.buscarFundos(this.dadosDeEntrada.orgao.id ? this.dadosDeEntrada.orgao.id : this.dadosDeEntrada.orgao)
                    this.fundos = resultado.items
                    if(this.verificaSeArrayContemApenasUmFundo(this.fundos)) {
                        this.selecionaFundoSeArrayConterApenasUmFundo(this.fundos)
                        await this.editarIncorporacao()
                    }
                }
            },
            selecionaSetorSeArrayConterApenasUmSetor(setores) {
                if(this.verificaSeArrayContemApenasUmSetor(setores)) {
                    this.dadosDeEntrada.setor = setores[0].id
                }
            },
            selecionaFundoSeArrayConterApenasUmFundo(fundos) {
                if(this.verificaSeArrayContemApenasUmFundo(fundos)) {
                    this.dadosDeEntrada.fundo = fundos[0].id
                }
            },
            selecionaOrgaoSeArrayConterApenasUmOrgao(orgaos) {
                if(this.verificaSeArrayContemApenasUmOrgao(orgaos)) {
                    this.dadosDeEntrada.orgao = orgaos[0].id
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
            removerFornecedor() {
                this.dadosDeEntrada.fornecedor = null
                this.editarIncorporacao()
            },
            async removerIncorporacao() {
                try {
                    await this.deletarIncorporacao(this.dadosDeEntrada.id)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                }
            },
            tratarEventoVoltar() {
                this.$router.replace({name: 'IncorporacaoListagem'})
            },
            tratarEventoContinuar() {
                this.$router.replace({name: 'ItensIncorporacaoListagem', params: {incorporacaoId: this.incorporacaoId}})
            },
            setarDadosAtualizados(dados) {
                this.verificarMudancasPropriedades(dados, this.incorporacao, this.dadosDeEntrada)
            },
            verificaSeArrayContemApenasUmOrgao(orgaos) {
                return orgaos.length === 1 && !this.dadosDeEntrada.orgao
            },
            verificaSeArrayContemApenasUmSetor(setores) {
                return setores.length === 1 && !this.dadosDeEntrada.setor
            },
            verificaSeArrayContemApenasUmFundo(fundos) {
                return fundos.length === 1 && !this.dadosDeEntrada.fundo
            },
            obterLarguraAutoComplete(ref) {
                if (this.$refs[ref] && this.$refs[ref].$el) {
                    return {
                        width: (this.$refs[ref].$el.clientWidth-30)+'px'
                    }
                }
                return {}
            },
            setaIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            }
        },
        async beforeRouteLeave(to, from, next) {
            if (this.dadosDeEntrada && this.dadosDeEntrada.id && !this.dadosDeEntrada.reconhecimento && this.rotaRedirecionamento !== to.name) {
                this.rotaRedirecionamento = to.name
                await this.removerIncorporacao()
            }
            next()
        }
    }
</script>

<style scoped lang="stylus">
    .form-dados-gerais
        min-height 62vh

    .cursor__pointer
        cursor pointer

    .uo__autocomplete
        font-size 14px

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
