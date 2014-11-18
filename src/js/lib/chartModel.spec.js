/*global describe,it*/
//jshint -W030
//jshint devel:true
define(function(require) {
  'use strict';
  var chartModel     = require('lib/chartModel');

  describe('CharModel', function() {

    describe('Base consistency', function() {
      it('Simple aggregation should be ok', function(done) {
        var
          theCollection = new chartModel.ChartSeries();
        theCollection.reset({
          aggregated: [
            {
              partitions:[
                {
                  key:'POD 1',
                  partition:'pod',
                },
                {
                  key:'2014/Q1',
                  partition:'year/quarter',
                }
              ],
              metrics:[
                {
                  name:'velocity',
                  value:'123',
                  unit:'story points'
                }
              ],
            }
          ]
        }, {parse: true});
        theCollection.models[0].should.be.ok;
        done();
      });
    });
  });

});
