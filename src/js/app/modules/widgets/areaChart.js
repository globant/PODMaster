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

    var AreaChart = ChartBase.extend({});

    Cocktail.mixin(
      AreaChart,
      Mixins.Area
    );
    return AreaChart;
  }
);
