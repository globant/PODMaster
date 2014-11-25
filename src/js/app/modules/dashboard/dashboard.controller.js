define(function(require) {
    'use strict';
    var
      _              = require('underscore'),
      $              = require('jquery'),
      Marionette     = require('marionette'),
      DashboardView  = require('modules/dashboard/dashboard.view'),
      chartModel     = require('lib/chartModel'),
      theModel       = new chartModel.MetricsModel(),
      dashboardView  = new DashboardView({model: theModel}),
      DashboardController = Marionette.Controller.extend({
        dashboardView: dashboardView,

        handleDefaultRoute: function(path, params) {
          var
            setter = _.bind(theModel.set, theModel),
            parser = _.bind(theModel.parse, theModel),
            setParsedData = _.compose(setter, parser);
          $.ajax({
            url: path + '?' + params,
            headers: {
              Accept: 'application/hal+json, application/json, */*; q=0.01'
            }
          }).then(setParsedData);
        }

      });

    return DashboardController;
  }
);
