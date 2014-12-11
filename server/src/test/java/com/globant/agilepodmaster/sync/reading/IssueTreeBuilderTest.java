package com.globant.agilepodmaster.sync.reading;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.readers.releases.IssueTreeBuilder;
import com.globant.agilepodmaster.readers.releases.IssueTreeBuilder.IssueNode;
import com.globant.agilepodmaster.readers.releases.jira.responses.Issue;
import com.globant.agilepodmaster.readers.releases.jira.responses.Issue.Fields;

import org.junit.Ignore;
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
  @Ignore
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

    assertTrue(hasIssue(roots.get(1).getSubIssues(), issueD) );
    assertTrue(hasIssue(roots.get(1).getSubIssues(), issueC) );
    assertTrue(hasIssue(roots.get(1).getSubIssues(), issueB) );

    IssueNode nodeB = getIssueNodeFrom(roots.get(1).getSubIssues(), issueB);
    assertThat(nodeB.getSubIssues(), hasSize(2));
    assertTrue(hasIssue(nodeB.getSubIssues(), issueG) );
    assertTrue(hasIssue(nodeB.getSubIssues(), issueF) );


    assertThat(roots.get(0).getSubIssues(), hasSize(2));
    assertTrue(hasIssue(roots.get(0).getSubIssues(), issueI) );
    assertTrue(hasIssue(roots.get(0).getSubIssues(), issueJ) );
    
  }
  
  private boolean hasIssue(List<IssueNode> list, Issue issue) {
    return getIssueNodeFrom(list,  issue) != null;
  }
  
  private IssueNode getIssueNodeFrom(List<IssueNode> list, Issue issue) {
    for (IssueNode issueNode: list) {
      if (issueNode.getIssue() == issue) {
        return issueNode;
      }
    }
    return null;
    
    
  }


}
