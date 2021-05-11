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
                class="az-table-list pr-tabela-todas-reservas listagem-tabela"
                hide-default-footer
                @click:row="tratarEventoAcessar">
                <template v-slot:item.codigo="{item}">
                    <span class="d-inline-block text-truncate max-10">{{ item.codigo }}</span>
                </template>
                <template v-slot:item.dataCriacao="{item}">
                    <span class="d-inline-block text-truncate max-10">{{ item.dataCriacao | azDate }}</span>
                </template>
                <template v-slot:item.quantidadeReservada="{item}">
                    <span class="d-inline-block text-truncate max-10">
                        {{ item.quantidadeReservada | formataValor}}
                    </span>
                </template>
                <template v-slot:item.intervalo="{item}">
                        <span class="d-inline-block text-truncate max-15">
                            {{ item.numeroInicio}} - {{item.numeroFim}}
                        </span>
                </template>
                <template v-slot:item.orgaos="{item}">
                    <span class="d-inline-block text-truncate max-20" v-if="item.orgaos && item.orgaos.length">
                        {{formataOrgaoParaExibicao(item.orgaos)}}
                    </span>
                </template>
                <template v-slot:item.acoes="{  }">
                    <v-icon>keyboard_arrow_right</v-icon>
                    <span class="mobile">Acessar</span>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import {mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import TooltipInformativo from '@/views/components/tooltip/TooltipInformativo'

    export default {
        name: 'ReservaListagemTabela',
        components: {Paginacao, TooltipInformativo},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao
            }
        },
        computed: {
            colunas() {
                return this.criarColunas(
                    6,
                    ['Código', 'Criação', 'Quantidade', 'Intervalo', 'Órgão', ''],
                    ['codigo', 'dataCriacao', 'quantidadeReservada', 'intervalo', 'orgaos', 'acoes'],
                    [false, false, false, false, false, false],
                    ['left', 'left', 'left', 'left', 'left', 'right'],
                    ['14%', '17%', '17%', '17%', '30%', '5%'],
                    'gray--text')
            }
        },
        methods: {
            ...mapMutations([mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA]),
            tratarEventoAcessar(item) {
                this.$emit('acessar', item)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.resetaPageReserva()
            },
            formataOrgaoParaExibicao(orgaos) {
                return orgaos[0]
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true,
            },
        },
    }
</script>

<style lang="stylus">
    .tooltipRestante .iconeTooltipInformativo
        margin-left 3px !important
        margin-bottom 0px !important
        margin-top 2px !important
</style>
