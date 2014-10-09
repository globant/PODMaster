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

      initialize: function(options) {
      },

      handleDefaultRoute: function(path) {
        var dashboardView = new DashboardView();
      }

    });

    return DashboardController;
  }
);
