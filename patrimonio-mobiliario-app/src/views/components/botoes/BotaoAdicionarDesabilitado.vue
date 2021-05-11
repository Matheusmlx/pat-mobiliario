<template>
    <v-tooltip
        top
        nudge-top="30"
        max-width="200">
        <template v-slot:activator="{ on }">
            <div v-on="on" class="btn-disabled">
                <v-btn
                    disabled
                    color="primary"
                    depressed>
                    <span :class="{'text-capitalize':textCapitalize}">Adicionar</span>
                </v-btn>
            </div>
        </template>
        É necessário selecionar pelo menos um item.
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoAdicionarDesabilitado',
        props:{
            textCapitalize:{
                type: Boolean,
                default: false
            }
        },
    }
</script>

<style scoped lang="stylus">
    .botao-default
        border 0
        color #777 !important

        .v-icon
            color #777 !important

        i
            margin-right 0
</style>
