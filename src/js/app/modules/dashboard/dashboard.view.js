define(function(require) {
    'use strict';
    var
      _             = require('underscore'),
      Marionette    = require('marionette'),
      template      = require('hbs!app/modules/dashboard/dashboard'),
      WidgetView    = require('modules/dashboard/widget.view'),

      DashboardView = Marionette.LayoutView.extend({

      el: 'section.main',

      template: template,

      regions:{
        menu:'#menu',
        content: '#content'
      },

      initialize: function() {
        var
          model = this.model.get('velocity'),
          render = _.bind(this.render, this),
          show = _.bind(this.content.show, this.content);
        render();
        show(new WidgetView({chartType:'Line', model:model}));
      }

    });

    return DashboardView;
  }
);
