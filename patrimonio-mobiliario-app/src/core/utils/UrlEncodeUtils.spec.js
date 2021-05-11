import UrlEncodeUtils from './UrlEncodeUtils'

describe('UrlEncodeUtils', () => {

    it('Deve retornar conteúdo com caracteres especiais codificados', () => {
        const urlEncodeUtils = new UrlEncodeUtils('{a}[b]`e^f\\|u')
        expect(urlEncodeUtils.encode()).toBe('%7Ba%7D%5Bb%5D%60e%5Ef%5C%7Cu')
    })

    it('Deve retornar conteúdo original', () => {
        const urlEncodeUtils = new UrlEncodeUtils('ua*/,_osdf+-/')
        expect(urlEncodeUtils.encode()).toBe('ua*/,_osdf+-/')
    })

})
