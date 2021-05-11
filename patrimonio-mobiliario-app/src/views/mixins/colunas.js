export default {
    methods: {
        criarColunas(quantidadeColunas, textArray, valueArray, sortableArray, alignArray, widthArray, classe) {
            let colunas = []
            for (let i = 0; i < quantidadeColunas; i++) {
                let coluna = {
                    text: textArray.length > 0 ? textArray[i] : null,
                    value: valueArray.length > 0 ? valueArray[i] : null,
                    sortable: sortableArray.length > 0 ? sortableArray[i] : null,
                    align: alignArray.length > 0 ? alignArray[i] : null,
                    width: widthArray.length > 0 ? widthArray[i] : null,
                    class: classe
                }
                colunas.push(coluna)
            }
            return colunas
        }
    }
}
