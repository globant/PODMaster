define(function(require) {
    'use strict';
    var
      _          = require('underscore'),
      d3         = require('d3'),
      Mixins     = {
        Timeline: {
          // Get the minimum or maximum value over the
          // whole data for linear scales
          getLinearExtent: function(attr, minmax) {
            // Return extent over all series
            var
              valuesAttr = 'values',
              collection = this,
              getLinearExtents = function(minmax) {
                var
                  getValue = function(obj) {
                    return obj.get(attr);
                  },
                  boundary = function(arr) {
                    return _[minmax](arr, getValue);
                  },
                  getValues = function(item) {
                    return item.get(valuesAttr);
                  },
                  seriesExtents = collection.map(
                    _.compose(
                      getValue,
                      boundary,
                      getValues
                    )
                  );
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
        },
        MarginConvention:{

          // Default options
          defaults: {
            margin: {top: 20, right: 20, bottom: 80, left: 80},
          },

          renderMargins: function() {
            var margin = this.options.margin;
            this.width = this.$el.width() - margin.left - margin.right;
            this.height = this.$el.height() - margin.top - margin.bottom;
            this.d3
              .style('padding-left', margin.left )
              .style('padding-top', margin.top )
              .style('padding-right', margin.right )
              .style('padding-bottom', margin.bottom );
          }

        },
        Scales:{

          // Default options
          defaults: {
            xAttr: 'x',
            yAttr: 'y'
          },

          getScales: function() {
            return {
              x: this.getXScale(),
              y: this.getYScale()
            };
          }

        },

        // Basic area chart
        Area:{
          ////className: d3.Chart.prototype.className + ' bbd3-line',

          defaults: {
            // Values list on each series
            valuesAttr: 'values',
            // Color attribute on each series
            colorAttr: 'color',
            // Line interpolation method
            //interpolate: 'monotone',
            interpolate: 'linear',
            xScale: 'ordinal',
            yScale: 'linear',
            xFormat: _.identity,
            yFormat: _.identity,
            xValid: _.isFinite,
            yValid: _.isFinite,
            // Default color scale for lines
            colorScale: d3.scale.category10()
          },

          renderAxes: function() {
            // X axis
            var xAxis = d3.svg.axis()
                .scale(this.scales.x)
                .orient('bottom')
                //.ticks(d3.time.months)
                .tickFormat(this.options.xFormat)
                .tickPadding(5),
            // Y axis
                yAxis = d3.svg.axis()
                .scale(this.scales.y)
                .orient('left')
                //.ticks(Math.ceil(this.height / 40))
                .tickFormat(this.options.yFormat)
                .tickSize(-this.width)
                .tickPadding(10);

            this.svg.append('g')
                .attr('class', 'x axis')
                .attr('transform', 'translate(0,' + this.height + ')')
                .call(xAxis);

            this.svg.append('g')
                .attr('class', 'y axis')
                .call(yAxis);
          },

          renderData: function() {
            var
              x = this.scales.x,
              y = this.scales.y,
              opts = this.options,
              // Area
              stack = d3.layout.stack()
                .values(function(d) {
                  return d[ opts.valuesAttr ];
                }),
              area = d3.svg.area()
                .x(function(d) {
                  return x(d[ opts.xAttr ]);
                })
                .y0(function(d) {
                  return y(d.y0);
                })
                .y1(function(d) {
                  return y(d.y0 + d.y);
                });
            // Series
            this.svg.selectAll('.series')
              .data(stack(this.collection.toJSON()))
              .enter()
                .append('g')
                .attr('class', 'series')
                .append('path')
                  .attr('class', 'area')
                  .attr('d', function(d) {
                    return area(d[ opts.valuesAttr ]);
                  })
                  .style('fill', function(series, i) {
                    return series[ opts.colorAttr ] || opts.colorScale(i);
                  });
          },

          getXScale: function() {
            return d3.scale[this.options.xScale]()
            //return d3.time.scale()
              .domain(this.collection.getLinearExtent(this.options.xAttr))
              .rangeBands([0, this.width]);
          },

          getYScale: function() {
            return d3.scale[this.options.yScale]()
              .rangeRound([this.height, 0])
              .domain([
                  0, // Force scale to start from zero
                  this.collection.getLinearExtent(this.options.yAttr, 'max')
              ])
              .nice();
          }

        },
        // Basic line chart
        LineMonths:{
          ////className: d3.Chart.prototype.className + ' bbd3-line',

          defaults: {
            // Values list on each series
            valuesAttr: 'values',
            // Color attribute on each series
            colorAttr: 'color',
            // Line interpolation method
            //interpolate: 'monotone',
            interpolate: 'linear',
            xFormat: _.identity,
            yFormat: _.identity,
            xValid: _.isFinite,
            yValid: _.isFinite,
            // Default color scale for lines
            colorScale: d3.scale.category10()
          },

          renderAxes: function() {
            // X axis
            var xAxis = d3.svg.axis()
                .scale(this.scales.x)

                //.ticks(d3.count(this.scales.x.domain()))
                .tickFormat(d3.time.format('%m'))
                //.ticks(d3.time.month)
                .tickSize(-this.height)
                .tickPadding(5),
            // Y axis
                yAxis = d3.svg.axis()
                .scale(this.scales.y)
                .orient('left')
                //.ticks(Math.ceil(this.height / 40))
                .tickFormat(this.options.yFormat)
                .tickSize(-this.width)
                .tickPadding(10);

            this.svg.append('g')
                .attr('class', 'x axis')
                .attr('transform', 'translate(0,' + this.height + ')')
                .call(xAxis)
                //Render axis legend
                .append('text')
                .attr('x', (this.width / 2) )
                .attr('y',  10 )
                .attr('dy', '.71em')
                .style('text-anchor', 'middle')
                .style('font-size', '16px')
                .text('Months');

            this.svg.append('g')
                .attr('class', 'y axis')
                .call(yAxis)
                //Render axis legend
                .append('text')
                .attr('transform',  'rotate(-90)' )
                .attr('y', -40 )
                .attr('dy', '.71em')
                .style('text-anchor', 'middle')
                .style('font-size', '16px')
                .text('Completed Story Points');
          },

          renderData: function() {
            var
              x = this.scales.x,
              y = this.scales.y,
              opts = this.options,
              // Lines
              line = d3.svg.line()
                .interpolate(opts.interpolate)
                .defined(function(d) {
                  return d && opts.yValid(d.get( opts.yAttr ));
                })
                .x(function(d) {
                  return x(d.get( opts.xAttr ));
                })
                .y(function(d) {
                  return y(d.get( opts.yAttr ));
                });
            // Series
            this.svg.selectAll('.series')
              .data(this.collection.models)
              .enter()
                .append('g')
                .attr('class', 'series')
                .append('path')
                  .attr('class', 'line')
                  .attr('d', function(series) {
                    return line(
                      series.get(opts.valuesAttr)
                    );
                  })
                  .style('stroke', function(series, i) {
                    return series.get(opts.colorAttr) || opts.colorScale(i);
                  });
          },

          getXScale: function() {
            var
              values = this.collection.pluck('values'),
              getter = function(prop, o) {
                return o.get(prop);
              },
              boundary = function(minmax) {
                return _[minmax](
                  _.map(
                    values,
                    _.wrap(
                      _.wrap('x', getter),
                      function(get, arr) {
                        return get(_[minmax](arr, get));
                      }
                    )
                  )
                );
              },
              upper = boundary('max'),
              lower = boundary('min');

            return d3.time.scale()
              .domain([lower, upper])
              .rangeRound([0, this.width]);
          },

          getYScale: function() {
            return d3.scale.linear()
              .domain([
                  this.collection.getLinearExtent(this.options.yAttr, 'min'),
                  this.collection.getLinearExtent(this.options.yAttr, 'max')
              ])
              .rangeRound([this.height, 0])
              .nice();
          }
        },
        // Basic line chart
        LineSprints:{
          ////className: d3.Chart.prototype.className + ' bbd3-line',

          defaults: {
            // Values list on each series
            valuesAttr: 'values',
            // Color attribute on each series
            colorAttr: 'color',
            // Line interpolation method
            //interpolate: 'monotone',
            interpolate: 'linear',
            xFormat: _.identity,
            yFormat: _.identity,
            xValid: _.isFinite,
            yValid: _.isFinite,
            // Default color scale for lines
            colorScale: d3.scale.category10()
          },

          renderAxes: function() {
            // X axis
            var xAxis = d3.svg.axis()
                .scale(this.scales.x)

                .ticks(d3.max(this.scales.x.domain()))
                //.tickFormat(this.options.xFormat)
                .tickSize(-this.height)
                .tickPadding(5),
            // Y axis
                yAxis = d3.svg.axis()
                .scale(this.scales.y)
                .orient('left')
                //.ticks(Math.ceil(this.height / 40))
                .tickFormat(this.options.yFormat)
                .tickSize(-this.width)
                .tickPadding(10);

            this.svg.append('g')
                .attr('class', 'x axis')
                .attr('transform', 'translate(0,' + this.height + ')')
                .call(xAxis)
                //Render axis legend
                .append('text')
                .attr('x', (this.width / 2) )
                .attr('y',  40 )
                .attr('dy', '.71em')
                .style('text-anchor', 'middle')
                .style('font-size', '16px')
                .text('Sprints');

            this.svg.append('g')
                .attr('class', 'y axis')
                .call(yAxis)
                //Render axis legend
                .append('text')
                .attr('transform',  'rotate(-90)' )
                .attr('x', -(this.height / 2) )
                .attr('y', -60 )
                .attr('dy', '.71em')
                .style('text-anchor', 'middle')
                .style('font-size', '16px')
                .text('Completed Story Points');
          },

          renderData: function() {
            var
              x = this.scales.x,
              y = this.scales.y,
              opts = this.options,
              // Lines
              line = d3.svg.line()
                .interpolate(opts.interpolate)
                .defined(function(d) {
                  return d && opts.yValid(d.get( opts.yAttr ));
                })
                .x(function(d) {
                  return x(d.get( opts.xAttr ));
                })
                .y(function(d) {
                  return y(d.get( opts.yAttr ));
                });
            // Series
            this.svg.selectAll('.series')
              .data(this.collection.models)
              .enter()
                .append('g')
                .attr('class', 'series')
                .append('path')
                  .attr('class', 'line')
                  .attr('d', function(series) {
                    return line(
                      series.get(opts.valuesAttr)
                    );
                  })
                  .style('stroke', function(series, i) {
                    return series.get(opts.colorAttr) || opts.colorScale(i);
                  });
          },

          getXScale: function() {
            var
              values = this.collection.pluck('values'),
              getter = function(prop, o) {
                return o.get(prop);
              },
              boundary = function(minmax) {
                return _[minmax](
                  _.map(
                    values,
                    _.wrap(
                      _.wrap('x', getter),
                      function(get, arr) {
                        return get(_[minmax](arr, get));
                      }
                    )
                  )
                );
              },
              upper = boundary('max'),
              lower = boundary('min');

            return d3.scale.ordinal()
              .domain(_.range(lower, upper + 2))
              .rangePoints([ 0, this.width ]);
          },

          getYScale: function() {
            return d3.scale.linear()
              .domain([
                  this.collection.getLinearExtent(this.options.yAttr, 'min'),
                  this.collection.getLinearExtent(this.options.yAttr, 'max')
              ])
              .rangeRound([this.height, 0])
              .nice();
          }
        }
    };

    return Mixins;
  }
);
