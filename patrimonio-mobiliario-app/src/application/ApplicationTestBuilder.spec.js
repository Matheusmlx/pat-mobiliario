import ApplicationTestBuilder from './ApplicationTestBuilder'

describe('ApplicationTestBuilder',() =>{
    it('Deve retornar a Store',()=>{
        expect(ApplicationTestBuilder.getStore()).toBeTruthy()
    })
    it('Deve retornar a Route',()=>{
        expect(ApplicationTestBuilder.getRouter()).toBeTruthy()
    })
    it('Deve adicionar um elemento com data',()=>{
        expect(ApplicationTestBuilder._addElemWithDataAppToBody).toBeDefined()
    })

    it('Deve criar o VueRouter',()=>{
        expect(ApplicationTestBuilder._createLocalVueRouter).toBeDefined()
    })

    it('Deve instanciar o Vuetify',()=>{
        expect(ApplicationTestBuilder._createLocalVuetify).toBeDefined()
    })

    it('Deve instanciar o i18n',()=>{
        expect(ApplicationTestBuilder._createLocalVueI18n).toBeDefined()
        expect(ApplicationTestBuilder._createLocalVueI18n()).toBeUndefined()
        expect(ApplicationTestBuilder._createLocalVueI18n({locale: 'pt_BR'})).toBeUndefined()
    })
})
