<template>
    <v-card>
        <v-card-text>
            <p class="text-md-center">Redirecionando para a edição do seus dados cadastrais.</p>
        </v-card-text>
    </v-card>
</template>

<script>
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'RedirecionaEditarUsuario',
        async created() {
            const data = await this.$store.dispatch(actionTypes.COMUM.BUSCAR_LINK_EDITAR_USUARIO)
            window.location.assign(this.tratarLinkSemDominio(data))
        },
        methods: {
            tratarLinkSemDominio(link) {
                return link.replace('null', '')
            }
        }
    }
</script>
