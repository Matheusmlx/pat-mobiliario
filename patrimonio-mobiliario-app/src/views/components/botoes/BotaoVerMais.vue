<template>
    <v-btn
        @click="verMais"
        color="primary"
        depressed>
        <span class="text-capitalize">Ver Mais</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoVerMais',
        methods:{
            verMais(){
                this.$emit('verMais')
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
