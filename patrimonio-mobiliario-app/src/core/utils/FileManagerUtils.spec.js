import fileManagerUtils from './FileManagerUtils'

describe('FileManagerUtils', () => {

    it('Deve retornar a url para uma imagem thumbnail', () => {
        const uri = 'repo1:/arquivos/imagem1'
        expect(fileManagerUtils.createThumbnailUrl('')).toBe('')
        expect(fileManagerUtils.createThumbnailUrl(uri)).toBe(`/hal/public/arquivos?uri=${uri}&thumbnail=true`)
    })

    it('Deve retornar a url para um arquivo', () => {
        const uri = 'repo1:/arquivos/arquivo1'
        expect(fileManagerUtils.createUrl('')).toBe('')
        expect(fileManagerUtils.createUrl(uri)).toBe(`/hal/public/arquivos?uri=${uri}&thumbnail=false`)
    })

})