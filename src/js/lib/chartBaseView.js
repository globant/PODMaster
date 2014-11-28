define(function(require) {
  'use strict';
  var
    _  = require('underscore'),
    d3  = require('d3'),
    Cocktail  = require('cocktail'),
    Marionette = require('marionette'),
    Mixins = require('lib/chartMixins'),

    ChartBase = Marionette.ItemView.extend({

      //a template-less view
      template: false,

      initialize: function() {
        _.defaults(
          this.options,
          _.result(this, 'defaults')
        );
      },

      render: function() {
        this.collection.bind('change reset add remove', this.renderChart, this);
      },

      onShow: function() {
        this.renderChart();
      },

      renderChart: function() {
        this.ensureSVGElement();
        this.renderMargins();

        this.scales = this.getScales();

        this.renderAxes();
        this.renderData();
      },

      ensureSVGElement: function() {
        if ( this.svg ) {
          return;
        }
        this.svg = d3.select(this.el).append('svg').append('g');
      },

      renderAxes: function() {},
      renderData: function() {}

    });

  Cocktail.mixin(
    ChartBase,
    Mixins.MarginConvention,
    Mixins.Scales
  );
  return ChartBase;
});
