define(
  [
    'backbone'
  ],
  function(Backbone) {
    'use strict';

    var DashboardModel = Backbone.Model.extend({

      url: function() {
      },

      parse: function(response) {
        return response.value;
      },

      fetch: function(options) {
        options = options || {};
        options.dataType = options.dataType || 'jsonp';
        return Backbone.Model.prototype.fetch.apply(this, [options]);
      }

    });

    return DashboardModel;

  }
);