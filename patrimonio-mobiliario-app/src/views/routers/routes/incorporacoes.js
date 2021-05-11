import IncorporacaoListagem
  from '@/views/pages/patrimonios/incorporacao/listagem/IncorporacaoListagem'
import IncorporacaoEdicao
  from '@/views/pages/patrimonios/incorporacao/edicao/IncorporacaoEdicao'
import VisualizarIncorporacao
  from '@/views/pages/patrimonios/incorporacao/visualizacao/dados-gerais/VisualizarIncorporacao'
import EditarIncorporacao
  from '@/views/pages/patrimonios/incorporacao/edicao/dados-gerais/DadosGerais'
import ItensListagem
  from '@/views/pages/patrimonios/incorporacao/edicao/itens/ItensListagem'
import Documentos
  from '@/views/pages/patrimonios/incorporacao/edicao/documentos/ListagemDocumentos'
import DocumentosVisualizacao
  from '@/views/pages/patrimonios/incorporacao/visualizacao/documentos/ListagemDocumentosVisualizar'
import VisualizarRegistroIncorporacao
  from '@/views/pages/patrimonios/incorporacao/visualizacaoRegistro/VisualizarRegistroIncorporacao'
import VisualizarRegistroIncorporacaoVisualizar
  from '@/views/pages/patrimonios/incorporacao/visualizacao/visualizacaoRegistro/VisualizarRegistroIncorporacaoVisualizar'
import ModalItensRegistroIncorporacao
  from '@/views/components/modal/incorporacao/registroIncorporacao/ModalItensRegistroIncorporacao'
import ModalEstornarPatrimoniosMotivo
  from '@/views/components/modal/incorporacao/registroIncorporacao/estorno/ModalEstornarPatrimoniosMotivo'
import ModalEstornarPatrimonios
  from '@/views/components/modal/incorporacao/registroIncorporacao/estorno/ModalEstornarPatrimonios'
import ModalEstornarPatrimoniosListagem
  from '@/views/components/modal/incorporacao/registroIncorporacao/estorno/ModalEstornarPatrimoniosListagem'
import ModalEstornarPatrimoniosFinalizar
  from '@/views/components/modal/incorporacao/registroIncorporacao/estorno/ModalEstornarPatrimoniosFinalizar'
import ModalEstornarPatrimoniosVisualizarEstorno
  from '@/views/components/modal/incorporacao/registroIncorporacao/estorno/ModalEstornarPatrimoniosVisualizarEstorno'
import itensincorporacao from '@/views/routers/routes/itensincorporacao'

const itensIncorporacaoEdicao = itensincorporacao.edicao
const itensIncorporacaoVisualizacao = itensincorporacao.visualizacao

