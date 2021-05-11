<template>
    <v-form class="az-form-content mt-0 pl-0 pr-0">
        <concedente-modal
            :modalConcedente="modalConcedente"
            @fecharModal="tratarEventoFecharModal"
            @selecionarConcedente="selecionarConcedente"/>
        <v-container fluid grid-list-xl white>
            <div wrap align-center white class="row-top">
                <label class="pr-titulo">
                    Cadastro do Convênio {{(ehNomeConvenioVazio ? '' : '» ' + dadosGerais.nome)}}
                </label>
            </div>
            <v-row wrap align-center white class="pl-10 pr-10">
                <v-col cols="12" md="3" sm="3" xs="12">
                    <v-text-field
                        v-model="dadosGerais.numero"
                        name="numero"
                        placeholder="Informe o número"
                        counter="50"
                        maxlength="50"
                        v-validate="'required|max:50'"
                        :error-messages="errors.collect('numero')"
                        @focusout="validarDadosFormulario"
                        @input="validarDadosFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="numero" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="12" md="3" sm="3" xs="12">
                    <v-text-field
                        v-model="dadosGerais.nome"
                        name="nome"
                        placeholder="Informe o nome do convênio"
                        counter="100"
                        maxlength="100"
                        v-validate="'required|max:100'"
                        :error-messages="errors.collect('nome')"
                        @focusout="validarDadosFormulario"
                        @input="validarDadosFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="nome" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="12" md="3" sm="3" xs="12">
                    <v-autocomplete
                        v-model="dadosGerais.concedente"
                        name="concedente"
                        placeholder="Pesquise pelo nome do concedente"
                        item-text="nome"
                        item-value="id"
                        append-icon=""
                        v-validate="'required'"
                        :filter="filtroAutocompleteConcedentes"
                        :items="concedentes"
                        :search-input.sync="buscaConcedenteDinamicamente"
                        :error-messages="errors.collect('concedente')"
                        @update:search-input="texto => verificarCampoConcedenteVazio(texto)"
                        @focusout="validarDadosFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="concedente" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                        <template v-slot:no-data v-if="possuiBuscaConcedente">
                            <az-call-to-action
                                class="btn-gerenciar-concedentes"
                                @click="abrirModalConcedente">
                                <v-icon size="16" color="gray">
                                    fas fa-cog
                                </v-icon>
                                <label class="btn-text-gerenciar-concedentes">Gerenciar Concedentes</label>
                            </az-call-to-action>
                        </template>
                        <template v-slot:no-data v-else>
                            <label v-show="false"></label>
                        </template>
                        <template v-slot:item="data">
                            <label class="auto-complete-item-text">{{data.item.nome}}</label>
                        </template>
                    </v-autocomplete>

                </v-col>
                <v-col cols="12" md="3" sm="3" xs="12">
                    <v-autocomplete
                        v-model="dadosGerais.fonteRecurso"
                        name="fonteRecurso"
                        placeholder="Selecione a fonte de recurso"
                        v-validate="getObrigatorioRotulosPersonalizados('fonteRecurso', 'CONVENIO')? 'required' : ''"
                        :items="fonteRecursos"
                        item-text="nome"
                        :filter="filtroComboAutoComplete"
                        :error-messages="errors.collect('fonteRecurso')"
                        @update:search-input="validarDadosFormulario"
                        @focusout="validarDadosFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="fonteRecurso" :tela="nomeTela"/>
                        </template>
                        <template v-slot:item="data">
                            <label class="auto-complete-item-text">{{data.item.nome}}</label>
                        </template>
                    </v-autocomplete>
                </v-col>
            </v-row>
            <v-row wrap align-center white class="pl-10 pr-10">
                <v-col cols="12" md="2" sm="3" xs="12">
                    <az-date
                        v-model="dadosGerais.dataVigenciaInicio"
                        name-date="dataVigenciaInicio"
                        date
                        :placeholderDate="(dadosGerais.dataVigenciaInicio) ? ' ' : placeholders.date"
                        is-required
                        @input="validarDadosFormulario">
                        <template v-slot:label-date>
                            <label-personalizado campo="dataVigenciaInicio" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </az-date>
                </v-col>
                <v-col cols="12" md="2" sm="3" xs="12">
                    <az-date
                        v-model="dadosGerais.dataVigenciaFim"
                        name-date="dataVigenciaFim"
                        date
                        :placeholderDate="(dadosGerais.dataVigenciaFim) ? ' ' : placeholders.date"
                        :min-date="dadosGerais.dataVigenciaInicio"
                        is-required
                        @input="validarDadosFormulario">
                        <template v-slot:label-date>
                            <label-personalizado campo="dataVigenciaFim" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </az-date>
                </v-col>
                <v-col cols="12" md="4" sm="3" xs="12">
                    <h3 class="v-label v-label--active theme--light radio-situacao">
                        <label-personalizado campo="situacao" :tela="nomeTela" obrigatorioPorPadrao/>
                    </h3>
                    <v-radio-group
                        v-validate="'required'"
                        v-model="dadosGerais.situacao"
                        @change="validarDadosFormulario"
                        class="checkbox_convenio"
                        row>
                        <v-radio label="Ativo" value="ATIVO"/>
                        <v-radio label="Inativo" value="INATIVO"/>
                    </v-radio-group>
                </v-col>
            </v-row>
            <div class="row-bottom">
                <acoes-botoes-salvar-cancelar :camposAceitos="camposAceitos" @salvar="tratarEventoSalvar" @cancelar="tratarEventoCancelar"/>
            </div>
        </v-container>
    </v-form>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import {createNamespacedHelpers} from 'vuex'
    import ConcedenteModal from '@/views/components/modal/configuracao/concedente/ConcedenteModal'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import AcoesBotoesSalvarCancelar from '@/views/components/acoes/AcoesBotoesSalvarCancelar'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ConvenioDadosGeraisCadastro',
        $_veeValidate: {
            validator: 'new'
        },
        components: {ConcedenteModal,LabelPersonalizado, AcoesBotoesSalvarCancelar},
        data() {
            return {
                dadosGerais: {
                    nome: '',
                    situacao: 'ATIVO',
                    concedente: null
                },
                placeholders: {
                    date: '__/__/____',
                },
                convenioId: null,
                modalConcedente: false,
                concedentes: [],
                paginas: 0,
                totalItens: 0,
                fonteRecursos: [
                    {id: 1, nome: 'Aquisição de materiais'},
                    {id: 2, nome: 'Aquisição de protótipos'}
                ],
                buscaConcedenteDinamicamente: null,
                estaBuscandoConcedentes: false,
                possuiBuscaConcedente: false,
                camposAceitos: true,
                nomeTela: 'CONVENIO'
            }
        },
        async mounted() {
            await this.validarDadosFormulario()
        },
        computed: {
            ...mapGetters(
                ['getObrigatorioRotulosPersonalizados','getObjetoValidado']
            ),
            ehNomeConvenioVazio() {
                return this.dadosGerais.nome === ''
            }
        },
        watch: {
            buscaConcedenteDinamicamente(val) {
                if (val) {
                    if (this.estaBuscandoConcedentes) return
                    if (val.length > 1) {
                        this.desabilitarLoadingGlobal()
                        this.estaBuscandoConcedentes = true

                        this.$store
                            .dispatch(actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE, val)
                            .then(resultado => {
                                this.concedentes = resultado.items
                                this.estaBuscandoConcedentes = false
                                this.habilitarLoadingGlobal()
                            })
                    }
                } else {
                    this.dadosGerais.concedente = null
                    this.concedentes = []
                }
            },
        },
        methods: {
            filtroComboAutoComplete(item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            filtroAutocompleteConcedentes(item, queryText) {
                const textOne = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()

                if (item.cpfCnpj) {
                    const textTwo = item.cpfCnpj
                    return textOne.indexOf(searchText) > -1 ||
                        textTwo.indexOf(searchText) > -1
                }
                return textOne.indexOf(searchText) > -1
            },
            abrirModalConcedente() {
                this.modalConcedente = true
            },
            async selecionarConcedente(concedente) {
                this.dadosGerais.concedente = concedente.id
                this.concedentes.push(concedente)
                this.tratarEventoFecharModal()
            },
            tratarEventoCancelar() {
                this.$router.push({name: this.$store.state.convenio.rota.origem})
            },
            tratarEventoFecharModal() {
                this.modalConcedente = false
            },
            verificarCampoConcedenteVazio(texto) {
                if (texto) {
                    this.possuiBuscaConcedente = (texto.length > 0)
                }else{
                    this.possuiBuscaConcedente = false
                }
                this.validarDadosFormulario()
            },
            async tratarEventoSalvar() {
                await this.validarDadosFormulario()
                if (this.camposAceitos) {
                    this.setMensagemLoading('Salvando o convênio...')
                    await this.$store.dispatch(actionTypes.CONFIGURACAO.CONVENIO.SALVAR_CONVENIO, this.dadosGerais)
                    this.convenioId = this.$store.state.convenio.dadosGerais.id
                    this.mostrarNotificacaoSucessoDefault()
                    this.tratarEventoCancelar()
                }
            },
            async validarDadosFormulario() {
                this.camposAceitos = this.getObjetoValidado(this.dadosGerais, 'CONVENIO')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .pr-titulo
        font-size 18px !important
        color #777 !important

    .row-top
        border-bottom-width 1px
        border-bottom-style solid
        border-bottom-color #ccc
        box-sizing border-box
        width 100%
        margin-bottom 20px
        padding 0 0 20px 20px

    .row-bottom
        border-top-width 1px
        border-top-style solid
        border-top-color #ccc
        box-sizing border-box
        width 100%
        margin-top 15%

    .radio-situacao
        margin-bottom 10px
        font-size 14px !important
        color #777 !important

    .auto-complete-item-text
        font-size 14px !important
        color #777 !important

    .btn-gerenciar-concedentes
        border none
        border-top-width 1px
        border-top-style solid
        border-top-color #ccc
        border-radius 0
        width 100%
        box-sizing border-box
        justify-content start
        max-height: 47px

    .btn-text-gerenciar-concedentes
        margin-left 5px
        font-weight bold
        font-size 14px !important
        color #777 !important
</style>
