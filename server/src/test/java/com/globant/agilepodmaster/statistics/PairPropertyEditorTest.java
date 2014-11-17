package com.globant.agilepodmaster.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PairPropertyEditorTest {

  @Test
  public void test() {
    PairPropertyEditor editor = new PairPropertyEditor();
    String value = "(1.23,2.5)";
    editor.setAsText(value);
    String asText = editor.getAsText();
    assertThat(asText, equalTo(value));
  }
}
