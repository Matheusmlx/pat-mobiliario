<template>
    <v-data>
        <tr>
            <template v-if="!modoEdicao">
                <td>{{ formatarCpfCnpj }}</td>
                <td>{{ dadosGerais.nome }}</td>
                <td>{{ dadosGerais.situacao | azEnum(situacoesConcedente) }}</td>
                <td>
                    <div v-if="(!concedenteEditando)">
                        <acoes-icones-tooltip-editar-selecionar
                            :desabilitado="!podeSelecionar"
                            @ativarEdicao="ativarModoEdicao"
                            @selecionar="selecionarConcedente"/>
                    </div>
                </td>
            </template>
            <template v-if="modoEdicao || estaInserindo">
                <td class="v-data-table_selected">
                    <v-text-field
                        class="input_concedente"
                        v-model="dadosGerais.cpfCnpj"
                        name="cpfCnpj"
                        :error-messages="errors.collect('cpfCnpj')"
                        v-validate="'required'"
                        v-mask="[masks.cpf, masks.cnpj]"
                        placeholder="Informe o CPF/CNPJ">
                    </v-text-field>
                </td>
                <td class="v-data-table_selected">
                        <v-text-field
                            class="input_concedente"
                            v-model="dadosGerais.nome"
                            name="nome"
                            counter="100"
                            maxlength="100"
                            v-validate="'required|max:100'"
                            :error-messages="errors.collect('nome')"
                            placeholder="Informe o concedente">
                        </v-text-field>
                </td>
                <td class="v-data-table_selected">
                    <div class="mt-3">
                        <v-radio-group
                            v-model="dadosGerais.situacao"
                            name="situacao"
                            :error-messages="errors.collect('situacao')"
                            class="checkbox_situacao"
                            v-validate="'required'"
                            row>
                            <v-radio label="Ativo" value="ATIVO"/>
                            <v-radio label="Inativo" value="INATIVO"/>
                        </v-radio-group>
                    </div>
                </td>
                <td class="v-data-table_selected">
                    <acoes-icones-salvar-cancelar @salvar="salvarConcedente" @cancelar="cancelarModoEdicao"/>
                </td>
            </template>
        </tr>
    </v-data>
</template>

<script>
    import {mapState} from 'vuex'
    import _ from 'lodash'
    import AcoesIconesSalvarCancelar from '@/views/components/acoes/AcoesIconesSalvarCancelar'
    import AcoesIconesTooltipEditarSelecionar from '@/views/components/acoes/AcoesIconesTooltipEditarSelecionar'

    export default {
        name: 'ConcedenteModalTabela',
        props: ['value','concedenteEditando'],
        components:{AcoesIconesTooltipEditarSelecionar, AcoesIconesSalvarCancelar},
        data() {
            return {
                masks: {
                    cpf: '###.###.###-##',
                    cnpj: '##.###.###/####-##'
                },
                modoEdicao: false,
                dadosGerais: this.value
            }
        },
        watch: {
            'value'(concedentes) {
                this.dadosGerais = _.cloneDeep(concedentes)
            },
            'concedente.todosConcedentes.filtros.conteudo.value'() {
                this.desativarModoEdicao()
                this.$emit('habilitaBotaoNovo')
            },
            'concedente.todosConcedentes.paginacao': {
                handler() {
                    this.desativarModoEdicao()
                    this.$emit('habilitaBotaoNovo')
                },
                deep: true
            }
        },
        computed: {
            ...mapState(['concedente']),
            estaInserindo() {
                if (!this.dadosGerais.id) {
                    this.ativarModoEdicao()
                }
                return !this.dadosGerais.id
            },
            formatarCpfCnpj() {
                if (this.dadosGerais.cpfCnpj) {
                    const cnpjCpf = this.dadosGerais.cpfCnpj.replace(/\D/g, '')
                    if (cnpjCpf.length === 11) {
                        return cnpjCpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/g, '$1.$2.$3-$4')
                    }
                    return cnpjCpf.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/g, '$1.$2.$3/$4-$5')
                }
                return ''
            },
            podeSelecionar(){
                return !!(this.value.situacao === 'ATIVO')
            }
        },
        methods: {
            ativarModoEdicao() {
                this.modoEdicao = true
                this.$validator.resume()
                this.$emit('desabilitaBotaoNovo')
                this.setaEditando()
            },
            setaEditando(){
                this.$emit('setaEditando',true)
            },
            retiraEditando(){
                this.$emit('setaEditando',false)
            },
            desativarModoEdicao() {
                this.modoEdicao = false
                this.$validator.pause()
                this.retiraEditando()
            },
            cancelarModoEdicao() {
                this.desativarModoEdicao()
                this.$emit('cancelar')
            },
            async validarDadosFormulario() {
                return await this.$validator.validateAll()
            },
            removerCaracteresEspeciais() {
                this.dadosGerais.cpfCnpj = this.dadosGerais.cpfCnpj.replace(/[./-]/g, '')
            },
            async salvarConcedente() {
                if (await this.validarDadosFormulario()) {
                    this.cancelarModoEdicao()
                    this.removerCaracteresEspeciais()
                    if (this.estaInserindo) {
                        this.$emit('salvarConcedente', this.dadosGerais)
                    } else {
                        this.$emit('editarConcedente', this.dadosGerais)
                    }
                }
            },
            selecionarConcedente() {
                this.$emit('selecionarConcedente', this.dadosGerais)
            }
        }
    }
</script>

<style lang="stylus">
    .input_concedente
        margin-top 3px
        padding-top 3px

    .checkbox_situacao
        i
            font-size 15px !important
            margin-left -5px

        label
            font-size 13px
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

    .label_input
        font-size 15px
        font-weight bold
        color #777 !important

    .text__field
        font-size 14px
</style>
