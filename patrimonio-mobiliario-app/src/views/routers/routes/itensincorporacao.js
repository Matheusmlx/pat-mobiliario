import ModalIncorporacaoItem
  from '@/views/components/modal/incorporacao/adicionaritem/ModalIncorporacaoItem'
import DadosGeraisDoItemModal
  from '@/views/components/modal/incorporacao/adicionaritem/itemincorporacao/edicao/DadosGeraisDoItemModal'
import VisualizacaoDadosGerais
  from '@/views/components/modal/incorporacao/adicionaritem/itemincorporacao/visualizacao/DadosGeraisDoItemVisualizacao'
import listagemPatrimonios
  from '@/views/components/modal/incorporacao/adicionaritem/itempatrimonio/edicao/IncorporacaoListagemPatrimonios'
import ItensCatalogoModalNovo
  from '@/views/components/modal/incorporacao/adicionaritem/itemcatalogo/novo/ItensCatalogoModalNovo'
import ItensCatalogoModalEdicao
  from '@/views/components/modal/incorporacao/adicionaritem/itemcatalogo/edicao/ItensCatalogoModalEdicao'
import ItensCatalogoVisualizacao
  from '@/views/components/modal/incorporacao/adicionaritem/itemcatalogo/visualizacao/ItensCatalogoModalVisualizacao'
import listagemPatrimoniosVisualizacao
  from '@/views/components/modal/incorporacao/adicionaritem/itempatrimonio/visualizacao/IncorporacaoListagemPatrimonios'

export default {
  edicao: [
    {
      path: '/incorporacao/',
      name: 'ModalIncorporacaoItem',
      component: ModalIncorporacaoItem,
      children: [
        {
          path: ':incorporacaoId/itemcatalogo/novo',
          name: 'ItensCatalogoModalNovo',
          component: ItensCatalogoModalNovo,
          meta: {
            page: {
              title: 'Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 1 - Selecione o item',
              podeVoltar: false,
              showModal: true,
              visualizacao: false,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal'],
          },
        },
        {
          path: ':incorporacaoId/itemcatalogo/edicao/:itemIncorporacaoId',
          name: 'ItensCatalogoModalEdicao',
          component: ItensCatalogoModalEdicao,
          meta: {
            page: {
              title: 'Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 1 - Selecione o item',
              podeVoltar: false,
              showModal: true,
              visualizacao: false,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal'],
          },
        },
        {
          path: ':incorporacaoId/itemincorporacao/:itemIncorporacaoId',
          name: 'DadosGeraisDoItemModal',
          component: DadosGeraisDoItemModal,
          meta: {
            page: {
              title: 'Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 2 - Dados gerais do item',
              podeVoltar: true,
              showModal: true,
              visualizacao: false,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal'],
          },
        },
        {
          path: ':incorporacaoId/item-patrimonios/:itemIncorporacaoId',
          name: 'IncorporacaoItemListagemPatrimonios',
          component: listagemPatrimonios,
          meta: {
            page: {
              title: 'Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 3 - Dados individuais dos patrimônios',
              podeVoltar: true,
              showModal: true,
              visualizacao: false,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal'],
          },
        },
      ],
    }],
  visualizacao: [
    {
      path: '/incorporacao/',
      name: 'ModalIncorporacaoItemVisualizacao',
      component: ModalIncorporacaoItem,
      children: [
        {
          path: ':incorporacaoId/itemcatalogo/visualizacao/',
          name: 'ItensCatalogoModalVisualizacao',
          component: ItensCatalogoVisualizacao,
          meta: {
            page: {
              title: 'Visualizar Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 1 - Selecione o item',
              podeVoltar: false,
              showModal: true,
              visualizacao: true,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Consulta'],
          },
        },
        {
          path: ':incorporacaoId/itemcatalogo/visualizacao/:itemIncorporacaoId',
          name: 'ItensCatalogoModalVisualizacaoCadastrado',
          component: ItensCatalogoVisualizacao,
          meta: {
            page: {
              title: 'Visualizar Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 1 - Selecione o item',
              podeVoltar: false,
              showModal: true,
              visualizacao: true,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Consulta'],
          },
        },
        {
          path: ':incorporacaoId/itemincorporacao/visualizacao/:itemIncorporacaoId',
          name: 'VisualizacaoDadosGerais',
          component: VisualizacaoDadosGerais,
          meta: {
            page: {
              title: 'Visualizar Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 2 - Visualização Dados Gerais do Item',
              podeVoltar: true,
              showModal: true,
              visualizacao: true,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Consulta'],
          },
        },
        {
          path: ':incorporacaoId/item-patrimonios/visualizacao/:itemIncorporacaoId',
          name: 'IncorporacaoItemListagemPatrimoniosVisualizacao',
          component: listagemPatrimoniosVisualizacao,
          meta: {
            page: {
              title: 'Visualizar Itens da Incorporação',
              showAutoSave: true,
            },
            modal: {
              title: 'Etapa 3 - Dados individuais dos patrimônios',
              podeVoltar: true,
              showModal: true,
              visualizacao: true,
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Consulta'],
          },
        },
      ],
    }],
}
