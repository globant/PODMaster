
//jshint unused:false
define(function(require) {
    'use strict';
    var
      $              = require('jquery'),
      //ejemplo        = require('json!modules/dashboard/ejemplo.json'),
      _              = require('underscore'),
      Backbone       = require('backbone'),
      Marionette     = require('marionette'),
      DashboardModel = require('entities/dashboard.model'),
      DashboardView  = require('modules/dashboard/dashboard.view'),
      AreaChart      = require('widgets/areaChart'),
      chartModel     = require('lib/chartModel'),

      moment         = require('moment'),

    DashboardController = Marionette.Controller.extend({

      handleDefaultRoute: function(path, params) {
        //jshint unused:false
        var
          theCollection = new chartModel.ChartSeries(),
          dashboardView = new DashboardView(),
          // Create chart object
          chart = new AreaChart({
            // View options
            el: this.el,
            collection: theCollection,
            xFormat: function(date) {
              return moment(date).format('YYYY/[Q]Q');
            }
          });
        $.ajax({
          url: path + '?' + params,
          headers: {
            Accept: 'application/hal+json, application/json, */*; q=0.01'
          }
        }).then(function(data) {
          theCollection.reset(data, {parse: true});
        });
        // Render the outer element
        dashboardView.render();
        dashboardView.content.show(chart);
      }

    });

    return DashboardController;
  }
);
