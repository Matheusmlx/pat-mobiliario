<template>
    <az-container>
        <az-form>
            <v-data-table
                ref="table"
                :headers="colunas"
                :items="itens"
                :server-items-length="totalItens"
                :options.sync="paginacaoInterna"
                :loading="false"
                no-data-text="Não há reservas cadastradas"
                class="az-table-list pr-tabela-todos-intervalos-reserva"
                hide-default-footer>
                <template v-slot:item.codigo="{item}">
                    <span class="d-inline-block text-truncate max-10">{{ item.codigo }}</span>
                </template>
                <template v-slot:item.orgao="{item}">
                    <span class="d-inline-block text-truncate max-25">{{ item.orgao }}</span>
                </template>
                <template v-slot:item.quantidadeReservada="{item}">
                    <span class="d-inline-block text-truncate max-10">{{ item.quantidadeReservada | formataValor }}</span>
                </template>
                <template v-slot:item.preenchimento="{item}">
                        <span class="d-inline-block text-truncate max-10">
                            {{ item.preenchimento | azEnum(preenchimentoReserva) }}
                        </span>
                </template>
                <template v-slot:item.intervalo="{item}">
                        <span class="d-inline-block text-truncate max-10">
                            {{ item.numeroInicio }} - {{ item.numeroFim }}
                        </span>
                </template>
                <template v-slot:item.acoes="{item}">
                    <botao-remover v-if="item.situacao === 'EM_ELABORACAO'" class="botaoRemover"
                                   @remover="removerIntervalo(item.id)"/>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import Paginacao from '@/views/components/tabela/Paginacao'
    import {mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'

    export default {
        name: 'ReservaIntervaloListagemTabela',
        components: {BotaoRemover, Paginacao},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao
            }
        },
        created() {
            this.resetaPage()
        },
        computed: {
            colunas() {
                return this.criarColunas(
                    6,
                    ['Código', 'Órgão', 'Quantidade', 'Preenchimento', 'Intervalo', ''],
                    ['codigo', 'orgao', 'quantidadeReservada', 'preenchimento', 'intervalo', 'acoes'],
                    [false, false, false, false, false, false],
                    ['left', 'left', 'left', 'left', 'left', 'right'],
                    ['10%', '35%', '14%', '16%', '20%', '5%'],
                    'gray--text')
            }
        },
        methods: {
            ...mapMutations([mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]),
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.resetaPageReservaIntervalo()
            },
            removerIntervalo(intervaloId) {
                this.$emit('removerIntervalo', intervaloId)
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true,
            },
        }
    }
</script>

<style lang="stylus">
    .pr-tabela-todos-intervalos-reserva
        cursor default !important
        min-height 42vh

    .pr-tabela-todos-intervalos-reserva tbody tr:hover
        cursor default

    .pr-tabela-todos-intervalos-reserva tbody tr
        height 60px

    .botaoRemover
        display flex
        padding 0 15% 2% 60%
        margin-bottom 8px

</style>
