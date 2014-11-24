define(function(require, exports) {
    'use strict';
    var
      Backbone = require('backbone'),
      Cocktail = require('cocktail'),
      _ = require('underscore'),
      d3 = require('d3'),
      moment = require('moment'),
      Mixins = require('lib/chartMixins');

    exports.PointModel = Backbone.Model.extend({
      get: function(attr) {
        var
          parseYearQuarter = function(raw) {
            var
              parsed = raw.match(/(\d{4})\/Q([1-4])/),
              year = parsed[1],
              quarter = parsed[2];
            return moment()
              .year(year)
              .quarter(quarter)
              .startOf('quarter')
              .unix() * 1000;
          };
        if ( attr === 'x' ) {
          return parseYearQuarter(this.attributes.x);
        }
        return Backbone.Model.prototype.get.apply(this, arguments);
      },

      toJSON: function()  {
        var
          result = Backbone.Model.prototype.toJSON.apply(this, arguments);
        result.x = this.get('x');
        return result;
      }

    });

    exports.ChartSeries = Backbone.Collection.extend({
        model: Backbone.Model,//exports.PointModel,
        x: d3.time.scale(),
        y: d3.scale.linear(),
        //x: d3.scale.linear().range([0,???]),
      initialize: function() {
        Backbone.Collection.prototype.initialize.apply(this, arguments);
        Cocktail.mixin( this, Mixins.Timeline );
      },

      toJSON: function() {
        return _.map(
          Backbone.Collection.prototype.toJSON.apply(this, arguments),
          function( item ) {
            item.values = _
            .map(
              item.values,
              function( value ) {
                return value.toJSON();
              }
            );
            return item;
          }
        );
      },

      parse: function(series) {
        var
          seriesPartition =  _.first(
              _.reject(
                series.aggregated[0].partitions,
                {partition: 'pod'}
              )
            ).partition,
          result = _(series.aggregated).groupBy(
            function(aggregation) {
              return _.findWhere(aggregation.partitions, {partition:'pod'}).key;
            })
            .map(
              function(podAggregations, podKey) {
                return {
                  name: podKey,
                  values: _.map(
                    podAggregations,
                      function(agg) {
                        var
                          foundX = _.findWhere(
                            agg.partitions,
                            {partition:seriesPartition}),
                          foundY = _.findWhere(
                            agg.metrics,
                           {name:'velocity'});
                        return new exports.PointModel({
                          x: foundX.key, //parseYearQuarter(foundX.key),
                          y: foundY.value
                        });
                      }
                  )
                };
              }).value();
        return result;
      },
      // Get the x value for a datum
      getX: function(d) {
        return this.getDatumValue(d, 'x');//this.options.xAttr);
      },

      // Get the y value for a datum
      getY: function(d) {
        return this.getDatumValue(d, 'y');//this.options.yAttr);
      },

      // Return x/y value for the given datum or model
      getDatumValue: function(d, attrName) {
        if (d instanceof Backbone.Model) {
          return d.get(attrName);
        } else {
          return d ? d[attrName] : null;
        }
      },
    });
  }
);
