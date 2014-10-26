define(
  [
    'marionette',
    'hbs!app/modules/dashboard/dashboard'
  ],
  function(Marionette, template) {
    'use strict';

    var DashboardView = Marionette.LayoutView.extend({

      el: 'section.main',

      template: template,
      regions:{
        menu:'#menu',
        content: '#content'
      },

    });

    return DashboardView;
  }
);
