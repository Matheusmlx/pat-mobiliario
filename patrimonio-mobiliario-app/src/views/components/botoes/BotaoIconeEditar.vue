<template>
    <v-btn class="botao-editar" icon @click="ativarEdicao">
        <v-icon size="25" class="mr-14">fa edit</v-icon>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoIconeEditar',
        methods: {
            ativarEdicao() {
                this.$emit('ativarEdicao')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .botao-editar
        float right !important
        width 38px !important
</style>
