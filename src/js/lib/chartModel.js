define(function(require, exports) {
    'use strict';
    var
      Backbone = require('backbone'),
      Cocktail = require('cocktail'),
      _ = require('underscore'),
      d3 = require('d3'),
      moment = require('moment'),
      Mixins = require('lib/chartMixins');

    exports.AxisModel = Backbone.Model.extend({

    });

    exports.YearQuarterPointModel = Backbone.Model.extend({
      parse: function(raw) {
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
        raw.x = parseYearQuarter(raw.x);
        return raw;
      },

      toJSON: function()  {
        var
          result = Backbone.Model.prototype.toJSON.apply(this, arguments);
        result.x = this.get('x');
        return result;
      }
    });

    exports.SprintPointModel = Backbone.Model.extend({
      parse: function(raw) {
        return raw;
      }
    });

    exports.SeriesCollection = Backbone.Collection.extend({
      model: Backbone.Model,//exports.PointModel,

      initialize: function() {
        Backbone.Collection.prototype.initialize.apply(this, arguments);
        Cocktail.mixin( this, Mixins.Timeline );
      },

      parse: function(raw) {
        this.series = raw.series;
        return raw.data;
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
      }
    });

    exports.MetricModel = Backbone.Model.extend({
      initialize: function() {
        this.set('series', new exports.SeriesCollection([]));
      },

      parse: function(data) {
        var
          result = {
            name: data.name,
            categories: data.categories,
            series: this.get('series')
          };
        this.get('series').reset(//new exports.SeriesCollection(
          {
            series:data.series,
            data: _(data.rawData).groupBy(
              function(aggregation) {
                return _.findWhere(
                  aggregation.partitions,
                  {partition:data.categories}
                ).key;
              })
              .map(
                function(podAggregations, podKey) {
                  var
                    PointModel = ({
                      'year/quarter' : exports.YearQuarterPointModel,
                      'sprint'       : exports.SprintPointModel
                    })[data.series];
                  return {
                    name: podKey,
                    values: _.map(
                      podAggregations,
                      function(agg) {
                        var
                          foundX = _.findWhere(
                            agg.partitions,
                            {partition:data.series}),
                          foundY = _.findWhere(
                            agg.metrics,
                           {name:'velocity'});
                        return new PointModel({
                            x: foundX.key,
                            y: foundY.value
                          }, {parse:true});
                      })
                  };
                }).value()
            },
            {parse: true}
        );
        return result;
      }
    });

    exports.MetricsModel = Backbone.Model.extend({
      scaleX: d3.time.scale(),
      scaleY: d3.scale.linear(),

      initialize: function() {
        this.set('velocity', new exports.MetricModel());
      },

      parse: function(partitions) {
        var
          theMetrics = {
            velocity:  this.get('velocity')
          },
          //Applicative `if`: returns a function that confitionally apply
          // trueF or falseF depending on the result of confF
          ifF = function(condF, trueF, falseF) {
            return function(arg) {
              return condF(arg) ? trueF(arg) : falseF(arg);
            };
          },
          // Check if an array's length is exactly 1
          isSizeEqualOne = _.compose(
            _.partialRight(_.isEqual, 1),
            _.size
          ),
          deepGetter = function(where, what) {
            return _.compose(
              _.partialRight(_.pluck, what),
              _.property(where)
            );
          },
          getMetricName = deepGetter('metrics', 'name'),
          getPartitionName = deepGetter('partitions', 'partition'),
          metricNames = _(partitions.aggregated)
            .map(getMetricName)
            .flatten()
            .unique()
          .value(),
          partitionNames = _(partitions.aggregated)
            .map(getPartitionName)
            .flatten()
            .unique()
            .groupBy(
              function(partition)
              {
                return _.contains(
                  ['pod', 'project'],
                  partition
                ) ? 'categories' : 'series';
              }
            )
            .mapValues( ifF( isSizeEqualOne, _.first, _.identity) )
          .value(),
          result = _(metricNames).reduce(
            function(result, metricName) {
              var
                theMetric = theMetrics[metricName];
              result[metricName] = theMetric//this.get(metricName)
              .set(
                theMetric.parse(
                  {
                    name:metricName,
                    categories: partitionNames.categories,
                    series: partitionNames.series,
                    rawData: partitions.aggregated
                  }
                )
              );
              return result;
            },
            {}
          );
        return result;
      }

    });

  }
);
