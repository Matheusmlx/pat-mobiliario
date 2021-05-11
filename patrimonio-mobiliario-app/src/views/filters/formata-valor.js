import azNumber from '@azinformatica/loki/src/filters/az-number'

export default (valor) => {
    if (!valor) {
        return ' - '
    }

   return azNumber(valor)
}

