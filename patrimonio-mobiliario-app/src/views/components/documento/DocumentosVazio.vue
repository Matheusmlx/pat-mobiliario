<template>
    <v-row
        align="center"
        justify="center"
        class="incorporacao-sem-documentos">
        <div class="text-center">
            <p class="text-info">Nenhum documento adicionado, clique para adicionar</p>
            <v-btn :disabled="!verificaPermissaoEdicao" @click="novoDocumento" color="primary" depressed>Novo</v-btn>
        </div>
    </v-row>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'ListagemDocumentosVazia',
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
        },
        methods: {
            novoDocumento() {
                this.$emit('novoDocumento')
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .incorporacao-sem-documentos
        min-height 62vh

    .text-info
        color #777 !important
        font-size 14px
</style>
