<template>
    <v-btn :disabled="desabilitar" @click="adicionarPatrimonio" color="primary" depressed>
        <v-icon size="15" class="ml-1 mr-1">add_circle</v-icon>
        <span class="text-capitalize"> Adicionar Patrimônio</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoAdicionarPatrimonio',
        props:{
            desabilitar:{
                type: Boolean,
                default: false
            }
        },
        methods:{
            adicionarPatrimonio(){
                this.$emit('adicionarPatrimonio')
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

        i
            margin-right 0
</style>
