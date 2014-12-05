require.config({

  paths: {
    //##Backbone & Marionette
    'backbone' : '../vendor/backbone/backbone',
    'marionette': '../vendor/marionette/lib/core/backbone.marionette',
    'backbone.wreqr' : '../vendor/backbone.wreqr/lib/backbone.wreqr',
    'backbone.babysitter' : '../vendor/backbone.babysitter/lib/backbone.babysitter',
    'cocktail' : '../vendor/cocktail/Cocktail',
    //##JQuery
    'jquery' : '../vendor/jquery/dist/jquery',
    'jquery-resize': 'https://rawgit.com/cowboy/jquery-resize/master/jquery.ba-resize',
    'moment' : '../vendor/moment/moment',
    'traverson' : '../vendor/traverson/browser/dist/traverson',
    'hal-model' : 'https://rawgit.com/mikekelly/backbone.hal/master/backbone.hal',
    'modernizr' : '../vendor/modernizr/modernizr',
    //##Handlebars
    'handlebars': '../vendor/handlebars/handlebars',
    //##D3
    'd3' : '../vendor/d3/d3',
    //##Text & Json loader plugins
    'text' : 'https://rawgit.com/requirejs/text/master/text',
    'json' : 'https://rawgit.com/millermedeiros/requirejs-plugins/master/src/json',
    //##Paths
    'modules' : 'app/modules',
    'widgets' : 'app/modules/widgets',
    'entities' : 'app/entities'
  },

  //##Shims
  shim: {

    'underscore': {
      exports: '_'
    },

    'jquery-resize': {
      deps: ['jquery']
    },

    'backbone': {
      deps: ['jquery', 'jquery-resize', 'underscore'],
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
      //location: '../vendor/lodash/modules',
      //main: 'main'
      location: '../vendor/lodash/dist',
      main: 'lodash.min.js'
    }
  ],

  waitSeconds: 30
});
