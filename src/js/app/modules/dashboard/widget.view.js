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
        tagName: 'section',
        className: 'widget',
        template: template,
        regions:{
          menu:'header',
          content: 'section.left',
          visualization: 'section.chart'
        },

        initialize: function() {
          var
            type   = {
              'year/quarter' : 'LineMonths',
              'month'        : 'LineMonths',
              'sprint'       : 'LineSprints'
            },
            seriesAttr = this.model.get('series').series;
          this.TheChart = ChartBase.extend({});
          Cocktail.mixin(
            this.TheChart,
            Mixins[type[seriesAttr]]
          );
          this.render();

        },
        onShow: function() {
          this.chart = new this.TheChart({
            //el: this.visualization.el,
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
