<template>
    <v-row
        align="center"
        justify="center"
        class="movimentacao-interna-itens">
        <div class="text-center">
            <p class="text-info">Nenhum <span class="font-weight-bold">patrimônio</span> adicionado, clique para adicionar</p>

            <botao-adicionar-patrimonio @adicionarPatrimonio="adicionarItem" :desabilitar="!verificaPermissaoEdicao" />

        </div>
    </v-row>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import BotaoAdicionarPatrimonio from '@/views/components/botoes/BotaoAdicionarPatrimonio'

    export default {
        name: 'MovimentacaoInternaListagemPatrimoniosVazia',
        components: {BotaoAdicionarPatrimonio},
        computed:{
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
        },
        methods:{
            adicionarItem(){
                this.$emit('adicionarItem')
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .movimentacao-interna-itens
        min-height 62vh

    @media screen and (max-device-height: 786px)
        .movimentacao-interna-itens
            min-height 58vh

    .text-info
        color #777 !important
        font-size 14px
</style>
