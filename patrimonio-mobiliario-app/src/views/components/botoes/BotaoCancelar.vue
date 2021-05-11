<template>
    <v-btn
        id="botaoCancelar"
        outlined
        depressed
        class="botao-cancelar"
        @click="cancelar">
        <span :class="{'text-capitalize':textCapitalize}">Cancelar</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoCancelar',
        props:{
            textCapitalize:{
                type: Boolean,
                default: false
            }
        },
        methods:{
            cancelar(){
                this.$emit('cancelar')
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .botao-cancelar
        color #999 !important
</style>
