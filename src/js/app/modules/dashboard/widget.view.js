define(function(require) {
    'use strict';
    var
      Marionette = require('marionette'),
      //AreaChart  = require('widgets/areaChart'),
      template = require('hbs!app/modules/dashboard/widget'),
      moment = require('moment'),
      Cocktail  = require('cocktail'),
      Mixins = require('lib/chartMixins'),
      ChartBase = require('lib/chartBaseView'),
      TheChart = ChartBase.extend({}),
      WidgetView = Marionette.LayoutView.extend({
        el: 'section#widget',

        template: template,

        regions:{
          menu:'#header',
          content: '#left',
          visualization: '#chart'
        },

        initialize: function() {
          Cocktail.mixin(
            TheChart,
            Mixins[this.options.chartType]
          );
          this.render();
          this.chart = new TheChart({
            collection: this.model.get('series'),
            xFormat: function(date) {
              return moment(date).format('MM/YY');
              //return moment(date).format('YYYY/[Q]Q');
            }
          });

        },
        onShow: function() {
          this.visualization.show(this.chart);
        }

      });

    return WidgetView;
  }
);
