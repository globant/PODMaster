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

    var BarChart = ChartBase.extend({});

    Cocktail.mixin(
      BarChart,
      Mixins.Bar
    );
    return BarChart;
  }
);
