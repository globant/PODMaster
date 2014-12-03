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

    exports.MonthPointModel = Backbone.Model.extend({
      parse: function(raw) {
        var
          parseMonth = function(raw) {
            return moment()
              .month(raw)
              .startOf('month')
              .unix() * 1000;
          };
        raw.x = parseMonth(raw.x);
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
        raw.x = raw.x.replace(/[^0-9]*/, '') * 1;
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
        this.get('series').reset(
          {
            series:data.series,
            data: _(data.rawData)
              .groupBy(
                function(aggregation) {
                  return _.findWhere(
                    aggregation.partitions,
                    {partition:data.categories}
                  ).key;
                }
              )
              .map(
                function(aggregations, aggregationKey) {
                  var
                    PointModel = ({
                      'year/quarter' : exports.YearQuarterPointModel,
                      'month'        : exports.MonthPointModel,
                      'sprint'       : exports.SprintPointModel
                    })[data.series];
                  return {
                    name: aggregationKey,
                    values: _.reduce(
                      aggregations,
                      function(result, agg ) {
                        var
                          sortedXPush = function( array, value ) {
                            var
                              getX = function(val) {
                                return val.get('x');
                              };
                            array.splice(
                              _.sortedIndex(
                                array,
                                value,
                                getX
                              ),
                              0,
                              value
                            );
                          },
                          foundX = _.findWhere(
                            agg.partitions,
                            {partition:data.series}
                          ),
                          foundY = _.findWhere(
                            agg.metrics,
                            {name:data.name}
                          );
                        if (!_.isUndefined(foundX)) {
                          sortedXPush(
                            result,
                            new PointModel({
                              x: foundX.key,
                              y: foundY.value
                            },
                            {parse:true})
                          );
                        }
                        return result;
                      }, [])
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
        var
        metrics = {
          'accumulated-story-points': exports.MetricModel,
          'velocity': exports.MetricModel,
          'bugs': exports.MetricModel,
          'accuracy-of-estimations': exports.MetricModel,
          'stability-of-velocity': exports.MetricModel,
          'remaining-story-points': Backbone.Model,
          'PrecisionMovement': Backbone.Model
        },
        setter = _.bind(this.set, this),
        createAndSet = function(metric) {
          setter( metric, new metrics[metric]());
        };
        _(metrics)
          .keys()
          .each(createAndSet);
      },

      parse: function(partitions) {
        var
          getMetric = _.bind(this.get, this),
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
                theMetric = getMetric(metricName);
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
