<template>
    <div class="label-personalizado">
        {{getNomeRotulosPersonalizados(campo,tela)}} {{complementoLabel}}
        <span
            v-if="!apenasNome && ((permissaoEdicao && getObrigatorioRotulosPersonalizados(campo, tela)) || (permissaoEdicao && obrigatorioPorPadrao))"
            class="ml-1 red--text">*</span>
        <tooltip-informativo v-if="apresentaTooltip" :campo="campo" :tela="tela" :mensagemPadraoTooltip="mensagemPadraoTooltip"/>
    </div>
</template>

<script>
    import TooltipInformativo from '@/views/components/tooltip/TooltipInformativo'
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'TemplateSlotLabel',
        props: {
            campo: {
                type: String,
                required: true
            },
            tela: {
                type: String,
                required: true
            },
            obrigatorioPorPadrao: {
                type: Boolean,
                default: false
            },
            apresentaTooltip: {
                type: Boolean,
                default: false
            },
            apenasNome: {
                type: Boolean,
                default: false
            },
            permissaoEdicao: {
                type: Boolean,
                default: true
            },
            complementoLabel: {
                type: String,
                default: ''
            },
            mensagemPadraoTooltip:{
                type: String
            }
        },
        components:{TooltipInformativo},
        computed: {
            ...mapGetters([
                'getNomeRotulosPersonalizados',
                'getObrigatorioRotulosPersonalizados',
                'getTooltipRotulosPersonalizados'
            ]),
        }
    }
</script>

<style lang="stylus">
.label-personalizado
    display inline
</style>
