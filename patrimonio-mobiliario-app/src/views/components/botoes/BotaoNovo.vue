<template>
        <az-call-to-action
            class="botao-novo"
            hideBorder
            v-bind="$attrs"
            v-on="$listeners"
        >
            <v-icon>add_circle</v-icon>
            <span class="text">Novo</span>
        </az-call-to-action>
</template>

<script>
    export default {
        name: 'BotaoNovo'
    }
</script>

<style lang="stylus">
.botao-novo
    .text
        padding-top 2px !important
        font-size 16px !important
    i
        font-size 24px
</style>
