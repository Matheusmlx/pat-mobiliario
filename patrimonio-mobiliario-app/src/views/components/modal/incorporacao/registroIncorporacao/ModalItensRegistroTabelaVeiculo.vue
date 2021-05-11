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
                <template v-slot:item.placa="{item}">
                    <span class="d-inline-block text-truncate max-10">{{formataPlaca(item.placa) | textoSemValor}}</span>
                </template>
                <template v-slot:item.renavam="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.renavam | textoSemValor}}</span>
                </template>
                <template v-slot:item.licenciamento="{item}">
                    <span class="d-inline-block text-truncate max-10" v-if="!item.licenciamento">{{item.licenciamento | textoSemValor}}</span>
                    <span class="d-inline-block text-truncate max-10" v-else>{{item.licenciamento | azEnum(mesesLicenciamento)}}</span>
                </template>
                <template v-slot:item.motor="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.motor | textoSemValor}}</span>
                </template>
                <template v-slot:item.chassi="{item}">
                    <span class="d-inline-block text-truncate max-10">{{formataChassi(item.chassi) | textoSemValor}}</span>
                </template>
            </v-data-table>
        </v-layout>
    </div>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import TooltipInformativo from '@/views/components/tooltip/TooltipInformativo'

    export default {
        name: 'ModalItensRegistroTabelaVeiculo',
        props: ['itens', 'paginacao', 'totalItens'],
        components:{ LabelPersonalizado, TooltipInformativo},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(8,
                                         [],
                                         ['uriImagem','numero','valorAquisicao','placa','renavam','licenciamento','motor','chassi'],
                                         [false,true,true,true,true,true,true,true],
                                         ['left','left','left','left','left','left','left','left'],
                                         ['10px','130px','140px','80px','100px','150px','100px','100px'],
                                         'gray--text')
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
        methods:{
            formataPlaca(placa) {
                let placaFormatada
                if(placa) {
                    placaFormatada = placa.slice(0, 3)
                    placaFormatada = placaFormatada + '-' + placa.slice(-4)
                }
                return placaFormatada
            },
            formataChassi(chassi) {
                let chassiFormatado
                if(chassi) {
                    chassiFormatado = chassi.slice(0, 3) + ' '
                    chassiFormatado = chassiFormatado + chassi.slice(3, 8) + ' '
                    chassiFormatado = chassiFormatado + chassi.slice(8, 9) + ' '
                    chassiFormatado = chassiFormatado + chassi.slice(9, 17)
                }
                return chassiFormatado
            },
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
