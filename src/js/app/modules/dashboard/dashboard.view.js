define(
  [
    'marionette',
    '../../entities/dashboard.model',
    'hbs!app/modules/dashboard/dashboard'
  ],
  function(Marionette, DashboardModel, template) {
    'use strict';

    var DashboardView = Marionette.LayoutView.extend({

      //el: 'section',

      template: template,
      regions:{
        menu:'#menu',
        content: '#content'
      },

      initialize: function() {
        this.model = new DashboardModel();
        this.model.fetch();
        //this.listenTo(this.model, 'change', this.render);
      },

    });

    return DashboardView;
  }
);
