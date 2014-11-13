define(
  [
    'underscore',
    'd3',
    'cocktail',
    'lib/chartMixins',
    'lib/chartBaseView'
  ],
  function(_, d3, Cocktail, Mixins, ChartBase) {
    'use strict';
    //jshint unused:false

    var BarChart = ChartBase.extend({
      getLinearExtent: function(attr, minmax) {
        if (!minmax) {
          // Keep recursive behavior
          return ChartBase.prototype.getLinearExtent.apply(this, arguments);
        }

        // Return extent over all series
        var
          boundary = _[minmax],
          rank = _.property(attr),
          valuesAttr = this.valuesAttr,
          seriesExtents = this.collection.map(
            function(series) {
              var
                values = series.get(valuesAttr),
                bounds = boundary(values, rank);
              return bounds[attr];
            },
          this);
        return _[minmax](seriesExtents);
      }
    });

    Cocktail.mixin(
      BarChart,
      Mixins.Line
    );
    return BarChart;
  }
);
