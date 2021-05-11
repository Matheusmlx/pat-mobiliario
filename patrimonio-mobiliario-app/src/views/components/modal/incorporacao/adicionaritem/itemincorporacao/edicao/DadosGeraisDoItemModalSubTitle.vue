<template>
    <v-container class="header ml-0 mr-0">
        <v-row class="rowSubtitle">
            <v-col class="headerItems headerIncorporacao" cols="2">
                <span class="font-weight-bold">Código</span>
                <p class="mb-0 mt-1">{{value.codigo}}</p>
            </v-col>
            <v-col cols="10" class="headerItensDescricao">
                <v-tooltip top nudge-top max-width="800">
                    <template v-slot:activator="{ on }">
                        <div v-on="on">
                            <span class="font-weight-bold">Descrição</span>
                            <p class="mb-0 mt-1 text-truncate">{{value.descricao}}</p>
                        </div>
                    </template>
                    {{value.descricao}}
                </v-tooltip>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    export default {
        props: ['value']

    }
</script>

<style lang="stylus">
    .headerItems
        font-size 14.4px !important
        color #838383
        max-height 65px
        min-width 170px
        max-width 170px

    .header
        background #FFF !important

    .headerItensDescricao
        margin-bottom 12px
        font-size 14.4px !important
        color #838383
        display inline-block

    .headerItensDescricao p
        max-width 790px

    .outlineClass
        max-height 52%
        padding-left 2%
        padding-right 2%
        border #a0a0a0 solid 1px
        border-radius 5px

    .rowSubtitle
        margin-right 0px !important
        margin-left 14px !important


</style>
