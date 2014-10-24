requirejs(['require','config/amd.js'],function(require){
  require(
    [
      'app/router'
    ],
    function(Router) {
      'use strict';

      new Router();
    }
  );
});
