/***
 * Application entry point.
 */
/*global mocha,chai*/
require(['../../config/amd'], function() {
  'use strict';
  require.config({
    baseUrl: '../../js',
    packages:[
      {
        name: 'underscore',
        location: '../vendor/lodash/',
        main: 'custom'
      }
    ],

  });

  chai.should();
  mocha.setup('bdd');

  require(
    [
      'lib/chartModel.spec'
    ], function() {
      mocha.globals(['jQuery']);
      mocha.run();
    }
  );
}
);