export default [
  {
    path: '/incorporacao',
    name: 'IncorporacaoListagem',
    component: IncorporacaoListagem,
    meta: {
      menu: {
        title: 'Incorporações',
      },
      page: {
        title: 'Incorporações',
        subtitle: 'Lista geral de cadastro de incorporações',
      },
      requiresAuth: true,
      authorities: ['Mobiliario.Normal', 'Mobiliario.Consulta'],
    },
  },
  {
    path: '/incorporacao/:incorporacaoId',
    name: 'IncorporacaoEdicao',
    component: IncorporacaoEdicao,
    children: [
      {
        path: 'editar',
        name: 'EditarIncorporacao',
        component: EditarIncorporacao,
        meta: {
          page: {
            title: 'Editar Incorporação',
            showAutoSave: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Normal'],
        },
      },
      {
        path: 'visualizacao',
        name: 'VisualizarIncorporacao',
        component: VisualizarIncorporacao,
        meta: {
          page: {
            title: 'Visualizar Incorporação',
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Consulta'],
        },
      },
      {
        path: 'itensIncorporacao',
        name: 'ItensIncorporacaoListagem',
        component: ItensListagem,
        meta: {
          page: {
            title: 'Itens da Incorporação',
            showAutoSave: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Normal'],
        },
        children: [
          ...itensIncorporacaoEdicao,
        ],
      },
      {
        path: 'itensIncorporacao/visualizacao',
        name: 'VisualizarItensIncorporacao',
        component: ItensListagem,
        meta: {
          page: {
            title: 'Visualizar Itens da Incorporação',
            showAutoSave: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Consulta'],
        },
        children: [
          ...itensIncorporacaoVisualizacao,
        ],
      },
      {
        path: 'documentos/edicao',
        name: 'IncorporacaoDocumentosEdicao',
        component: Documentos,
        meta: {
          page: {
            title: 'Editar Entrada',
            showAutoSave: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Normal'],
        },
      },
      {
        path: 'documentos/visualizacao',
        name: 'VisualizarIncorporacaoDocumentos',
        component: DocumentosVisualizacao,
        meta: {
          page: {
            title: 'Editar Entrada',
            showAutoSave: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Consulta'],
        },
      },
    ],
  },
  {
    path: '/incorporacao/:incorporacaoId/visualizacaoRegistro',
    name: 'VisualizarRegistroIncorporacao',
    component: VisualizarRegistroIncorporacao,
    meta: {
      page: {
        title: 'Visualizar Incorporação',
      },
      requiresAuth: true,
      authorities: ['Mobiliario.Normal'],
    },
    children: [
      {
        path: '/incorporacao/:incorporacaoId/visualizacaoRegistro/item/:itemIncorporacaoId',
        name: 'ModalItemVisualizarRegistro',
        component: ModalItensRegistroIncorporacao,
        meta: {
          page: {
            title: 'Visualizar Incorporação',
            showAutoSave: true,
          },
          modal: {
            showModal: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Normal'],
          scrollTop: false,
          scrollTopAfterLeave: false
        },
      },
      {
        path: 'estornarPatrimonios',
        name: 'ModalEstornarPatrimonios',
        component: ModalEstornarPatrimonios,
        meta: {
          requiresAuth: true,
          authorities: ['Mobiliario.Normal'],
        },
        children: [
          {
            path: 'motivo',
            name: 'ModalEstornarPatrimoniosMotivo',
            component: ModalEstornarPatrimoniosMotivo,
            meta: {
              requiresAuth: true,
              authorities: ['Mobiliario.Normal'],
            },
          },
          {
            path: 'listagem',
            name: 'ModalEstornarPatrimoniosListagem',
            component: ModalEstornarPatrimoniosListagem,
            meta: {
              requiresAuth: true,
              authorities: ['Mobiliario.Normal'],
            },
          },
          {
            path: 'finalizar',
            name: 'ModalEstornarPatrimoniosFinalizar',
            component: ModalEstornarPatrimoniosFinalizar,
            meta: {
              requiresAuth: true,
              authorities: ['Mobiliario.Normal'],
            },
          },
          {
            path: 'visualizarEstorno',
            name: 'ModalEstornarPatrimoniosVisualizarEstorno',
            component: ModalEstornarPatrimoniosVisualizarEstorno,
            meta: {
              requiresAuth: true,
              scrollTop: false,
              scrollTopAfterLeave: false,
              authorities: ['Mobiliario.Normal'],
            },
          },
        ],
      },
    ],
  },
  {
    path: '/incorporacao/:incorporacaoId/visualizacaoRegistro/visualizacao',
    name: 'VisualizarRegistroIncorporacaoVisualizacao',
    component: VisualizarRegistroIncorporacaoVisualizar,
    meta: {
      page: {
        title: 'Visualizar Incorporação',
        showAutoSave: true,
      },
      requiresAuth: true,
      authorities: ['Mobiliario.Consulta'],
    },
    children: [
      {
        path: '/incorporacao/:incorporacaoId/visualizacaoRegistro/item/:itemIncorporacaoId/visualizacao',
        name: 'ModalItemVisualizarRegistroVisualizacao',
        component: ModalItensRegistroIncorporacao,
        meta: {
          page: {
            title: 'Visualizar Incorporação',
            showAutoSave: true,
          },
          modal: {
            showModal: true,
          },
          requiresAuth: true,
          authorities: ['Mobiliario.Consulta'],
        },
      },
    ],
  },
]
