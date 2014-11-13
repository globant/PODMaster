define(
  [
    'underscore',
    'cocktail',
    'marionette',
    'd3'
  ],
  //jshint unused:false
  function(_, Cocktail, Marionette, d3 ) {
    'use strict';

    var Mixins = {
      MarginConvention:{

        // Default options
        defaults: {
          margin: {top: 20, right: 20, bottom: 40, left: 40},
        },

        renderMargins: function() {
          var margin = this.options.margin;
          this.width = this.$el.width() - margin.left - margin.right;
          this.height = this.$el.height() - margin.top - margin.bottom;
          d3.select('svg', this.el)
            .attr('width', this.width + margin.left + margin.right)
            .attr('height', this.height + margin.top + margin.bottom);
          this.svg
            .attr('transform',
              'translate(' +
              margin.left +
              ',' +
              margin.top +
              ')'
            );
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
          xScale: 'linear',
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
              .ticks(Math.ceil(this.width / 150))
              .tickFormat(this.options.xFormat)
              .tickPadding(5),
          // Y axis
              yAxis = d3.svg.axis()
              .scale(this.scales.y)
              .orient('left')
              .ticks(Math.ceil(this.height / 40))
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
            // Lines
            line = d3.svg.line()
              .interpolate(opts.interpolate)
              .defined(function(d) {
                return d && opts.yValid(d[opts.yAttr]);
              })
              .x(function(d) {
                return x(d[opts.xAttr]);
              })
              .y(function(d) {
                return y(d[opts.yAttr]);
              }),
            // Series
            series = this.svg.selectAll('.series')
              .data(this.collection.models)
              .enter()
                .append('g')
                .attr('class', 'series')
                .append('path')
                  .attr('class', 'line')
                  .attr('d', function(series) {
                    return line(series.get(opts.valuesAttr));
                  })
                  .style('stroke', function(series, i) {
                    return series.get(opts.colorAttr) || opts.colorScale(i);
                  });
        },

        getXScale: function() {
          return d3.scale[this.options.xScale]()
            .rangeRound([0, this.width])
            .domain(this.getLinearExtent(this.options.xAttr));
        },

        getYScale: function() {
          return d3.scale[this.options.yScale]()
            .rangeRound([this.height, 0])
            .domain([
                0, // Force scale to start from zero
                this.getLinearExtent(this.options.yAttr, 'max')
            ])
            .nice();
        }

      }
    };

    return Mixins;
  }
);
