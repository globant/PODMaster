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
          render = _.bind(this.render, this),
          show   = _.bind(this.content.show, this.content),
          type   = {
            'year/quarter' : 'LineMonths',
            'month'        : 'LineMonths',
            'sprint'       : 'LineSprints'
          },
          createWidget = function(model) {
            var
              seriesAttr = model.get('series').series;
            return new WidgetView({chartType: type[seriesAttr], model:model});
          };
        render();
        this.model
        .get('velocity')
        //.get('accumulated-story-points')
          .bind('reset change', _.compose(show, createWidget));
      }

    });

    return DashboardView;
  }
);
