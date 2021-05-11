const responsavel = {
    nome: 'Responsável',
    obrigatorio: true
}

export default {
    i18n: {
        CONTA_CONTABIL: {
            campos: {
                codigoContabil: {
                    nome: 'Código Contábil'
                },
                contaContabil: {
                    nome: 'Conta Contábil'
                },
                tipoBem: {
                    nome: 'Tipo do Bem'
                },
                tipoConta: {
                    nome: 'Tipo da Conta'
                },
                vidaUtil: {
                    nome: 'Vida Útil'
                },
                residual: {
                    nome: 'Residual'
                }
            }
        },
        CONVENIO: {
            campos: {
                numero: {
                    nome: 'Número'
                },
                nome: {
                    nome: 'Nome'
                },
                concedente: {
                    nome: 'Concedente'
                },
                fonteRecurso: {
                    nome: 'Fonte de Recurso',
                    obrigatorio: true
                },
                dataVigenciaInicio: {
                    nome: 'Início Vigência'
                },
                dataVigenciaFim: {
                    nome: 'Fim Vigência'
                },
                situacao: {
                    nome: 'Situação'
                }
            }
        },
        CONCEDENTE: {
            campos: {
                cpfCnpj: {
                    nome: 'CPF/CNPJ'
                },
                nome: {
                    nome: 'Concedente'
                },
                situacao: {
                    nome: 'Situação'
                }
            }
        },
        RECONHECIMENTO: {
            campos: {
                nome: {
                    nome: 'Nome'
                },
                situacao: {
                    nome: 'Situação'
                },
                execucaoOrcamentaria: {
                    nome: 'Execução Orçamentária'
                },
                camposObrigatorios: {
                    nome: 'Campos obrigatórios'
                }
            }
        },
        INCORPORACAO_DADOS_GERAIS: {
            campos: {
                reconhecimento: {
                    nome: 'Reconhecimento',
                    tooltip: 'De acordo com o MCASP, o reconhecimento define como o bem foi adquirido. Esse campo é equivalente a modalidade de aquisição ou origem do bem.'
                },
                numProcesso: {
                    nome: 'Nº Processo',
                    obrigatorio: true
                },
                fornecedor: {
                    nome: 'Fornecedor',
                    obrigatorio: true
                },
                numeroEmpenho: {
                    nome: 'N° Empenho'
                },
                dataEmpenho: {
                    nome: 'Data Empenho'
                },
                valorEmpenho: {
                    nome: 'Valor Empenho'
                },
                dataRecebimento: {
                    nome: 'Recebimento'
                },
                nota: {
                    nome: 'Nº Nota'
                },
                dataNota: {
                    nome: 'Data Nota'
                },
                valorNota: {
                    nome: 'Valor Nota'
                },
                orgao: {
                    nome: 'Órgão Responsável'
                },
                setor: {
                    nome: 'Setor',
                    tooltip: 'Este campo contém o equivalente a Unidades Organizacionais e Setores. O campo será ativado somente quando o usuário escolher um órgão.'
                },
                origem: {
                    nome: 'Origem'
                },
                convenio: {
                    nome: 'Convênio',
                    obrigatorio: true
                },
                fundo: {
                    nome: 'Fundo Especial',
                    obrigatorio: true
                },
                projeto: {
                    nome: 'Projeto',
                    obrigatorio: true
                },
                comodante: {
                    nome: 'Comodante',
                    obrigatorio: true
                },
                numeroNotaLancamentoContabil: {
                    nome: 'Número da NL',
                    obrigatorio: true
                },
                dataNotaLancamentoContabil: {
                    nome: 'Data da NL',
                    obrigatorio: true
                },
                dataFinalizacao: {
                    nome: 'Finalizado em'
                }
            }
        },
        INCORPORACAO_ITEM_LISTAGEM_PATRMONIO: {
            campos: {
                numero: {
                    nome: 'Patrimônio'
                },
                valorAquisicao: {
                    nome: 'Valor Un.(R$)'
                },
                placa: {
                    nome: 'Placa',
                    obrigatorio: true
                },
                renavam: {
                    nome: 'Renavam',
                    obrigatorio: true
                },
                licenciamento: {
                    nome: 'Licenciamento',
                    obrigatorio: true
                },
                motor: {
                    nome: 'Motor',
                    obrigatorio: true
                },
                chassi: {
                    nome: 'Chassi',
                    obrigatorio: true
                },
                numeroSerie: {
                    nome: 'N° Série',
                    obrigatorio: true
                }
            }
        },
        ITEM_INCORPORACAO: {
            campos: {
                marca: {
                    nome: 'Marca',
                    obrigatorio: true
                },
                modelo: {
                    nome: 'Modelo',
                    obrigatorio: true
                },
                fabricante: {
                    nome: 'Fabricante',
                    obrigatorio: true
                },
                quantidade: {
                    nome: 'Quantidade'
                },
                codigo: {
                    nome: 'Codigo'
                },
                descricao: {
                    nome: 'Descricao'
                },
                valorTotal:{
                    nome: 'Valor Total'
                },
                numeracaoPatrimonial: {
                    nome: 'Numeração Patrimonial',
                    tooltip: 'Escolha o tipo de numeração que você vai utilizar, e os números serão gerados no próximo passo.'
                },
                naturezaDespesa: {
                    nome: 'Natureza de Despesa'
                },
                contaContabil: {
                    nome: 'Conta Contábil Classificação',
                    tooltip: 'Campo para selecionar a conta contábil de classificação onde o patrimônio estará.' +
                        ' Porém, enquanto o bem não for distribuído, ele ficará na conta contábil de Estoque Interno.'
                },
                estadoConservacao: {
                    nome: 'Estado de Conservação'
                },
                tipoBem: {
                    nome: 'Tipo'
                },
                uriImagem: {
                    nome: 'Imagem',
                    obrigatorio: true
                },
                'anoFabricacaoModelo': {
                    nome: 'Ano Fabricação/Modelo',
                    obrigatorio: true
                },
                combustivel: {
                    nome: 'Combustível',
                    obrigatorio: true
                },
                categoria: {
                    nome: 'Categoria',
                    obrigatorio: true
                }
            }
        },
        DOCUMENTOS: {
            campos: {
                numero: {
                    nome: 'Número'
                },
                valor: {
                    nome: 'Valor',
                    obrigatorio: true
                },
                tipo: {
                    nome: 'Tipo'
                },
                data: {
                    nome: 'Data',
                    obrigatorio: true
                },
                uriAnexo: {
                    nome: 'Anexo',
                    obrigatorio: true
                }
            },
        },
        FICHA_INDIVIDUAL_PATRIMONIO: {
            campos: {
                contaContabilAtual: {
                    nome: 'Conta Contábil Atual',
                    tooltip: 'asdf'
                }
            }
        },
        INCORPORACAO_ESTORNO: {
            campos: {
                motivo: {
                    nome: 'Motivo'
                },
                dataHora: {
                    nome: 'Data/Hora'
                }
            }
        },
        MOVIMENTACAO_INTERNA_DADOS_GERAIS:  {
            campos : {
                setorOrigem: {
                    nome: 'Setor Origem'
                },
                setorDestino: {
                    nome: 'Setor Destino'
                },
                motivoObservacao: {
                    nome: 'Motivo/Obs.',
                    obrigatorio: true
                },
                numeroProcesso: {
                    nome: 'N° Processo',
                    obrigatorio: true
                }
            }
        },
        DISTRIBUICAO_DADOS_GERAIS: {
            campos: {
                responsavel,
            }
        },
        DEFINITIVA_DADOS_GERAIS: {
            campos: {
                responsavel
            }
        },
        TEMPORARIA_DADOS_GERAIS: {
            campos: {
                responsavel
            }
        },
        ENTRE_ESTOQUES_DADOS_GERAIS: {
            campos: {
                responsavel
            }
        },
        DEVOLUCAO_ALMOXARIFADO_DADOS_GERAIS: {
            campos: {
                responsavel
            }
        },
        TESTE_OBRIGATORIEDADE: {
            campos: {
                stringTrue: {
                    nome: 'StringTrue',
                    obrigatorio: 'true'
                },
                stringFalse: {
                    nome: 'StringFalse',
                    obrigatorio: 'false'
                },
                indefinido: {
                    nome: 'Indefinido'
                },
                boleanoTrue: {
                    nome: 'BoleanoTrue',
                    obrigatorio: true
                },
                boleanoFalse: {
                    nome: 'BoleanoFalse',
                    obrigatorio: false
                },
                nulo: {
                    nome: 'Nulo',
                    obrigatorio: null
                },
            }
        }
    }
}
