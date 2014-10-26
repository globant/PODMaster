define(
  [
    'backbone',
    'marionette',
    '../../entities/dashboard.model',
    './dashboard.view'
  ],
  function(Backbone, Marionette, DashboardModel, DashboardView) {
    'use strict';

    var DashboardController = Marionette.Controller.extend({

      handleDefaultRoute: function() {
        //jshint unused:false
        var dashboardView = new DashboardView();
        dashboardView.render();
      }

    });

    return DashboardController;
  }
);
