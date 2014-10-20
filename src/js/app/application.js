define(
  [
    'marionette',
    'handlebars',
    './router',
    'templateregistry'
  ],
  function(Marionette, Handlebars, Router, JST) {
    'use strict';

    var Application = new Marionette.Application();

    Application.addInitializer(function(options) {
      new Router();
    });

    Application.on('before:start', function(options) {
      Marionette.Renderer.render = function(template, data) {
        if (!JST[template]) {
          throw "Template '" + template + "' not found!";
        }
        return JST[template](data);
      };
    });

    Application.on('start', function(options) {

      Application.addRegions({
        mainRegion: '.main'
      });
    });

    return Application;
  }
);