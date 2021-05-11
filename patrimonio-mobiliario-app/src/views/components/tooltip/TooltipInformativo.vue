<template>
    <span class="az-info">
        <v-menu
            max-width="500"
            open-on-hover
            offset-y
            top
            nudge-top="5"
            :nudge-left="nudgeLeft"
            content-class="elevation-0">
            <template v-slot:activator="{on}">
                <v-icon class="iconeTooltipInformativo" size="15" v-on="on" :color="color" >{{icon}}</v-icon>
            </template>

            <div class="az-info-content" v-if="campo && tela && getTooltipRotulosPersonalizados(campo,tela) && !mensagemPadraoTooltip">
                {{getTooltipRotulosPersonalizados(campo,tela)}}
            </div>
            <div class="az-info-content" v-if="mensagemPadraoTooltip">
                {{mensagemPadraoTooltip}}
            </div>
        </v-menu>
    </span>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        props: {
            campo: {
                type: String,
            },
            tela: {
                type: String,
            },
            mensagemPadraoTooltip: {
                type: String,
            },
            icon: {
                type: String,
                default: 'help_outline'
            },
            color: {
                type: String,
                default: ''
            },
            nudgeLeft: {
                type: Number,
                default: 0
            }
        },
        computed:{
            ...mapGetters([
                'getTooltipRotulosPersonalizados'
            ]),
        }
    }
</script>

<style lang="stylus" scoped>
    .iconeTooltipInformativo
      margin-left 4px
      margin-bottom 4px
    .az-info
        .v-menu__content
            background red
        &-content
            padding 10px
            font-size 12px
            opacity 1 !important
            background #333333 !important
            text-align justify !important
            line-height 1.4
            color white
</style>
