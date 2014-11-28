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
      WidgetView = Marionette.LayoutView.extend({
        TheChart: ChartBase.extend({}),
        el: 'section#widget',

        template: template,

        regions:{
          menu:'#header',
          content: '#left',
          visualization: '#chart'
        },

        initialize: function() {
          Cocktail.mixin(
            this.TheChart,
            Mixins[this.options.chartType]
          );

        },
        onShow: function() {
          this.render();
          this.chart = new this.TheChart({
            collection: this.model.get('series'),
            xFormat: function(date) {
              return moment(date).format('MM/YY');
              //return moment(date).format('YYYY/[Q]Q');
            }
          });
          this.visualization.show(this.chart);
        }

      });

    return WidgetView;
  }
);
