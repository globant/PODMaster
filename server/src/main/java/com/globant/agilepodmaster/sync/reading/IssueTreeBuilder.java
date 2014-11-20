package com.globant.agilepodmaster.sync.reading;

import com.globant.agilepodmaster.sync.reading.jira.responses.Issue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * Class to build a tree for being used to provided nested task to the snapshot builder.
 * @author jose.dominguez@globant.com
 *
 */
@Data
@Log
@Service
public class IssueTreeBuilder {

  /**
   * Builds the tree.
   * 
   * @param issues issues that will be part of the tree.
   * @return a list of roots.
   */
  public List<IssueNode> buildTree(List<Issue> issues) {

    Map<Issue, IssueNode> mappedPairs = new HashMap<Issue, IssueNode>();

    for (Issue issue : issues) {
      IssueNode issueNode = new IssueNode(issue, new ArrayList<IssueNode>());
      mappedPairs.put(issue, issueNode);
    }

    addSubTasksToMap(mappedPairs);
    return getRoots(mappedPairs);

  }

  private void addSubTasksToMap(Map<Issue, IssueNode> mappedPairs) {

    for (Map.Entry<Issue, IssueNode> pair : mappedPairs.entrySet()) {
      if (pair.getKey().getFields().getParent() != null) {

        String parentId = pair.getKey().getFields().getParent().getId();

        Issue parentIssue = mappedPairs.keySet().stream()
            .filter(u -> u.getId().equals(parentId)).findFirst().get();

        if (parentIssue != null) {
          mappedPairs.get(parentIssue).getSubIssues().add(pair.getValue());
        } else {
          log.warning("Parent item " + parentIssue
              + " can't be found in result");
        }
      }
    }
  }

  private List<IssueNode> getRoots(Map<Issue, IssueNode> mappedPairs) {

    List<IssueNode> taskRootsDTOs = new ArrayList<IssueNode>();
    for (Map.Entry<Issue, IssueNode> pair : mappedPairs.entrySet()) {
      if (pair.getKey().getFields().getParent() == null) {
        taskRootsDTOs.add(pair.getValue());
      }
    }
    return taskRootsDTOs;
  }

  @Data
  @AllArgsConstructor
  public class IssueNode {

    private Issue issue;

    private List<IssueNode> subIssues;
  }



}
