define(
  [
    'marionette',
    '../../entities/dashboard.model',
    'templateregistry'
  ],
  function(Marionette, DashboardModel, JST) {
    'use strict';

    var DashboardView = Marionette.LayoutView.extend({

      el: 'section',

      template: 'dashboard.hbs',

      initialize: function() {
        this.model = new DashboardModel();
        this.tplFunction = JST[this.template];
        this.model.fetch();
        this.listenTo(this.model, 'change', this.render);
      },

      render: function() {
      }

    });

    return DashboardView;
  }
);
