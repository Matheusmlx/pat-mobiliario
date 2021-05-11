<template>
    <v-form class="az-form-content mt-0 pl-0 pr-0">
        <concedente-modal
            :modalConcedente="modalConcedente"
            @selecionarConcedente="selecionarConcedente"
            @fecharModal="tratarEventoFecharModal"/>
        <v-container fluid grid-list-xl white>
            <div wrap align-center white class="row-top">
                <label class="pr-titulo">
                    Edição do Convênio {{(ehNomeConvenioVazio ? '' : '» ' + dadosGerais.nome)}}
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
                        :filter="filtroAutocompleteConcedentes"
                        :items="concedentes"
                        :search-input.sync="buscaConcedenteDinamicamente"
                        v-validate="'required'"
                        :error-messages="errors.collect('concedente')"
                        @focusout="validarDadosFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="concedente" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                        <template v-slot:item="data">
                            <label class="auto-complete-item-text">{{data.item.nome}}</label>
                        </template>
                        <template v-slot:append-item>
                            <az-call-to-action
                                class="btn-gerenciar-concedentes"
                                @click="abrirModalConcedente">
                                <v-icon size="16" color="gray">
                                    fas fa-cog
                                </v-icon>
                                <label class="btn-text-gerenciar-concedentes">Gerenciar Concedentes</label>
                            </az-call-to-action>
                        </template>

                    </v-autocomplete>

                </v-col>

                <v-col cols="12" md="3" sm="3" xs="12">
                    <v-autocomplete
                        v-model="dadosGerais.fonteRecurso"
                        name="fonteRecurso"
                        placeholder="Selecione a fonte de recurso"
                        item-text="nome"
                        :items="fonteRecursos"
                        :filter="filtroComboAutoComplete"
                        v-validate="getObrigatorioRotulosPersonalizados('fonteRecurso', 'CONVENIO')? 'required' : ''"
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
                <v-col cols="12" md="3" sm="3" xs="12">
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
                <v-col cols="12" md="3" sm="3" xs="12">
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
                <v-col cols="12" md="6" sm="6" xs="12">
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
            <acoes-botoes-salvar-cancelar :camposAceitos="camposAceitos" @salvar="tratarEventoEditar" @cancelar="tratarEventoCancelar"/>
            </div>
        </v-container>
    </v-form>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import {createNamespacedHelpers} from 'vuex'
    import ConcedenteModal from '@/views/components/modal/configuracao/concedente/ConcedenteModal'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import moment from 'moment'
    import AcoesBotoesSalvarCancelar from '@/views/components/acoes/AcoesBotoesSalvarCancelar'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ConvenioDadosGeraisEdicao',
        $_veeValidate: {
            validator: 'new'
        },
        components: {AcoesBotoesSalvarCancelar, ConcedenteModal, LabelPersonalizado},
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
                fonteRecursos: [
                    {id: 1, nome: 'Aquisição de materiais'},
                    {id: 2, nome: 'Aquisição de protótipos'}
                ],
                buscaConcedenteDinamicamente: null,
                estaBuscandoConcedentes: false,
                camposAceitos: false,
                nomeTela: 'CONVENIO'
            }
        },
        computed: {
            ...mapGetters(
                ['getObrigatorioRotulosPersonalizados', 'getObjetoValidado']
            ),
            ehNomeConvenioVazio() {
                return this.dadosGerais.nome === ''
            }
        },
        async mounted() {
            await this.buscarConvenio()
            await this.validarDadosFormulario()
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
            }
        },
        methods: {
            async buscarConvenio() {
                if (this.$route.params.id) {
                    this.dadosGerais = await this.$store.dispatch(
                        actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID,
                        this.$route.params.id
                    )
                    this.formatarData()
                    if (this.dadosGerais.fonteRecurso) {
                        this.setarFonteRecurso()
                    }
                    if (this.dadosGerais.concedente) {
                        await this.buscarConcedente()
                    }
                }
            },
            formatarData() {
                this.dadosGerais.dataVigenciaInicio = moment(this.dadosGerais.dataVigenciaInicio).format('YYYY-MM-DDTHH:mm:ss.SSSZZ')
                this.dadosGerais.dataVigenciaFim = moment(this.dadosGerais.dataVigenciaFim).format('YYYY-MM-DDTHH:mm:ss.SSSZZ')
            },
            setarFonteRecurso() {
                const fonteRecurso = {id: 3, nome: this.dadosGerais.fonteRecurso}
                this.fonteRecursos.push(fonteRecurso)
            },
            async selecionarConcedente(concedente) {
                this.dadosGerais.concedente = concedente.id
                this.concedentes.push(concedente)
                this.tratarEventoFecharModal()
            },
            async buscarConcedente() {
                const concedente = await this.$store.dispatch(
                    actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTE_POR_ID,
                    this.dadosGerais.concedente
                )
                this.dadosGerais.concedente = concedente.id
                this.concedentes.push(concedente)
            },
            filtroComboAutoComplete(item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            filtroAutocompleteConcedentes(item, queryText) {
                const textOne = item.nome.toLowerCase().trim()
                const searchText = queryText.toLowerCase().trim()

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
            tratarEventoCancelar() {
                this.$router.push({name: this.$store.state.convenio.rota.origem})
            },
            tratarEventoFecharModal() {
                this.modalConcedente = false
            },
            async tratarEventoEditar() {
                if (this.camposAceitos) {
                    this.setMensagemLoading('Salvando alterações do convênio...')
                    await this.$store.dispatch(
                        actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO,
                        this.dadosGerais
                    )
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

<style lang="stylus">
    .checkbox_convenio
        i
            font-size 15px !important
            margin-left -5px

        .v-input--selection-controls__ripple
            border-radius 50%
            cursor pointer
            height 22px
            position absolute
            -webkit-transition inherit
            transition inherit
            width 23px
            left -11px
            top -6px
            margin 7px

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

    .btn-text-gerenciar-concedentes
        margin-left 5px
        font-weight bold
        font-size 14px !important
        color #777 !important

</style>
