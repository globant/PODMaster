define(function(require) {
    'use strict';
    var
      _             = require('underscore'),
      Backbone      = require('backbone'),
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
          model = this.model,
          WidgetsCompositeView = Marionette.CompositeView.extend({
            tagName: 'section',
            className: 'widgets',
            template: _.template('<section class="list"></section>'),
            childView: WidgetView,
            childViewContainer: 'section.list'
          }),
          widgetsCollection = new Backbone.Collection();
        this.render();
        this.content
        .show( new WidgetsCompositeView({ collection:  widgetsCollection }));
        model.get('velocity')
        .on(
          'reset change',
          _.bind(widgetsCollection.add, widgetsCollection)
        );
        model.get('accumulated-story-points')
        .on(
          'reset change',
          _.bind(widgetsCollection.add, widgetsCollection)
        );
        //widgetsCollection.add(model.get('velocity'));
        //widgetsCollection.add(model.get('accumulated-story-points'));
      }

    });

    return DashboardView;
  }
);
