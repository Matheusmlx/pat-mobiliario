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
                    <template v-slot:header.placa>
                        <label-personalizado campo="placa" :tela="nomeTela"/>
                    </template>
                    <template v-slot:header.renavam>
                        <label-personalizado campo="renavam" :tela="nomeTela"/>
                    </template>
                    <template v-slot:header.licenciamento>
                        <label-personalizado campo="licenciamento" :tela="nomeTela"/>
                    </template>
                    <template v-slot:header.motor>
                        <label-personalizado campo="motor" :tela="nomeTela"/>
                    </template>
                    <template v-slot:header.chassi="item">
                        <label-personalizado campo="chassi" :tela="nomeTela"/>
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
                    <template v-slot:item.placa="{item}">
                        <v-text-field
                            v-model="item.placa"
                            name="placa"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('placa', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('placa')"
                            dense/>
                    </template>
                    <template v-slot:item.renavam="{item}">
                        <v-text-field
                            v-model="item.renavam"
                            name="renavam"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('renavam', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('renavam')"
                            dense/>
                    </template>
                    <template v-slot:item.licenciamento="{item}">
                        <v-text-field
                            v-model="item.licenciamento"
                            name="licenciamento"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('licenciamento', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('licenciamento')"
                            dense/>
                    </template>
                    <template v-slot:item.motor="{item}">
                        <v-text-field
                            v-model="item.motor"
                            name="motor"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('motor', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('motor')"
                            dense/>
                    </template>
                    <template v-slot:item.chassi="{item}">
                        <v-text-field
                            v-model="item.chassi"
                            name="chassi"
                            disabled
                            placeholder=" "
                            maxlength="50"
                            v-validate="getObrigatorioRotulosPersonalizados('chassi', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:50' : ''"
                            :error-messages="errors.collect('chassi')"
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
        name: 'TipoVeiculoTabela',
        components: {Paginacao, LabelPersonalizado, TooltipInformativo},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        computed:{
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            colunas(){
                return this.criarColunas(7,
                                         [],
                                         ['numero','valorAquisicao','placa','renavam','licenciamento','motor','chassi'],
                                         [true,true,true,true,true,true,true],
                                         ['left','left','left','left','left','left','left'],
                                         ['13%','14%','12%','14%','16%','13%','15%'],
                                         'gray--text')
            }
        },
        methods: {
            tratarPaginacaoVisualizacao(pagina) {
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
