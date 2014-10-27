define(
  [
    'marionette',
    'd3',
    'hbs!app/modules/widgets/agilePodsEcosystem'
  ],
  function(Marionette, d3, template) {
    'use strict';

    var BaseD3View = Marionette.ItemView.extend({
      //el: 'svg',
      initialize:function() {
        Marionette.ItemView.prototype.initialize.apply(this,arguments);
        this.d3 = d3.select(this.el);
      }
    });

    var BaseD3Container = BaseD3View.extend({
      template:template
    });

    var AgilePodsEcosystemView = BaseD3Container.extend({
      render:function() {
        //call to the `super` to render the svg element properly
        BaseD3Container.prototype.render.apply(this,arguments);
        var
          isNodeVisible= function (type, role) {
              return true; //type === 'member' && this.selectedRoles.indexOf(role) < 0 ? 'none' : 'block';
          },
          getRoleColor= function (role) {
              return 'red';//this.roleColors[this.roles.indexOf(role)];
          };
        var data = this.model.toJSON();
        var view = this.$el;

        var w = '100%';// $('#podEcosystemSVG', view).closest('.widget-content').width();
        var h = 400;//'100%';//$('#podEcosystemSVG', view).closest('.widget-content').height();
        var r = h * 0.85;
        var x = d3.scale.linear().range([0, r]),
        y = d3.scale.linear().range([0, r]),
        node,
        root;
        node = root = data;
        var pack = d3.layout.pack()
        .size([r, r])
        .value(function(d) {return isNodeVisible(d.nodetype, d.role) ? d.size : 0;})
        .padding(5);

        //var vis = this.d3
        var vis = this.d3.select('svg')
        .attr('width', w)
        .attr('height', h)
        .append('g')
        .attr('class', 'nvd3 nv-wrap')
        .append('g')
        .attr('transform', 'translate(' + (w - r) / 2 + ',' + (h - r) / 2 + ')');

        var nodes = pack.nodes(root);

        vis.selectAll('circle')
        .data(nodes)
        .enter().append('circle')
        .attr('class', function(d) {return d.children ? 'node' : 'leaf node';})
        .attr('cx', function(d) {return d.x;})
        .attr('cy', function(d) {return d.y;})
        .attr('r', function(d) {return d.r;})
        .style('fill', function(d) {return d.nodetype === 'pod' || d.nodetype === 'project' ? 'white' : getRoleColor(d.role);})
        .style('stroke', function(d) {return getCircleStroke(d, 1);})
        .attr('data-bind', function(d) {return 'attr: { display:isNodeVisible(\'' + d.nodetype + '\',\'' + d.role + '\') }';})
        .style('opacity', function(d) {return d.nodetype === 'root' ? 0 : 1;})
        .on('mouseover', function(d) {d3.select(this).style('stroke', getCircleStroke(d, 5));})
        .on('mouseout', function(d) {d3.select(this).style('stroke', getCircleStroke(d, 1));})
        .on('click', function(d) {return zoom(node === d ? root : d);})
        .append('title')
        .text(function(d) {return d.name;});


        vis.selectAll('text')
        .data(nodes)
        .enter().append('text')
        .attr('class', function(d) {return d.children ? 'node' : 'leaf node';})
        .attr('x', function(d) {return d.x;})
        .attr('y', function(d) {return d.y;})
        .attr('dy', function(d) {
          switch (d.nodetype) {
            case 'pod':
              return +(d.r + 10);
            case 'project':
              return -(d.r + 1);
            default:
              return '.35em';
          }
        })
        .attr('text-anchor', 'middle')
        .attr('data-bind', function(d) {return 'attr: { display:isNodeVisible(\'' + d.nodetype + '\',\'' + d.role + '\') }';})
        .style('opacity', function(d) {return d.nodetype !== 'project' && d.r <= 18 ? 0 : 1;})
        .style('pointer-events', 'none')
        .text(function(d) {return d.nodetype === 'pod' || d.nodetype === 'project' || (d.nodetype !== 'root' && d.r > 80) ? d.name : d.initials;});

        this.d3.on('click', function() {zoom(root);});

        var getRoleColor = this.getRoleColor;
        function getCircleStroke(d, darker) {
          if (d.nodetype === 'pod' || d.nodetype === 'project'){
            return darker === 1 ? 'black' : 'slategray';
          } else {
            return d3.rgb(getRoleColor(d.role)).darker(darker);
          }
        }

        function zoom(d) {
          var k = r / d.r / 2;
          x.domain([d.x - d.r, d.x + d.r]);
          y.domain([d.y - d.r, d.y + d.r]);

          var t = vis.transition().duration(750);

          t.selectAll('circle')
          .attr('cx', function(d) {return x(d.x);})
          .attr('cy', function(d) {return y(d.y);})
          .attr('r', function(d) {return k * d.r;});

          t.selectAll('text')
          .attr('x', function(d) {return x(d.x);})
          .attr('y', function(d) {return y(d.y);})
          .attr('dy', function(d) {
            switch (d.nodetype) {
              case 'pod':
                return +(k * d.r + 10);
              case 'project':
                return -(k * d.r + 1);
              default:
                return '.35em';
            }
          })
          .style('opacity', function(d) {return d.nodetype !== 'project' && k * d.r <= 18 ? 0 : 1;})
          .text(function(d) {return d.nodetype === 'pod' || d.nodetype === 'project' || (d.nodetype === 'member' && k * d.r > 80) ? d.name : d.initials;});

          node = d;
          d3.event.stopPropagation();
        }
      }
    });

    return AgilePodsEcosystemView;
  }
);

