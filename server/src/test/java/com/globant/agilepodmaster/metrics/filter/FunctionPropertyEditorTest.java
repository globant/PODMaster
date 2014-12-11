package com.globant.agilepodmaster.metrics.filter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test for custom FunctionExpressionPropertyEditor.
 * @author Jose Dominguez.
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    FunctionPropertyEditorTest.HappyPathsTest.class,
    FunctionPropertyEditorTest.ExceptionFlowsTest.class })
public class FunctionPropertyEditorTest {
  /**
   * Class to test the happy path.
   *
   */
  @RunWith(Parameterized.class)
  public static class HappyPathsTest {
    private String expression;
    private String expectedResult;

    /**
     * Constructor.
     * @param expression input expresion.
     * @param expectedResult the expected result.
     */
    public HappyPathsTest(String expression, String expectedResult) {
      this.expression = expression;
      this.expectedResult = expectedResult;
    }

    
    /**
     * Parameters definition.
     * 
     * @return parameters for the test.
     */
    @Parameters
    public static Collection<Object[]> metrics() {
      return Arrays.asList(new Object[][] {
          {"remaining-story-points", "remaining-story-points"},
          {"accuracy-of-estimations", "accuracy-of-estimations"},
          {"actual-percent-complete", "actual-percent-complete"}});
    }

    /**
     * Testing the happy path with different parameters.
     */
    @Test
    public void shouldSetAndGetAsText() {
      FunctionPropertyEditor editor = new FunctionPropertyEditor();
      editor.setAsText(expression);
      assertThat(editor.getAsText(), equalTo(expectedResult));
    }
  }

  /**
   * Testing exceptions.
   */
  @RunWith(Parameterized.class)
  public static class ExceptionFlowsTest {
    private String expression;
    private String expectedResult;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Constructor.
     * @param expression input expression.
     * @param expectedResult expected result.
     */
    public ExceptionFlowsTest(String expression, String expectedResult) {
      this.expression = expression;
      this.expectedResult = expectedResult;
    }

    /**
     * Parameters definition.
     * @return parameters for the test.
     */
    @Parameters
    public static Collection<Object[]> primeNumbers() {
      return Arrays.asList(new Object[][] {
          {"completelywrongname", "Invalid metric name: completelywrongname"}});
    }

    /**
     * Testing exceptions with different parameters.
     */
    @Test
    public void shouldThrowIllegalArgumentException() {
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage(expectedResult);
      
      FunctionPropertyEditor editor = new FunctionPropertyEditor();
      editor.setAsText(expression);
    }
  }
}
