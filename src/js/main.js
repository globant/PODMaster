require.config({

  paths: {
    'backbone' : '../vendor/backbone/backbone',
    'marionette': '../vendor/marionette/lib/core/backbone.marionette',
    'backbone.wreqr' : '../vendor/backbone.wreqr/lib/backbone.wreqr',
    'backbone.babysitter' : '../vendor/backbone.babysitter/lib/backbone.babysitter',
    'jquery' : '../vendor/jquery/dist/jquery',
    'underscore' : '../vendor/lodash/dist/lodash',
    'modernizr' : '../vendor/modernizr/modernizr',
    'handlebars' : '../vendor/handlebars/handlebars',
    'templateregistry' : 'app/templates'
  },

  shim: {
    'backbone': {
      deps: ['underscore', 'jquery'],
      exports: 'Backbone'
    },

    'modernizr': {
      exports: 'Modernizr'
    },

    'handlebars': {
      exports: 'Handlebars'
    }
  },

  waitSeconds: 30
});

require(
  [
    'app/router'
  ],
  function(Router) {
    'use strict';

    new Router();
  }
);
