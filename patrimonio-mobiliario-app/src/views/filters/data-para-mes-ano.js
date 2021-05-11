export default (dataInicial) => {
  if (dataInicial) {
    const data = dataInicial.split('-')
    const ano = data[0]
    const mes = data[1]
    return `${mes}/${ano}`
  }
  return '-'
}