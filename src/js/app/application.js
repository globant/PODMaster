define(
  [
    'backbone',
    'marionette',
    './router',
    './modules/dashboard/dashboard.controller'
  ],
  function(Backbone, Marionette, Router, Controller ) {
    'use strict';

    var Application = new Marionette.Application();

    Application.on('start', function() {
      Application.controller = new Controller();
      Application.router = new Router({controller: Application.controller});
      Backbone.history.start();
    });

    return Application;
  }
);
