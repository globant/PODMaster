/* global requirejs */
requirejs(['require','config/amd.js'],function(require){
  'use strict';
  require(
    [
      'app/application'
    ],
    function(App) {
      App.start();
    }
  );
});
