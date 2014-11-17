define(function(require) {
    'use strict';
    var
      _              = require('underscore'),
      Marionette     = require('marionette'),
      traverson      = require('traverson'),
      HalModel       = require('hal-model').Model,
      DashboardView  = require('modules/dashboard/dashboard.view'),
      chartModel     = require('lib/chartModel'),
      theModel       = new chartModel.MetricsModel(),
      dashboardView  = new DashboardView({model: theModel}),
      DashboardController = Marionette.Controller.extend({
        dashboardView: dashboardView,

        handleDefaultRoute: function(path, params) {
          var
            api = traverson.jsonHal.from(path + (('?' + params) || '')),
            setter = _.bind(theModel.set, theModel),
            parser = _.bind(theModel.parse, theModel),
            setParsedData = _.compose(setter, parser);
          api.newRequest()
            .follow()
            //.follow('podmaster:dashboards', 'podmaster:metrics')
            .withRequestOptions(
              {
                headers: {
                  'X-Requested-With':null,
                  'Accept': 'application/hal+json, application/json; q=0.01'
                }
              }
            )
            //.withTemplateParameters({aggregation: ['year/quarter', 'pod']})
            .getResource(function(error, document) {
              var hal;
              if (error) {
                return;
              }
              if (document['_links']) {
                hal = new HalModel(document);
                setParsedData(hal.toJSON());
              } else {
                setParsedData(document);
              }
            });
        }
      });

    return DashboardController;
  }
);
