<template>
    <az-container>
        <v-col cols="12">
            <span class="session-title">Dados de Depreciação</span>
        </v-col>
        <v-col cols="12">
            <v-simple-table class="simple-table">
                <thead>
                <tr>
                    <th class="font-weight-bold" scope="col">Método</th>
                    <th class="font-weight-bold" scope="col">Vida Útil (meses)</th>
                    <th class="font-weight-bold" scope="col">Valor Depreciação Mensal</th>
                    <th class="font-weight-bold" scope="col">Valor Depreciado Acumulado</th>
                    <th class="font-weight-bold" scope="col">Valor Líquido / Ajustado</th>
                    <th class="font-weight-bold" scope="col">Valor Residual</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="patrimonio.dadosDepreciacao">
                    <td class="grey--text">{{patrimonio.dadosDepreciacao.metodo | formatacaoCampoMetodoDepreciacao}}</td>
                    <td class="grey--text">{{patrimonio.dadosDepreciacao.vidaUtil | formatarCampo}}</td>
                    <td class="grey--text">{{patrimonio.dadosDepreciacao.valorDepreciacaoMensal | valorParaDinheiro}}</td>
                    <td class="grey--text">{{patrimonio.dadosDepreciacao.valorDepreciacaoAcumulado | valorParaDinheiro}}</td>
                    <td class="grey--text">{{patrimonio.valorLiquido | valorParaDinheiro}}</td>
                    <td class="grey--text">{{ patrimonio.valorResidual | valorParaDinheiro }}</td>
                </tr>
                </tbody>
            </v-simple-table>
        </v-col>
      <v-col class="d-flex justify-end pt-0" cols="12">
        <a @click="abrirModal" class="text--secondary body-2 ver-mais">Ver Mais</a>
      </v-col>
    </az-container>
</template>

<script>
    export default {
        name: 'FichaPatrimonioDadosGeraisDepreciacao',
        props: {
            patrimonio: {
                required: true
            }
        },
        data() {
            return {
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        methods:{
            abrirModal() {
                this.$router.push({name: 'ModalTabelaDepreciacoes',params:{patrimonioId: this.$route.params.patrimonioId}})
            }
        },
        filters:{
            formatacaoCampoMetodoDepreciacao(metodo){
                switch (metodo) {
                case 'QUOTAS_CONSTANTES' :
                    return 'Quotas Constantes'
                default:
                    return '-'
                }
            },
            formatarCampo(value){
                if(!value){
                    return '-'
                }else{
                    return value
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
.titulo
    color: #777;

.ver-mais
  text-decoration: underline grey
</style>
