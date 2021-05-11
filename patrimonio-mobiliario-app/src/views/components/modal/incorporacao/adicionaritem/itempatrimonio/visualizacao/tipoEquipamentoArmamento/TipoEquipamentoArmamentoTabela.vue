<template>
    <div>
        <v-layout column>
                <v-data-table
                    ref="table"
                    :headers="colunas"
                    :items="itens"
                    :server-items-length="totalItens"
                    :options.sync="paginacaoInterna"
                    :loading="false"
                    no-data-text="Não há registros de patrimônios"
                    class="tabela-modal-incorporacao-itens item-patrimonio-tabela"
                    must-sort
                    hide-default-footer>
                    <template v-slot:header.numero>
                        <label-personalizado campo="numero" :tela="nomeTela" :apenasNome="true"/>
                    </template>
                    <template v-slot:header.valorAquisicao>
                        <label-personalizado campo="valorAquisicao" :tela="nomeTela" :apenasNome="true"/>
                    </template>
                    <template v-slot:header.numeroSerie="item">
                        <label-personalizado campo="numeroSerie" :tela="nomeTela"/>
                    </template>
                    <template v-slot:item.numero="{item}">
                        <span class="d-inline-block text-truncate max-10">{{item.numero}}</span>
                    </template>
                    <template v-slot:item.valorAquisicao="{item}">
                        <span v-if="item.diferencaDizima" class="d-inline-block text-truncate max-13">
                            {{ item.valorAquisicao | valorParaDinheiro }}
                            <tooltip-informativo :tela="nomeTela" campos="valorAquisicao"
                                                 mensagemPadraoTooltip="Esse patrimônio recebeu um arrendondamento devido a dízimas periódicas"/>
                        </span>
                        <span v-else
                              class="d-inline-block text-truncate max-10">{{ item.valorAquisicao | valorParaDinheiro }}</span>
                    </template>
                    <template v-slot:item.numeroSerie="{item}">
                        <v-text-field
                            v-model="item.numeroSerie"
                            name="numeroSerie"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('numeroSerie', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('numeroSerie')"
                            dense/>
                    </template>
                </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage" class="mr-5 ml-5"/>
        </v-layout>
    </div>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import TooltipInformativo from '@/views/components/tooltip/TooltipInformativo'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'TipoEquipamentoArmamentoTabela',
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        components:{Paginacao, LabelPersonalizado, TooltipInformativo},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        computed:{
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            colunas(){
                return this.criarColunas(4,
                                         [],
                                         ['numero','valorAquisicao','numeroSerie','acoes'],
                                         [true,true,true,false],
                                         ['left','left','left','right'],
                                         ['10px','70px','50px','200px'],
                                         'gray--text')
            }
        },
        methods: {
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO)
            },
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

</style>
