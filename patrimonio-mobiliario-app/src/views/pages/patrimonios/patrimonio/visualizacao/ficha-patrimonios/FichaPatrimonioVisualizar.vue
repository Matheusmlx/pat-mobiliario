<template>
    <div>
        <az-back-button :route="{ name: 'PatrimoniosListagem' }" text="Voltar para listagem"/>
        <az-container>
            <v-tabs>
                <v-tab :to="{name:'FichaPatrimonioDadosGeraisVisualizacao'}">DADOS GERAIS</v-tab>
                <v-tab :to="{name:'FichaPatrimonioMovimentacoesVisualizacao'}">MOVIMENTAÇÕES</v-tab>
            </v-tabs>
            <router-view/>
        </az-container>
    </div>
</template>

<script>
    export default {
        name: 'FichaPatrimonioVisualizar',
    }
</script>
