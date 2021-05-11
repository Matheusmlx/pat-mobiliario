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
                    <span :class="{'text-capitalize':textCapitalize}">Continuar</span>
                    <v-icon v-if="!esconderIcone" size="15" class="ml-1">fas fa-arrow-right</v-icon>
                </v-btn>
            </div>
        </template>
        É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoContinuarDesabilitado',
        props:{
            esconderIcone:{
                type: Boolean,
                default: false
            },
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
