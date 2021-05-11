export default {
    methods: {
        verificarMudancasPropriedades(objetoNovo, objetoAntigo, objetoAtual) {
            for (let chave of Object.keys(objetoNovo)) {
                if (typeof objetoNovo[chave] === 'object' && objetoNovo[chave] !== null) {
                    if (typeof objetoAntigo[chave] === 'object' && objetoAntigo[chave] !== null && objetoAntigo[chave].id !== objetoNovo[chave].id) {
                        this.atualizarDados(chave, objetoNovo[chave], objetoAntigo, objetoAtual)
                    }
                    if (typeof objetoAntigo[chave] !== 'object' && objetoAntigo[chave] !== objetoNovo[chave].id) {
                        this.atualizarDados(chave, objetoNovo[chave], objetoAntigo, objetoAtual)
                    }
                }
                else if (objetoAntigo[chave] === undefined || objetoAntigo[chave] !== objetoNovo[chave]) {
                    this.atualizarDados(chave, objetoNovo[chave], objetoAntigo, objetoAtual)
                }
            }
        },
        atualizarDados(chave, valor, objetoAntigo, objetoAtual) {
            this.$set(objetoAtual, chave, valor)
            objetoAntigo[chave] = valor
        }
    }
}
