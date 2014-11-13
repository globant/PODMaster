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

        Marionette.ItemView.prototype.initialize(this, arguments);
      },

      onShow: function() {

        this.renderMargins();

        this.scales = this.getScales();

        this.renderAxes();
        this.renderData();
      },
      // Get the minimum or maximum value over the whole data for linear scales
      getLinearExtent: function(attr, minmax) {
        // Return either one extreme or whole extent
        if (minmax) {
          return _[minmax](this.collection.pluck(attr));
        } else {
          return [
            this.getLinearExtent(attr, 'min'),
            this.getLinearExtent(attr, 'max')
          ];
        }
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
