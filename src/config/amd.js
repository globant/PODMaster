require.config({

  paths: {
    //##Backbone & Marionette
    'backbone' : '../vendor/backbone/backbone',
    'marionette': '../vendor/marionette/lib/core/backbone.marionette',
    'backbone.wreqr' : '../vendor/backbone.wreqr/lib/backbone.wreqr',
    'backbone.babysitter' : '../vendor/backbone.babysitter/lib/backbone.babysitter',
    //##JQuery
    'jquery' : '../vendor/jquery/dist/jquery',
    'modernizr' : '../vendor/modernizr/modernizr',
    //##Handlebars
    'handlebars': '../vendor/handlebars/handlebars',
    //##D3
    'd3' : '../vendor/d3/d3',
    //##Text & Json loader plugins
    'text' : 'https://rawgit.com/requirejs/text/master/text',
    'json' : 'https://rawgit.com/millermedeiros/requirejs-plugins/master/src/json',
    //##Paths
    'widgets' : 'app/widgets'
  },

  //##Shims
  shim: {

    'underscore': {
      exports: '_'
    },

    'backbone': {
      deps: ['jquery', 'underscore'],
      exports: 'Backbone'
    },

    'modernizr': {
      exports: 'Modernizr'
    },

    'handlebars': {
      exports: 'Handlebars'
    }

  },

  packages:[
    {
      name: 'hbs',
      location: '../vendor/requirejs-hbs',
      main: 'hbs'
    },
    {
      name: 'underscore',
      location: '../vendor/lodash/modules',
      main: 'main'
    }
  ],

  waitSeconds: 30
});
