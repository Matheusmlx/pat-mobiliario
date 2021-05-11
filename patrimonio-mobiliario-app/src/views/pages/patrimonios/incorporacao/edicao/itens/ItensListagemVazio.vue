<template>
    <v-container fluid white>
        <v-row>
            <v-col cols="12">
                <v-row align="center" justify="center" class="incorporacao-item-sem-itens">
                    <div class="text-center">
                        <p class="text-info">Nenhum <strong>item</strong> adicionado, clique para adicionar</p>
                        <az-call-to-action
                            active
                            :disabled="!verificaPermissaoEdicao"
                            slot="actions"
                            @click="adicionarItem">
                            <v-icon>add_circle</v-icon>
                            Adicionar Item
                        </az-call-to-action>
                    </div>
                </v-row>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    export default {
        name: 'ItensListagemVazio',
        computed:{
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
        },
        methods:{
            adicionarItem(){
                this.$emit('adicionarItem')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .incorporacao-item-sem-itens
        min-height 59vh

    @media screen and (max-device-height: 786px)
        .incorporacao-item-sem-itens
            min-height 55vh

</style>
