define(function(require, exports) {
    'use strict';
    var
      Backbone = require('backbone'),
      _ = require('underscore'),
      d3 = require('d3'),
      moment = require('moment');

    exports.PointModel = Backbone.Model.extend({

      toJSON: function() {
        return this.toJSON;
      }

    });

    exports.ChartSeries = Backbone.Collection.extend({
        model: exports.PointModel,
        x: d3.time.scale(),
        y: d3.scale.linear(),
        //x: d3.scale.linear().range([0,???]),

      parse: function(series) {
        var
          parseYearQuarter = function(raw) {
            var
              parsed = raw.match(/(\d{4})\/Q([1-4])/),
              year = parsed[1],
              quarter = parsed[2];
            return moment()
              .year(year)
              .quarter(quarter);
          },
          result = _(series.aggregated).groupBy(
            function(aggregation) {
              return _.findWhere(aggregation.partitions, {partition:'pod'}).key;
            })
            .map(
              function(podAggregations, podKey) {
                return {
                  name: podKey,
                  values: _.map(podAggregations,
                              function(agg) {
                                var
                                  foundX = _.findWhere(
                                    agg.partitions,
                                    {partition:'year/quarter'}),
                                  foundY = _.findWhere(
                                    agg.metrics,
                                   {name:'velocity'});
                                return {
                                  x: parseYearQuarter(foundX.key),
                                  y: foundY.value
                                };
                              })
                };
              }).value();
        return result;
      }
    });
  }
);
