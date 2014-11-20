package com.globant.agilepodmaster.statistics;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
  private final SimpleRegressionCommand srCommand;
  private final NormalApproximationCommand nmCommand;

  @Autowired
  public StatisticsController(
      SimpleRegressionCommand srCommand, NormalApproximationCommand nmCommand) {
    this.srCommand = srCommand;
    this.nmCommand = nmCommand;
  }

  @RequestMapping("/prediction/regression/simple")
  public RegressionResult simpleRegression(
      @RequestParam("pair") Set<Pair<Number, Number>> 
        pairs,
      @RequestParam(value = "confidence", required = false, defaultValue = ".95") 
        double confidenceLevel,
      @RequestParam(value = "intercept", required = false, defaultValue = "true")
        boolean intercept,
      @RequestParam(value = "predict", required = false)
        List<Double> requiredPredictions,
      @RequestParam(value="precision", required=false)
        Integer precision) {
    
    return srCommand.regress(
        pairs.stream().collect(Collectors.toSet()),
        confidenceLevel, 
        intercept, 
        requiredPredictions,
        precision
    );
  }

  @RequestMapping("/approximation/normal")
  public ConfidenceInterval normalEstimation(
      @RequestParam("data") Set<Double> data, 
      @RequestParam(value = "confidence", required = false, defaultValue = ".95") 
        double confidenceLevel) {
    
    return nmCommand.estimate(data, confidenceLevel);
  }

  @InitBinder
  public void initBinderAll(WebDataBinder binder) {
    binder.registerCustomEditor(Pair.class, new PairPropertyEditor());
  }
}
