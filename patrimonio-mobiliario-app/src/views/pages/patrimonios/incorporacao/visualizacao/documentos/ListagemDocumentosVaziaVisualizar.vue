<template>
    <v-row
        align="center"
        justify="center"
        class="incorporacao-sem-documentos">
        <div class="text-center">
            <p class="text-info">Nenhum documento adicionado, clique para adicionar</p>
            <v-btn disabled color="primary" class="btn-disabled" depressed>Novo</v-btn>
        </div>
    </v-row>
</template>

<script>
    export default {
        name: 'ListagemDocumentosVaziaVisualizar',
    }
</script>

<style lang="stylus" scoped>
    .incorporacao-sem-documentos
        min-height 62vh

    .text-info
        color #777 !important
        font-size 14px
</style>
