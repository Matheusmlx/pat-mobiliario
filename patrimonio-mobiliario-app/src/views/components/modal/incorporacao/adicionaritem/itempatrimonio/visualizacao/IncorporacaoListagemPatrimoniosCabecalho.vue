<template>
    <v-container fluid grid-list-xl white>
        <div class="modal-header">
            <v-simple-table dense class="cabecalho-modal3-desktop">
                <template v-slot:default>
                    <thead>
                    <tr>
                        <th><span class="font-header-cabecalho">Código</span></th>
                        <th><span class="font-header-cabecalho">Quantidade</span></th>
                        <th><span class="font-header-cabecalho">Descrição</span></th>
                        <th><span class="font-header-cabecalho">Numeração Patrimonial</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><label class="text-header">{{item.codigo}}</label></td>
                        <td><label class="text-header">{{item.quantidade}}</label></td>
                        <td>
                            <v-tooltip top max-width="500">
                                <template v-slot:activator="{ on, attrs }">
                                    <label class="d-inline-block text-truncate max-30 text-header" v-bind="attrs"
                                           v-on="on">{{item.descricao}}</label>
                                </template>
                                <span>{{item.descricao}}</span>
                            </v-tooltip>
                        </td>
                        <td><label class="text-header">{{item.numeracaoPatrimonial | azEnum(numeracaoPatrimonial) }}</label></td>
                    </tr>
                    </tbody>
                </template>
            </v-simple-table>
            <v-container fluid grid-list-xl white class="cabecalho-modal3-mobile">
                <div class="modal-header">
                    <v-row wrap align-center white class="pl-10 pr-10">
                        <v-col cols="12" md="2" sm="2" xs="12">
                            <v-row><h3 class="font-header-cabecalho">Código</h3></v-row>
                            <v-row><label class="text-header">{{item.codigo}}</label></v-row>
                        </v-col>
                        <v-col cols="12" md="2" sm="2" xs="12">
                            <v-row><h3 class="font-header-cabecalho">Quantidade</h3></v-row>
                            <v-row><label class="text-header">{{item.quantidade}}</label></v-row>
                        </v-col>
                        <v-col cols="12" md="5" sm="5" xs="12">
                            <v-tooltip top>
                                <template v-slot:activator="{ on, attrs }">
                                    <v-row><h3 class="font-header-cabecalho">Descrição</h3></v-row>
                                    <v-row v-bind="attrs" v-on="on">
                                        <label class="d-inline-block text-truncate max-30 text-header">{{item.descricao}}</label>
                                    </v-row>
                                </template>
                                <span>{{item.descricao}}1</span>
                            </v-tooltip>
                        </v-col>
                        <v-col cols="12" md="3" sm="3" xs="12">
                            <v-row><h3 class="font-header-cabecalho">Numeração Patrimonial</h3></v-row>
                            <v-row><label class="text-header">{{item.numeracaoPatrimonial}}</label></v-row>
                        </v-col>
                    </v-row>
                </div>
            </v-container>
        </div>
    </v-container>
</template>

<script>
    export default {
        name: 'IncorporacaoModalEtapa3Cabecalho',
        props: ['item'],
    }
</script>

<style scoped lang="stylus">
    .modal-header
        margin-bottom 5px
        padding-bottom 5px
        width 100%

        .theme--light.v-data-table > .v-data-table__wrapper > table > thead > tr > th
            padding 12px 0px 0px 0px

        .theme--light.v-data-table > .v-data-table__wrapper > table > thead > tr:last-child > th
            border none
            cursor default

        .theme--light.v-data-table > .v-data-table__wrapper > table > tbody > tr:hover
            background-color white
            cursor text

        .theme--light.v-data-table > .v-data-table__wrapper > table > tbody > tr > td
            padding 0px
            margin 0px

    .font-header-cabecalho
        font-size 14.4px
        color #838383
        max-height 65px
        min-width 170px
        max-width 170px

    .text-header
        font-size 14.4px
        color #777

    .max-30
        max-width 30vw

    .cabecalho-modal3-desktop
        padding-top 1px
        margin-bottom 8px
        margin-left 18px

    .cabecalho-modal3-mobile
        display none

    @media (max-width: 720px)
        .max-30
            max-width 80vw !important

        .cabecalho-modal3-desktop
            display none

        .cabecalho-modal3-mobile
            display block
</style>
