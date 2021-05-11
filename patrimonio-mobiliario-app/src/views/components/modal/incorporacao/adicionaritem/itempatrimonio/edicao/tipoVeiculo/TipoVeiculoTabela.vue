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
                    <template v-slot:header.chassi>
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
                              class="d-inline-block text-truncate max-13">{{ item.valorAquisicao | valorParaDinheiro }}</span>
                    </template>
                    <template v-slot:item.placa="{item}">
                        <v-text-field
                            v-model="item.placa"
                            :name="`placa${item.id}`"
                            data-vv-as="placa"
                            placeholder="Informe a placa"
                            maxlength="8"
                            v-mask="mascaraPlaca"
                            v-validate="getObrigatorioRotulosPersonalizados('placa', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:8' : ''"
                            :error-messages="errors.collect(`placa${item.id}`)"
                            dense
                            @input="item.placa = item.placa.toUpperCase()"
                            @focusout="atualizarItem(item)"/>
                    </template>
                    <template v-slot:item.renavam="{item}">
                        <v-text-field
                            v-model="item.renavam"
                            :name="`renavam${item.id}`"
                            data-vv-as="renavam"
                            placeholder="Informe o renavam"
                            maxlength="17"
                            v-mask="mascaraRenavam"
                            v-validate="getObrigatorioRotulosPersonalizados('renavam', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:17' : ''"
                            :error-messages="errors.collect(`renavam${item.id}`)"
                            dense
                            @focusout="atualizarItem(item)"/>
                    </template>
                    <template v-slot:item.licenciamento="{item}">
                        <az-combo-enum
                            v-model="item.licenciamento"
                            :enum-object="mesesLicenciamento"
                            :name="`licenciamento${item.id}`"
                            data-vv-as="licenciamento"
                            class="comboLicenciamento"
                            placeholder="Informe o licenciamento"
                            :is-required="getObrigatorioRotulosPersonalizados('licenciamento', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO')"
                            :error-messages="errors.collect(`licenciamento${item.id}`)"
                            :orderBy="''"
                            dense
                            @input="atualizarItem(item)"/>
                    </template>
                    <template v-slot:item.motor="{item}">
                        <v-text-field
                            v-model="item.motor"
                            :name="`motor${item.id}`"
                            data-vv-as="motor"
                            placeholder="Informe o motor"
                            maxlength="17"
                            v-mask="mascaraMotor"
                            v-validate="getObrigatorioRotulosPersonalizados('motor', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:17' : ''"
                            :error-messages="errors.collect(`motor${item.id}`)"
                            dense
                            @focusout="atualizarItem(item)"/>
                    </template>
                    <template v-slot:item.chassi="{item}">
                        <v-text-field
                            v-model="item.chassi"
                            :name="`chassi${item.id}`"
                            data-vv-as="chassi"
                            placeholder="Informe o chassi"
                            maxlength="20"
                            v-mask="mascaraChassi"
                            v-validate="getObrigatorioRotulosPersonalizados('chassi', 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO') ? 'required|max:20' : ''"
                            :error-messages="errors.collect(`chassi${item.id}`)"
                            dense
                            @focusout="atualizarItem(item)"/>
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
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        components:{Paginacao, LabelPersonalizado, TooltipInformativo},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                mascaraPlaca: 'AAA-#X##',
                mascaraChassi:'XXX XXXXX X XXXXXXXX',
                mascaraRenavam: '#################',
                mascaraMotor: 'XXXXXXXXXXXXXXXXX',
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
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO)
            },
            atualizarItem(patrimonio) {
                this.$emit('atualizarPatrimonio', patrimonio)
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
        .comboLicenciamento
            padding-top 0
            .v-select__selection
                color #858585!important
                font-size 13px
                margin-bottom 0
            .v-input__icon .v-icon
                font-size 22px
                margin-bottom 0

</style>
