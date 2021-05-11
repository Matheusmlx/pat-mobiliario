<template>
    <div>
        <v-layout column style="height: 50vh">
            <v-flex style="overflow: auto">
                <v-data-table :headers="colunas"
                              :items="value"
                              :loading="false"
                              :server-items-length="totalItens"
                              :options.sync="paginacaoInterna"
                              class="az-table-list pr-tabela-todos-convenios"
                              hide-default-footer
                              disable-pagination
                              must-sort
                              no-data-text="Não há concedentes cadastrados">
                    <template v-slot:header.cpfCnpj>
                        <label-personalizado campo="cpfCnpj" :tela="nomeTela" :apenasNome="true"/>
                    </template>
                    <template v-slot:header.nome>
                        <label-personalizado campo="nome" :tela="nomeTela" :apenasNome="true"/>
                    </template>
                    <template v-slot:header.situacao>
                        <label-personalizado campo="situacao" :tela="nomeTela" :apenasNome="true"/>
                    </template>
                    <template v-slot:item="{ item }">
                        <concedente-modal-formulario
                            v-model="item"
                            :concedente-editando="concedenteEditando"
                            @cancelar="cancelar"
                            @salvarConcedente="salvarConcedente"
                            @selecionarConcedente="selecionarConcedente"
                            @editarConcedente="editarConcedente"
                            @setaEditando="setaEditando"
                            @desabilitaBotaoNovo="desabilitaBotaoNovo"
                            @habilitaBotaoNovo="habilitaBotaoNovo"/>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
        <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
    </div>
</template>

<script>
    import ConcedenteModalFormulario from './ConcedenteModalFormulario'
    import {createNamespacedHelpers} from 'vuex'
    import mutationTypes from '@/core/constants/mutationTypes'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ConcedenteModalListagem',
        props: ['value', 'paginas', 'paginacao', 'totalItens'],
        components: {Paginacao, ConcedenteModalFormulario, LabelPersonalizado},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                linhasPorPagina: [10, 25, 50, 100],
                concedenteEditando: false,
                nomeTela: 'CONCEDENTE'
            }
        },
        computed: {
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            colunas(){
                return this.criarColunas(4,
                                         [],
                                         ['cpfCnpj','nome','situacao','acoes'],
                                         [true,true,true,false],
                                         ['left','left','left','left'],
                                         ['20%','40%','25%','10%'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        methods: {
            cancelar() {
                this.$emit('cancelar')
            },
            editarConcedente(concedente) {
                this.$emit('editarConcedente', concedente)
            },
            salvarConcedente(concedente) {
                this.$emit('salvarConcedente', concedente)
            },
            selecionarConcedente(concedente) {
                this.$emit('selecionarConcedente', concedente)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE)
            },
            setaEditando(concedente){
                this.concedenteEditando = concedente
            },
            desabilitaBotaoNovo(){
                this.$emit('desabilitaBotaoNovo')
            },
            habilitaBotaoNovo(){
                this.$emit('habilitaBotaoNovo')
            }
        }
    }
</script>

<style lang="stylus">
    .v-data-table_selected
        background #eeeeee !important

        .v-input__slot::before
            border-color rgba(0, 0, 0, 0.87) !important
</style>
