define(
  [
    'marionette',
    'backbone',
    'hbs!app/modules/dashboard/dashboard'
  ],
  function(Marionette, Backbone, template) {
    'use strict';

    var DashboardView = Marionette.LayoutView.extend({

      el: 'section.main',

      template: template,

      regions:{
        menu:'#menu',
        content: '#content'
      }

    });

    return DashboardView;
  }
);
