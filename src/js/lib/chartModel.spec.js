/*global describe,it*/
//jshint -W030
//jshint devel:true
define(function(require) {
  'use strict';
  var
    sampleData  = require('json!modules/dashboard/ejemplo.json'),
    chartModel  = require('lib/chartModel'),
    MetricsModel = chartModel.MetricsModel;

  describe('MetricsModel', function() {

    describe('Base consistency', function() {
      var
        metrics = new MetricsModel();
      it('An empty model should be valid', function(done) {
        var
          aMetric;
        metrics.should.be.an.instanceOf(MetricsModel);
        aMetric = metrics.get('velocity');
        aMetric.should.be.an.instanceOf(chartModel.MetricModel);
        done();
      });
      it('Simple aggregation should be ok', function(done) {
        var
          aMetric, aSeries;
        metrics.set(metrics.parse(sampleData));
        metrics.should.be.an.instanceOf(MetricsModel);
        aMetric = metrics.get('velocity');
        aMetric.should.be.an.instanceOf(chartModel.MetricModel);
        aSeries = aMetric.get('series');
        aSeries.should.be.an.instanceOf(chartModel.SeriesCollection);
        done();
      });
    });
  });

});
