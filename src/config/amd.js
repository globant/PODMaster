require.config({

  paths: {
    'backbone' : '../vendor/backbone/backbone',
    'marionette': '../vendor/marionette/lib/core/backbone.marionette',
    'backbone.wreqr' : '../vendor/backbone.wreqr/lib/backbone.wreqr',
    'backbone.babysitter' : '../vendor/backbone.babysitter/lib/backbone.babysitter',
    'jquery' : '../vendor/jquery/dist/jquery',
    'underscore' : '../vendor/lodash/dist/lodash.underscore',
    'modernizr' : '../vendor/modernizr/modernizr',
    'hbs': '../vendor/require-handlebars-plugin/hbs',
    'handlebars': '../vendor/require-handlebars-plugin/Handlebars',
    'i18nprecompile': '../vendor/require-handlebars-plugin/hbs/i18nprecompile',
    'json2': '../vendor/require-handlebars-plugin/hbs/json2',
    'd3' : '../vendor/d3/d3',
    'text' : 'https://rawgit.com/requirejs/text/master/text',
    'json' : 'https://rawgit.com/millermedeiros/requirejs-plugins/master/src/json',
    'widgets' : 'app/widgets'
  },
  'hbs': {
    // to allow cross-domain requests
    useXhr: function () {
      'use strict';
      return true;
    },
    i18n: false,
    templateExtension: 'hbs',
    disableI18n: true,
    i18nDirectory: 'app/i18n'
  },

  shim: {

    'underscore': {
      exports: '_'
    },
    'backbone': {
      deps: ['jquery','underscore'],
      exports: 'Backbone'
    },

    'modernizr': {
      exports: 'Modernizr'
    },


    'hbs': {
      deps: ['underscore', 'handlebars']
    },
    /*
    'handlebars': {
      exports: 'Handlebars'
    },
   */

  },

  waitSeconds: 30
});
