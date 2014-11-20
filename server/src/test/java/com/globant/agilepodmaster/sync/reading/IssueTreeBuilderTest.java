package com.globant.agilepodmaster.sync.reading;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.sync.reading.IssueTreeBuilder.IssueNode;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;
import com.globant.agilepodmaster.sync.reading.jira.responses.Issue.Fields;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Test of IssueTreeBuilder class.
 * @author jose.dominguez@globant.com
 *
 */
public class IssueTreeBuilderTest extends AbstractUnitTest {

  @Autowired
  IssueTreeBuilder issueTreeBuilder;


  /**
   * Test of buildTree function.
   */
  @Test
  public void testBuildTree() {

    Issue issueA = new Issue();
    issueA.setId("issue_A");
    issueA.setFields(new Fields());
    issueA.getFields().setParent(null);

    Issue issueB = new Issue();
    issueB.setId("issue_B");
    issueB.setFields(new Fields());
    issueB.getFields().setParent(issueA);

    Issue issueC = new Issue();
    issueC.setId("issue_C");
    issueC.setFields(new Fields());
    issueC.getFields().setParent(issueA);

    Issue issueD = new Issue();
    issueD.setId("issue_D");
    issueD.setFields(new Fields());
    issueD.getFields().setParent(issueA);

    Issue issueF = new Issue();
    issueF.setId("issue_F");
    issueF.setFields(new Fields());
    issueF.getFields().setParent(issueB);

    Issue issueG = new Issue();
    issueG.setId("issue_G");
    issueG.setFields(new Fields());
    issueG.getFields().setParent(issueB);

    Issue issueH = new Issue();
    issueH.setId("issue_H");
    issueH.setFields(new Fields());
    issueH.getFields().setParent(null);

    Issue issueJ = new Issue();
    issueJ.setId("issue_J");
    issueJ.setFields(new Fields());
    issueJ.getFields().setParent(issueH);

    Issue issueI = new Issue();
    issueI.setId("issue_I");
    issueI.setFields(new Fields());
    issueI.getFields().setParent(issueH);

    List<Issue> issues = Arrays.asList(issueA, issueB, issueC, issueD,
        issueF, issueG, issueH, issueI, issueJ);

    List<IssueNode> roots = issueTreeBuilder.buildTree(issues);

    assertThat(roots, hasSize(2));
    assertThat(roots.get(0).getIssue(), equalTo(issueH));
    assertThat(roots.get(1).getIssue(), equalTo(issueA));

    assertThat(roots.get(1).getSubIssues(), hasSize(3));
    assertThat(roots.get(1).getSubIssues().get(0).getIssue(), equalTo(issueB));
    assertThat(roots.get(1).getSubIssues().get(1).getIssue(), equalTo(issueD));
    assertThat(roots.get(1).getSubIssues().get(2).getIssue(), equalTo(issueC));

    IssueNode nodeB = roots.get(1).getSubIssues().get(0);
    assertThat(nodeB.getSubIssues(), hasSize(2));
    assertThat(nodeB.getSubIssues().get(0).getIssue(), equalTo(issueF));
    assertThat(nodeB.getSubIssues().get(1).getIssue(), equalTo(issueG));

    assertThat(roots.get(0).getSubIssues(), hasSize(2));
    assertThat(roots.get(0).getSubIssues().get(0).getIssue(), equalTo(issueI));
    assertThat(roots.get(0).getSubIssues().get(1).getIssue(), equalTo(issueJ));

  }

}
