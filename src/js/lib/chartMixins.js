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
          margin: {top: 20, right: 20, bottom: 20, left: 20},
        },

        renderMargins: function() {
          var margin = this.options.margin;
          this.width = this.$el.width() - margin.left - margin.right;
          this.height = this.$el.height() - margin.top - margin.bottom;
          this.svg = d3.select(this.el)
            .append('svg')
              .attr('width', this.width + margin.left + margin.right)
              .attr('height', this.height + margin.top + margin.bottom)
            .append('g')
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

      // Basic bar chart
      Bar:{
        ////className: D3.Chart.prototype.className + ' bbd3-bar',

        defaults: {
            xFormat: _.identity,
            xValid: Boolean,
            yScale: 'linear',
            barPadding: 0.2
        },

        renderAxes: function() {
          // X axis
          var xAxis = d3.svg.axis()
              .scale(this.scales.x)
              .orient('bottom')
              .tickFormat(this.options.xFormat),
          // Y axis
              yAxis = d3.svg.axis()
              .scale(this.scales.y)
              .orient('left')
              .ticks(Math.ceil(this.height / 40))
              .tickSize(-this.width)
              .tickFormat(this.options.yFormat)
              .tickPadding(6);

          this.svg.append('g')
              .attr('class', 'x axis')
              .attr('transform', 'translate(0,' + this.height + ')')
              .call(xAxis);

          this.svg.append('g')
              .attr('class', 'y axis')
              .call(yAxis);
        },

        renderData: function() {
          var chart = this,
              x = this.scales.x,
              y = this.scales.y,

              bars = this.svg.selectAll('.bar')
              .data(this.collection.models, this.joinData)
              .enter().append('rect')
                  .attr('class', 'bar')
                  .attr('width', x.rangeBand())
                  .attr('height', function(d) {
                    return chart.height - y(chart.getY(d));
                  })
                  .attr('transform', function(d) {
                    return 'translate(' +
                      x(chart.getX(d)) +
                      ',' +
                      y(chart.getY(d)) +
                      ')';
                  });
        },

        getXScale: function() {
          return d3.scale.ordinal()
              .rangeRoundBands([0, this.width], this.options.barPadding)
              .domain(this.collection.pluck(this.options.xAttr));
        },

        getYScale: function() {
          return d3.scale[this.options.yScale]()
              .rangeRound([this.height, 0])
              .domain([
                  0,
                  this.getLinearExtent(this.options.yAttr, 'max')
              ])
              .nice();
        }

      }
    };

    return Mixins;
  }
);
