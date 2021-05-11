<template>
    <v-btn
        outlined
        depressed
        :class="{'btn-disabled': desabilitado}"
        :disabled="desabilitado"
        class="btn-adicionar-modal3"
        @click="adicionarOutro">
        Adicionar Outro
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoAdicionarOutro',
        props:{
            desabilitado:{
                type: Boolean,
                default: false
            }
        },
        methods:{
            adicionarOutro(){
                this.$emit('adicionarOutro')
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .btn-adicionar-modal3
        color #999 !important
</style>
