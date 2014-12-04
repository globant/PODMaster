define(function(require) {
  'use strict';
  var
    _  = require('underscore'),
    d3  = require('d3'),
    Cocktail  = require('cocktail'),
    Marionette = require('marionette'),
    Mixins = require('lib/chartMixins'),

    ChartBase = Marionette.ItemView.extend({

      tagName: 'section',
      className: 'chartContainer',
      //Important: this approache ensure that the svg element
      //is properly created (avoid namespace issue with backbone)
      template: _.template('<svg xmlns="http://www.w3.org/2000/svg"></svg>'),

      initialize: function() {
        _.defaults(
          this.options,
          _.result(this, 'defaults')
        );
      },

      render: function() {
        Marionette.ItemView.prototype.render.apply(this, arguments);
        this.d3 = d3.select(this.$el.find('svg').get(0));
        this.svg = this.d3.append('g');
        this.collection.bind('change reset add remove', this.renderChart, this);
        this.$el.resize( _.bind( this.renderChart, this));
      },

      onDomRefresh: function() {
        this.renderChart();
      },

      renderChart: function() {
        this.d3.selectAll('svg>g>*').remove();
        this.renderMargins();

        this.scales = this.getScales();

        this.renderAxes();
        this.renderData();
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
