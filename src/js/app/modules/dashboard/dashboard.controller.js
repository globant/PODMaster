define(
  [
    'backbone',
    'marionette',
    '../../entities/dashboard.model',
    './dashboard.view',
    '../widgets/barChart'
  ],
  function(Backbone, Marionette, DashboardModel, DashboardView, BarChart) {
    'use strict';

    var DashboardController = Marionette.Controller.extend({

      handleDefaultRoute: function() {
        //jshint unused:false
        var dashboardView = new DashboardView(),
          // Create chart object
          chart = new BarChart({

          // View options
          el: this.el,
          collection: new Backbone.Collection([
            {A:10, B:20},
            {A:40, B:10},
            {A:50, B:30}
          ]),

          // Override default options to match data
          // (use default margins)
          xAttr: 'A',
          yAttr: 'B'
        });
        // Render the outer element
        dashboardView.render();
        // Render the chart (Sets margins, gets scales, renders data)
        dashboardView.content.show(chart);
      }

    });

    return DashboardController;
  }
);
