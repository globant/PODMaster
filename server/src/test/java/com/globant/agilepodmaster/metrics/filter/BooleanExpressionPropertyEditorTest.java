package com.globant.agilepodmaster.metrics.filter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;

/**
 * Test for custom BooleanExpressionPropertyEditor.
 * @author Andres Postiglioni.
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    BooleanExpressionPropertyEditorTest.HappyPathsTest.class,
    BooleanExpressionPropertyEditorTest.ExceptionFlowsTest.class })
public class BooleanExpressionPropertyEditorTest {
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
     * @return parameters for the test.
     */
    @Parameters
    public static Collection<Object[]> primeNumbers() {
      return Arrays.asList(new Object[][] {
          // Pod filters
          { "pod.name=something", "pod.name=something" },
          { "pod.name==something", "pod.name=something" },
          { "pod.name<>something", "pod.name!=something" },
          { "pod.name!=something", "pod.name!=something" },
          // Sprint filters
          { "sprint.name=something", "sprint.name=something" },
          { "sprint.name==something", "sprint.name=something" },
          { "sprint.name<>something", "sprint.name!=something" },
          { "sprint.name!=something", "sprint.name!=something" },
      });
    }


    /**
     * Testing the happy path with different parameters.
     */
    @Test
    public void shouldSetAndGetAsText() {
      BooleanExpressionPropertyEditor editor = new BooleanExpressionPropertyEditor();
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
          // IllegalArgumentInputs 
          { "completelywrongfilter", "Cannot parse: completelywrongfilter" },
          { "somethingUnknown!=something", "Cannot parse operand: somethingUnknown" },
          { "sprint.name*something", "Cannot parse: sprint.name*something" }, });
    }

    /**
     * Testing exceptions with different parameters.
     */
    @Test
    public void shouldThrowIllegalArgumentException() {
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage(expectedResult);
      
      BooleanExpressionPropertyEditor editor = new BooleanExpressionPropertyEditor();
      editor.setAsText(expression);
    }
  }
}
