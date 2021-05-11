import getters from './getters'
import rotulosPersonalizadosDefault from './rotulosPersonalizadosDefault'

describe('getters', () => {
    let windowSpy

    beforeEach(() => {
        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            $i18n: {
                locale: 'rotulosPersonalizados',
                getLocaleMessage: () => rotulosPersonalizadosDefault.i18n
            }
        }))
    })

    const tipoCampo = 'nome', nomeCampo = 'Nome Intangivel', tipoCampo2 = 'orgao', tipoTela = 'CONVENIO'
    const state = (tipo = tipoCampo, nome = nomeCampo, obrigatorio = true) => ({
        rotulosPersonalizados: rotulosPersonalizadosDefault.i18n
    })

    const convenio = (nome = 'Patrimonio Intangível') => ({
        nome,
        orgao: 'Prefeitura',
        setor: 'secretaria'
    })

    it('deve retornar os rotulosPersonalizados do state', () => {
        const rotulosPersonzalidos = getters.getRotulosPersonalizados()
        expect(rotulosPersonzalidos).toEqual(rotulosPersonalizadosDefault.i18n)
    })

    it('deve retornar o campo pelo nome do tipo', () => {
        const telaNome = 'CONVENIO'
        const campoLabel = getters.getNomeRotulosPersonalizados()('numero', telaNome)
        expect(campoLabel).toEqual(rotulosPersonalizadosDefault.i18n.CONVENIO.campos.numero.nome)
    })

    it('deve retornar o campo vazio quando não for encontrado', () => {
        const tipo = 'CONVENIO'
        const campoLabel = getters.getNomeRotulosPersonalizados()('Qualquer', tipo)
        expect(campoLabel).toBeUndefined()
    })

    it('deve retornar true para obrigatoriedade String true', () => {
        const campoNome = 'stringTrue'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(true)
    })

    it('deve retornar false para obrigatoriedade String false', () => {
        const campoNome = 'stringFalse'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(false)
    })

    it('deve retornar true para obrigatoriedade undefined (obrigatorio por padrão)', () => {
        const campoNome = 'indefinido'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(true)
    })

    it('deve retornar true para obrigatoriedade boolean true', () => {
        const campoNome = 'boleanoTrue'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(true)
    })

    it('deve retornar false para obrigatoriedade boolean false', () => {
        const campoNome = 'boleanoFalse'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(false)
    })

    it('deve retornar true para obrigatoriedade nula (obrigatório por padrão)', () => {
        const campoNome = 'nulo'
        const telaNome = 'TESTE_OBRIGATORIEDADE'
        const campoRequired = getters.getObrigatorioRotulosPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(true)
    })

    it('deve retornar o tooltip do campo', () => {
        const telaNome = 'INCORPORACAO_DADOS_GERAIS'
        const campoLabel = getters.getTooltipRotulosPersonalizados()('reconhecimento', telaNome)
        expect(campoLabel).toEqual(rotulosPersonalizadosDefault.i18n.INCORPORACAO_DADOS_GERAIS.campos.reconhecimento.tooltip)
    })

    it('deve retornar o valido, quando todos os campos forem válidos', () => {
        const valido = getters.getObjetoValidado()(rotulosPersonalizadosDefault.i18n.CONVENIO.campos, 'CONVENIO')
        expect(valido).toBeTruthy()
    })

    it('deve retornar não valido quando obrigatorio por padrao e não preenchido', () => {
        const concedente = {cpfCnpj: null, nome: 'olá', situacao: 'não ta boa'}
        const valido = getters.getObjetoValidado()(concedente, 'CONCEDENTE')
        expect(valido).toBeFalsy()
    })

    it('deve retornar não valido quando obrigatorio for configurável true e não preenchido', () => {
        const convenio = {numero: '123',
        nome: 'ola',
        concedente: 'ola',
        fonteRecurso: null,
        dataVigenciaInicio: '10/02/2020',
        dataVigenciaFim: '10/03/2020',
        situacao: 'mais ou menos'}
        const valido = getters.getObjetoValidado()(convenio, 'CONVENIO')
        expect(valido).toBeFalsy()
    })

    it('deve retornar o não valido, quando algum dos campos forem inválidos', () => {
        const stateAtual = state(tipoCampo, nomeCampo, true)
        const valido = getters.getObjetoValidado(stateAtual)(tipoTela, convenio(''))
        expect(valido).toBeFalsy()
    })

    describe('ItemIncorporacao', () => {
        it('deve validar se todos os campos obrigatórios dos itens da incorporacao foram preenchidos, para o tipo veiculo', () => {
            const item = {id:1,marca:'fiat',modelo:'automovel',fabricante:'volks',quantidade:10,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:'2019/2020',combustivel:'DIESEL',categoria:'AERONAVES',descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,true,'ITEM_INCORPORACAO')
            expect(valido).toEqual(true)
        })

        it('deve falhar se campos obrigatórios padrão dos itens da incorporacao não foram preenchidos, para o tipo veiculo', () => {
            const item = {id:1,marca:'fiat',modelo:'automovel',fabricante:'volks',quantidade:null,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:'2019/2020',combustivel:'DIESEL',categoria:'AERONAVES',descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,true,'ITEM_INCORPORACAO')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campos obrigatórios personalizados obrigatorio=true dos itens da incorporacao não foram preenchidos, para o tipo veiculo', () => {
            const item = {id:1,marca:null,modelo:'automovel',fabricante:'volks',quantidade:10,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:'2019/2020',combustivel:'DIESEL',categoria:'AERONAVES',descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,true,'ITEM_INCORPORACAO')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campos obrigatórios personalizados obrigatorio=true dos itens da incorporacao não foram preenchidos e faltar um tipo veiculo, para o tipo veiculo', () => {
            const item = {id:1,marca:'fiat',modelo:'automovel',fabricante:'volks',quantidade:10,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:'2019/2020',combustivel:null,categoria:'AERONAVES',descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,true,'ITEM_INCORPORACAO')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios dos itens da incorporacao foram preenchidos, para o tipo diferente de veiculo', () => {
            const item = {id:1,marca:'fiat',modelo:'automovel',fabricante:'volks',quantidade:10,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:null,combustivel:null,categoria:null,descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,false,'ITEM_INCORPORACAO')
            expect(valido).toEqual(true)
        })

        it('deve falhar se campos obrigatórios padrão dos itens da incorporacao não foram preenchidos, para o tipo diferente de veiculo', () => {
            const item = {id:1,marca:'fiat',modelo:'automovel',fabricante:'volks',quantidade:null,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:null,combustivel:null,categoria:null,descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,false,'ITEM_INCORPORACAO')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campos obrigatórios personalizados obrigatorio=true dos itens da incorporacao não foram preenchidos, para o tipo diferente de veiculo', () => {
            const item = {id:1,marca:null,modelo:'automovel',fabricante:'volks',quantidade:10,valorTotal:50.00,numeracaoPatrimonial:'AUTOMATICA',tipoBem:'VEICULO',uriImagem:'uri',anoFabricacaoModelo:null,combustivel:null,categoria:null,descricao:'Caminhonete pick up',codigo:'0000019',imagem:null,situacao:'EM_ELABORACAO',contaContabil:41,naturezaDespesa:1,estadoConservacao:1}
            const valido = getters.getItemIncorporacaoValidado()(item,false,'ITEM_INCORPORACAO')
            expect(valido).toEqual(false)
        })
    })

    describe('Incorporacao', () => {
        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com convenio checkbox e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'1234', fundo:'123',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se convenio checkbox obrigatoria mas convenio não preenchido', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null, fundo:'123',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:false,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com fundo checkbox e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'1234',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null,fundo:'123',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:null,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:false,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se fundo checkbox obrigatoria mas fundo não preenchido', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'1234',fundo:null,reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,origemConvenio:false,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com convenio e fundo checkbox e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'1234',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:null,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se projeto checkbox obrigatoria mas projeto não preenchido', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'1234',fundo:'123',projeto:null,comodante:'123',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com convenio, fundo e projeto checkbox e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'1234',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:'Projeto Teste',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:null,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se comodato checkbox obrigatoria mas comodante não preenchido', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'1234',fundo:'123',comodante:null,reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,origemConvenio:false,origemFundo:true,origemProjeto:false,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com convenio, fundo, projeto e comodato checkbox e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'1234',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:'Projeto Teste',comodante:'Geraldino',reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:null,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:true, origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se fundo e convenio checkbox obrigatoria mas fundo e convenio não preenchido e nota obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:'123',valorNota:1.110000,dataNota:'2020-09-01T00:00:00.000-0400',fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null, fundo:null,reconhecimento:{id:1,notaFiscal:true,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com convenio checkbox e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'1234', fundo:null,reconhecimento:{id:1,notaFiscal:false,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:false,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se convenio checkbox obrigatoria mas convenio não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null, fundo:'123',reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:false,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se todos os campos obrigatórios da incorporacao foram preenchidos, com fundo checkbox e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null, fundo:'123',reconhecimento:{id:1,notaFiscal:false,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:false,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se fundo checkbox obrigatoria mas fundo não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:null,reconhecimento:{id:1,notaFiscal:false,empenho:null},naturezaDespesa:1,origemConvenio:false,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve validar se convenio e fundo checkbox obrigatoria e convenio e fundo preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve validar se convenio, fundo e projeto checkbox obrigatoria e convenio, fundo e projeto preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:'123',reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve validar se convenio, fundo, projeto e comodato checkbox obrigatoria e convenio, fundo, projeto e comodante preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:'123',comodante:'123',reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,numeroNotaLancamentoContabil:'2020NL12345',dataNotaLancamentoContabil:'2020-09-09T00:00:00.000-0400',origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se convenio e fundo checkbox obrigatoria mas convenio e fundo não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null, fundo:null,reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:false,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se convenio, fundo e projeto checkbox obrigatoria mas convenio, fundo e projeto não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null,fundo:null,projeto:null,reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:false}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se convenio, fundo, projeto e comodato checkbox obrigatoria mas convenio, fundo, projeto e comodante não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:null,fundo:null,projeto:null,comodante:null,reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se projeto checkbox obrigatorio mas não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:null,comodante:'123',reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se comodato checkbox obrigatorio mas comodante não preenchido e nota Não obrigatoria', () => {
            const item = {id:3,codigo:4,quantidade:null,dataRecebimento:'2020-09-09T00:00:00.000-0400',situacao:'EM_ELABORACAO',numProcesso:'12345',nota:null,valorNota:null,dataNota:null,fornecedor:'Google Brasil Internet Ltda',orgao:132,setor:5127,convenio:'123', fundo:'123',projeto:'123',comodante:null,reconhecimento:{id:1,notaFiscal:null,empenho:null},naturezaDespesa:1,origemConvenio:true,origemFundo:true,origemProjeto:true,origemComodato:true}
            const valido = getters.getIncorporacaoValidado()(item,item.origemConvenio,item.origemFundo,item.origemProjeto,item.origemComodato,item.reconhecimento.notaFiscal,'INCORPORACAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })
    })

    describe('Documento', () => {
        it('deve validar se todos campos obrigatorios do documento preenchidos', () => {
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se campo numero do documento não preenchido', () => {
            const documento = {numero: null, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campo valor do documento não preenchido', () => {
            const documento = {numero: 123, valor: null, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campo tipo do documento não preenchido', () => {
            const documento = {numero: 123, valor: 100, tipo: null, data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campo data do documento não preenchido', () => {
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: null, uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campo uriAnexo do documento possuir obrigatoriedade configurável true e não for preenchido', () => {
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: null}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve validar se campo uriAnexo do documento possuir obrigatoriedade configurável false e não for preenchido', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.uriAnexo.obrigatorio = false
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: null}
            const valido = getters.getDocumentoTodosObrigatoriosValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve validar se todos campos obrigatorios do documento foram preenchidos', () => {
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se campo numero do documento não for preenchido', () => {
            const documento = {numero: null, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campo tipo do documento não for preenchido', () => {
            const documento = {numero: 123, valor: 100, tipo: null, data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve validar se campo valor possuir obrigatoriedade configurável false e não for preenchido', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.valor.obrigatorio = false
            const documento = {numero: 123, valor: null, tipo: 'NOTA', data: '10/02/2021', uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve validar se campo data possuir obrigatoriedade configurável false e não for preenchido', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.data.obrigatorio = false
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: null, uriAnexo: 'Anexo'}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve validar se campo uriAnexo possuir obrigatoriedade configurável false e não for preenchido', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.uriAnexo.obrigatorio = false
            const documento = {numero: 123, valor: 100, tipo: 'NOTA', data: '10/02/2021', uriAnexo: null}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve validar se campos com obrigatoriedade configurável false e não preenchidos e campos obrigatorios por padrão preenchidos', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.valor.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.data.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.uriAnexo.obrigatorio = false
            const documento = {numero: 123, valor: null, tipo: 'NOTA', data: null, uriAnexo: null}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(true)
        })

        it('deve falhar se campos com obrigatoriedade configurável false e não preenchidos e campo numero obrigatorio por padrão não preenchidos', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.valor.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.data.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.uriAnexo.obrigatorio = false
            const documento = {numero: null, valor: null, tipo: 'NOTA', data: null, uriAnexo: null}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })

        it('deve falhar se campos com obrigatoriedade configurável false e não preenchidos e campo tipo obrigatorio por padrão não preenchidos', () => {
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.valor.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.data.obrigatorio = false
            rotulosPersonalizadosDefault.i18n.DOCUMENTOS.campos.uriAnexo.obrigatorio = false
            const documento = {numero: 123, valor: null, tipo: null, data: null, uriAnexo: null}
            const valido = getters.getDocumentoNumeroTipoObrigatorioValidado()(documento,'DOCUMENTOS')
            expect(valido).toEqual(false)
        })
    })

    describe('Distribuicao', () => {
        it('deve validar se todos os campos obrigatórios da distribuição foram preenchidos', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(true)
        })

        it('deve falhar validação da distribuição se setorOrigem da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: null, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se setorDestino da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: null, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se motivoObservacao da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: null, numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se numeroProcesso da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: null, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se orgao da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: null, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se numeroNotaLancamentoContabil da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: null, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se dataNotaLancamentoContabil da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: null, numeroProcesso: 12344, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se numeroProcesso e orgao da movimentação não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: null, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: null, responsavel: 3}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })

        it('deve falhar validação da distribuição se responsável não foi preenchido', () => {
            const distibuicao = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, motivoObservacao: 'motivo', numeroNotaLancamentoContabil: 123, dataNotaLancamentoContabil: '2020-09-09T00:00:00.000-0400', numeroProcesso: 12344, responsavel: null}
            const valido = getters.getDistribuicaoValidado()(distibuicao,'MOVIMENTACAO_INTERNA_DADOS_GERAIS','INCORPORACAO_DADOS_GERAIS', 'DISTRIBUICAO_DADOS_GERAIS')
            expect(valido).toEqual(false)
        })
    })

    describe('ENTRE_SETORES, ENTRE_ESTOQUES, DEVOLUCAO_ALMOXARIFADO', () => {
        it('deve validar se campos obrigatórios preenchidos(entre_setores, entre_estoques, devolucao_almoxarifado)', () => {
            const movimentacaoInterna = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4}

            const valido = getters.getMovimentacaoInternaValidado()(movimentacaoInterna)
            expect(valido).toBe(true)
        })

        it('deve falhar quando campos obrigatórios não preenchidos(entre_setores, entre_estoques, devolucao_almoxarifado)', () => {
            const movimentacaoInterna = {id: 1, orgao: 2, setorDestino: 4}

            const valido = getters.getMovimentacaoInternaValidado()(movimentacaoInterna)
            expect(valido).toBe(false)
        })
    })

    describe('TEMPORARIA', () => {
        it('deve validar se campos obrigatórios preenchidos(temporária)', () => {
            const temporaria = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4, dataDevolucao: '2021-02-10T00:00:00.000-0400'}

            const valido = getters.getTemporariaValidado()(temporaria)
            expect(valido).toBe(true)
        })

        it('deve falhar quando campos obrigatórios não preenchidos(temporária)', () => {
            const temporaria = {id: 1, orgao: 2, setorOrigem: 3, setorDestino: 4}

            const valido = getters.getTemporariaValidado()(temporaria)
            expect(valido).toBe(false)
        })
    })
})
