export default (valor) => {
  if (valor) {
    return valor.toLocaleString('pt-br', { minimumFractionDigits: 2 }) + '%'
  }
  return '-'
}
