package com.globant.agilepodmaster.metrics.filter;

import java.beans.PropertyEditorSupport;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

import com.globant.agilepodmaster.core.QSprintPodMetric;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.StringExpression;

@NoArgsConstructor
public class BooleanExpressionPropertyEditor extends PropertyEditorSupport {
  private static final String REGEX = "(?<operand1>.+?)(?<operator>==|=|!=|<>)(?<operand2>.+)";
  private static final Pattern PATTERN = Pattern.compile(REGEX);

  public BooleanExpressionPropertyEditor(BooleanExpression value) {
    super(value);
  }

  @Override
  public String getAsText() {
    Predicate value = (Predicate) super.getValue();

    // remove the class, just care about the properties
    String toString = value.toString();
    if (toString.startsWith("sprintPodMetric.")) {
      toString = toString.substring("sprintPodMetric.".length());
    }

    // remove spaces
    String[] split = toString.split(" ");
    toString = String.join("", split);

    return toString;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    Matcher matcher = PATTERN.matcher(text);
    
    if (!matcher.find()) {
      throw new IllegalArgumentException("Cannot parse: " + text);
    }

    String operand1 = matcher.group("operand1");
    ComparableExpression<?> operand1Exp = parseOperand1(operand1);

    String operator = matcher.group("operator");
    Function<Object, BooleanExpression> operatorFunc = parseOperator(operator, operand1Exp);

    String operand2 = matcher.group("operand2");
    BooleanExpression predicate = operatorFunc.apply(parseOperand2(operand2, operand1Exp));

    super.setValue(predicate);
  }

  // TODO: This could be made nicer and more generic
  private ComparableExpression<?> parseOperand1(String operand1) {
    QSprintPodMetric sprintpodmetric = QSprintPodMetric.sprintPodMetric;

    ComparableExpression<?> operand1Exp = null;

    if ("pod.name".equalsIgnoreCase(operand1)) {
      operand1Exp = sprintpodmetric.pod.name;
    } else if ("sprint.name".equalsIgnoreCase(operand1)) {
      operand1Exp = sprintpodmetric.sprint.name;
    } else {
      throw new IllegalArgumentException("Cannot parse operand: " + operand1);
    }

    return operand1Exp;
  }

  @SuppressWarnings("unchecked")
  private Function<Object, BooleanExpression> parseOperator(String operator,
      @SuppressWarnings("rawtypes") ComparableExpression operand1Exp) {
    Function<Object, BooleanExpression> operatorFunc;
    if (operator.matches("=|==")) {
      operatorFunc = operand1Exp::eq;
    } else if (operator.matches("!=|<>")) {
      operatorFunc = operand1Exp::ne;
    } else {
      throw new IllegalArgumentException("Cannot parse operator: " + operator);
    }

    return operatorFunc;
  }
  
  private Object parseOperand2(String operand2, ComparableExpression<?> operand1Exp) {
    if (operand1Exp instanceof StringExpression) {
      return operand2;
    } else {
      throw new IllegalArgumentException("Cannot parse operand: " + operand2);
    }
  }
}