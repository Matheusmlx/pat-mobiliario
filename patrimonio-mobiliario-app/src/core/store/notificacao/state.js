export default {
  notificacao: {
    page: 1,
    items: [],
  },
  configuracaoNotificacao: {
    refreshTimeout: 30 * 1000,
    activeFilter: 'Todas',
    filters: ['Todas', 'Lidas', 'Não Lidas'],
    title: 'Notificações',
    endNotificationText: 'Fim das notificações.',
    noNotificationText: 'Nenhuma notificação encontrada...',
    viewMoreText: 'Ver mais',
    notificationIcon: 'fas fa-bell',
    whenIcon: 'alarm',
    closeIcon: '',
    allowLoadingViewMore: true,
    closeMenuOnVisit: true
  },
  noNotificationTextAuxiliar: ''
}
