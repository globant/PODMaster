define(
  [
    'underscore',
    'backbone',
    'cocktail',
    'marionette',
    'd3',
    'lib/chartMixins'
  ],
  function(_, Backbone, Cocktail, Marionette, d3, Mixins ) {
    'use strict';

    var ChartBase = Marionette.ItemView.extend({

      //a template-less view
      template: false,

      initialize: function() {
        _.defaults(
          this.options,
          _.result(this, 'defaults')
        );

        // jscs:disable disallowDanglingUnderscores
        this.__super__ = Marionette.ItemView.prototype;
        this.__super__.initialize(this, arguments);
        // jscs:enable disallowDanglingUnderscores
      },

      render: function() {
        this.collection.bind('change reset add remove', this.renderChart, this);
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
  }
);
