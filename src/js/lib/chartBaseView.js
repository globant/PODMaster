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
        this.collection.bind('change reset add remove', this.render, this);
      },

      render: function() {
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

      // Get the minimum or maximum value over the whole data for linear scales
      getLinearExtent: function(attr, minmax) {
        // Return extent over all series
        var
          opts = this.options,
          collection = this.collection,
          getLinearExtents = function(minmax) {
            var
              boundary = _[minmax],
              rank = _.property(attr),
              valuesAttr = opts.valuesAttr,
              seriesExtents = collection.map(
                function(series) {
                  var
                    values = series.get(valuesAttr),
                    bounds = boundary(values, rank);
                  return bounds[attr];
                },
              this);
            return _[minmax](seriesExtents);
          };
        if (!minmax) {
          return [
            getLinearExtents('min'),
            getLinearExtents('max')
          ];
        }
        return getLinearExtents(minmax);
      },

      // Get the x value for a datum
      getX: function(d) {
        return this.getDatumValue(d, this.options.xAttr);
      },

      // Get the y value for a datum
      getY: function(d) {
        return this.getDatumValue(d, this.options.yAttr);
      },

      // Return x/y value for the given datum or model
      getDatumValue: function(d, attrName) {
        if (d instanceof Backbone.Model) {
          return d.get(attrName);
        } else {
          return d ? d[attrName] : null;
        }
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
