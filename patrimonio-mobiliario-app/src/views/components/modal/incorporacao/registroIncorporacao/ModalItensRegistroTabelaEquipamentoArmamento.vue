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
                class="tabela-modal-incorporacao-itens"
                @click:row="tratarEventoRedirecionarFichaPatrimonio"
                must-sort
                hide-default-footer>
                <template v-slot:header.uriImagem>
                    <label-personalizado campo="uriImagem" :tela="'ITEM_INCORPORACAO'"/>
                </template>
                <template v-slot:header.numero>
                    <label-personalizado campo="numero" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.valorAquisicao>
                    <label-personalizado campo="valorAquisicao" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.numeroSerie>
                    <label-personalizado campo="numeroSerie" :tela="nomeTela"/>
                </template>
                <template v-slot:item.uriImagem="{item}">
                    <v-img :src="'data:image/png;base64,' + item.imagem" max-width="40" max-height="40" aspect-ratio="1" class="img-patrimonio-registro"/>
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
                <template v-slot:item.numeroSerie="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.numeroSerie | textoSemValor}}</span>
                </template>
            </v-data-table>
        </v-layout>
    </div>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import TooltipInformativo from '@/views/components/tooltip/TooltipInformativo'

    export default {
        name: 'ModalItensRegistroTabelaEquipamentoArmamento',
        props: ['itens', 'paginacao', 'totalItens'],
        components:{LabelPersonalizado, TooltipInformativo},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
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
        computed:{
            colunas(){
                return this.criarColunas(5,
                                         [],
                                         ['uriImagem','numero','valorAquisicao','numeroSerie','acoes'],
                                         [false,true,true,true,false],
                                         ['left','left','left','left','right'],
                                         ['10px','80px','120px','120px','300px'],
                                         'gray--text')
            }
        },
        methods: {
            tratarEventoRedirecionarFichaPatrimonio(item) {
                this.$emit('redirecionarFichaPatrimonio', item)
            }
        }
    }
</script>

<style lang="stylus">
    .img-patrimonio-registro
        border 1px solid #777
</style>
