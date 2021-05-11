<template>
    <v-data>
        <tr>
            <template v-if="!modoEdicao">
                <td class="text-truncate max-15">{{ value.nome }}</td>
                <td class="td-situacao">{{ value.situacao | azEnum(situacoesReconhecimento) }}</td>
                <td>
                    <div class="mt-0">{{ execucaoOrcamentaria }}</div>
                </td>
                <td>
                    <div class="mt-0">{{ camposObrigatorios }}</div>
                </td>
                <td>
                    <botao-icone-editar v-if="permitirAcoes" @ativarEdicao="ativarModoEdicao" />
                </td>
            </template>
            <template v-if="modoEdicao || estaInserindo">
                <td class="v-data-table_selected">
                    <v-text-field
                        v-model="value.nome"
                        name="nome"
                        :error-messages="errors.collect('nome')"
                        placeholder="Informe o nome"
                        v-validate="'required|max:100'"
                        counter="100"
                        maxlength="100"
                        data-vv-as="nome"
                        dense
                        class="inputNome"
                        @keyup.enter="salvar"
                        @keyup.esc="cancelar"
                        @click:append="cancelar"
                        @click:clear="salvar"/>
                </td>
                <td class="v-data-table_selected">
                    <v-radio-group
                        v-validate="'required'"
                        v-model="value.situacao"
                        class="checkbox"
                        row>
                        <v-radio label="Ativo" value="ATIVO"/>
                        <v-radio label="Inativo" value="INATIVO"/>
                    </v-radio-group>
                </td>
                <td class="v-data-table_selected">
                    <v-radio-group
                        v-validate="'required'"
                        class="checkbox checkbox_execucao_orcamentaria"
                        name="execucaoOrcamentaria"
                        :error-messages="errors.collect('execucaoOrcamentaria')"
                        v-model="value.execucaoOrcamentaria"
                        row>
                        <v-radio label="Sim" :value="true"/>
                        <v-radio label="Não" :value="false"/>
                    </v-radio-group>
                </td>
                <td class="v-data-table_selected">
                    <v-row class="checkbox_campos_obrigatorios">
                        <v-checkbox v-model="value.empenho" label="Empenho"/>
                        <v-checkbox v-model="value.notaFiscal" label="Nota fiscal"/>
                    </v-row>
                </td>
                <td class="v-data-table_selected">
                        <acoes-icones-salvar-cancelar class="pb-3" @salvar="salvar" @cancelar="cancelar"/>
                </td>
            </template>
        </tr>
    </v-data>
</template>

<script>
    import {mapState} from 'vuex'
    import AcoesIconesSalvarCancelar from '@/views/components/acoes/AcoesIconesSalvarCancelar'
    import BotaoIconeEditar from '@/views/components/botoes/BotaoIconeEditar'

    export default {
        name: 'ReconhecimentoForm',
        components:{AcoesIconesSalvarCancelar, BotaoIconeEditar},
        props: {
            value: {
                required: true
            },
            permitirAcoes: {
                required: true
            }
        },
        data: () => ({
            modoEdicao: false,
        }),
        watch: {
            'value.nome': {
                handler(nome) {
                    if (nome.length > 100) {
                        this.value.nome = nome.slice(0, 100)
                    }
                }
            },
            'value'() {
                this.desativarModoEdicao()
            },
            'todosReconhecimentos.filtros.conteudo'() {
                this.habilitarAcoes()
                this.desativarModoEdicao()
                this.$emit('habilitaBotaoNovo')
            },
            'todosReconhecimentos.paginacao'() {
                this.habilitarAcoes()
                this.desativarModoEdicao()
                this.$emit('habilitaBotaoNovo')
            }
        },
        computed: {
            ...mapState({todosReconhecimentos: state => state.reconhecimento.todosReconhecimentos}),
            estaInserindo() {
                if (!this.value.id) {
                    this.ativarModoEdicao()
                }
                return !this.value.id
            },
            execucaoOrcamentaria() {
                return this.value.execucaoOrcamentaria ? 'Sim' : 'Não'
            },
            camposObrigatorios() {
                if (this.value.notaFiscal && this.value.empenho) {
                    return 'Empenho, Nota Fiscal'
                }
                if (this.value.notaFiscal) {
                    return 'Nota Fiscal'
                }
                if (this.value.empenho) {
                    return 'Empenho'
                }
                return '----'
            }
        },
        methods: {
            ativarModoEdicao() {
                this.modoEdicao = true
                this.$validator.resume()
                this.desativarAcoes()
                this.$emit('desabilitaBotaoNovo')
            },
            cancelar() {
                this.desativarModoEdicao()
                this.$emit('cancelarEdicao')
            },
            desativarModoEdicao() {
                this.$validator.pause()
                this.modoEdicao = false
            },
            desativarAcoes() {
                this.$emit('desativarAcoes')
            },
            habilitarAcoes() {
                this.$emit('habilitarAcoes')
            },
            async salvar() {
                if (await this.validarDadosFormulario()) {
                    if (this.estaInserindo) {
                        this.$emit('inserirNovoReconhecimento', this.value)
                    } else {
                        this.verificarSeCamposNulos()
                        this.$emit('atualizarReconhecimento', this.value)
                    }
                }
            },
            async validarDadosFormulario() {
                return await this.$validator._base.validateAll()
            },
            verificarSeCamposNulos() {
                if (!this.value.notaFiscal) {
                    this.value.notaFiscal = false
                }
                if (!this.value.empenho) {
                    this.value.empenho = false
                }
            }
        }
    }
</script>

<style lang="stylus">
    .az-table-list
        td
            padding 0 10px !important

    .habilitarEdicao
        button
            visibility visible

    .desabilitarEdicao
        button
            visibility hidden !important

    .v-input--selection-controls__ripple
        border-radius: 50%
        cursor: pointer
        height: 22px
        position: absolute
        -webkit-transition: inherit
        transition: inherit
        width: 23px
        left: -11px
        top: -6px
        margin: 7px

    .label_input
        font-size 15px
        font-weight bold
        color #777 !important

    .inputNome
        margin-top 3px
        padding-top 3px

    .checkbox
        i
            font-size 15px !important
            margin-left -5px

        label
            font-size 13px
            margin-left -5px

    .checkbox_execucao_orcamentaria
        margin-bottom 3px !important
        display inline-flex

    .checkbox_campos_obrigatorios
        padding-bottom 8px
        i
            font-size 15px !important
            margin-left 12px

        label
            font-size 13px
            margin-left 5px
            margin-right 5px

        .v-input--selection-controls__ripple
            left 0

        .v-input__control
            height 25px

        .v-input--checkbox
            padding-top 0

        button
            margin 0 !important
            padding 0 !important
            visibility visible !important
            display inline-flex !important

    .max-20
        max-width 20vw

    @media (max-width 720px)
        .inputNome
            margin-top 0 !important

    .v-btn--icon.v-size--default .v-icon, .v-btn--fab.v-size--default .v-icon
        width 2px !important;
</style>
