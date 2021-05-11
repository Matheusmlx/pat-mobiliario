<template>
    <v-btn
        @click="voltar"
        color="grey"
        text
        outlined>
        <v-icon v-if="!esconderIcone" size="15" class="mr-1">fas fa-arrow-left</v-icon>
        <span :class="{'text-capitalize':textCapitalize}">Voltar</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoVoltar',
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
        methods:{
            voltar(){
                this.$emit('voltar')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .botao-default
        border 0
        color #777 !important

        .v-icon
            color #777 !important
</style>
