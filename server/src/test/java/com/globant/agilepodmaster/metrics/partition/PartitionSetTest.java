package com.globant.agilepodmaster.metrics.partition;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.YearQuarter;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test sorting of PartitionSet.
 * @author jose.dominguez@globant.com
 *
 */
public class PartitionSetTest {

  /**
   * Test sorting ProjectPod.
   */
  @Test
  public void testShouldSortProjectPod() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<String> partPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partPod2 = new Partition<String>("pod", "pod2");
    Partition<String> partPod3 = new Partition<String>("pod", "pod3");

    PartitionSet ps1 = PartitionSet.build(partProject1, partPod3);
    PartitionSet ps2 = PartitionSet.build(partPod2, partProject1);
    PartitionSet ps3 = PartitionSet.build(partPod3, partProject2);
    PartitionSet ps4 = PartitionSet.build(partProject2, partPod2);
    PartitionSet ps5 = PartitionSet.build(partProject2, partPod1);
    PartitionSet ps6 = PartitionSet.build(partProject1, partPod1);

    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6);
    Collections.sort(list);

    assertThat(list, contains(ps6, ps2, ps1, ps5, ps4, ps3));

  }
  
  /**
   * Test sorting Project Pod Quarter.
   */
  @Test
  public void testShouldSortProjectPodQuarter() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<String> partPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partPod2 = new Partition<String>("pod", "pod2");

    Partition<Quarter> partQ1 = new Partition<Quarter>("quarter", Quarter.Q1);
    Partition<Quarter> partQ2 = new Partition<Quarter>("quarter", Quarter.Q2);

    PartitionSet ps1 = PartitionSet.build(partPod2, partProject1, partQ2);
    PartitionSet ps2 = PartitionSet.build(partProject2, partPod2, partQ2);
    PartitionSet ps3 = PartitionSet.build(partProject2, partPod1, partQ2);
    PartitionSet ps4 = PartitionSet.build(partProject1, partPod1, partQ2);

    PartitionSet ps5 = PartitionSet.build(partPod2, partProject1, partQ1);
    PartitionSet ps6 = PartitionSet.build(partProject2, partPod2, partQ1);
    PartitionSet ps7 = PartitionSet.build(partProject2, partPod1, partQ1);
    PartitionSet ps8 = PartitionSet.build(partProject1, partPod1, partQ1);

    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6, ps7,
        ps8);
    Collections.sort(list);

    assertThat(list, contains(ps8, ps4, ps5, ps1, ps7, ps3, ps6, ps2));

  }
  
  /**
   * Test sorting by Project Pod Sprint.
   */
  @Test
  public void testShouldSortProjectPodSprint() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<String> partPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partPod2 = new Partition<String>("pod", "pod2");
    
    Partition<Integer> partSprint1 = new Partition<Integer>("sprint", 1);
    Partition<Integer> partSprint2 = new Partition<Integer>("sprint", 2);

    PartitionSet ps1 = PartitionSet.build(partPod2, partProject1, partSprint2);
    PartitionSet ps2 = PartitionSet.build(partProject2, partPod2, partSprint2);
    PartitionSet ps3 = PartitionSet.build(partProject2, partPod1, partSprint2);
    PartitionSet ps4 = PartitionSet.build(partProject1, partPod1, partSprint2);

    PartitionSet ps5 = PartitionSet.build(partPod2, partProject1, partSprint1);
    PartitionSet ps6 = PartitionSet.build(partProject2, partPod2, partSprint1);
    PartitionSet ps7 = PartitionSet.build(partProject2, partPod1, partSprint1);
    PartitionSet ps8 = PartitionSet.build(partProject1, partPod1, partSprint1);

    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6, ps7,
        ps8);
    Collections.sort(list);

    assertThat(list, contains(ps8, ps4, ps5, ps1, ps7, ps3, ps6, ps2));

  }
  
  /**
   * Test sorting by project.
   */
  @Test
  public void testShouldSortByProject() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");
    Partition<String> partProject3 = new Partition<String>("project", "project3");

    PartitionSet ps2 = PartitionSet.build(partProject2);
    PartitionSet ps1 = PartitionSet.build(partProject1);
    PartitionSet ps3 = PartitionSet.build(partProject3);

    List<PartitionSet> list = Arrays.asList(ps2, ps1, ps3);
    Collections.sort(list);

    assertThat(list, contains(ps1, ps2, ps3));

  }
  
  /**
   * Test sorting by Pod Quarter.
   */
  @Test
  public void testShouldSortByPodQuarter() {

    Partition<Quarter> partitionQ1 = new Partition<Quarter>("quarter",
        Quarter.Q1);
    Partition<Quarter> partitionQ2 = new Partition<Quarter>("quarter",
        Quarter.Q2);
    Partition<Quarter> partitionQ3 = new Partition<Quarter>("quarter",
        Quarter.Q3);
    Partition<Quarter> partitionQ4 = new Partition<Quarter>("quarter",
        Quarter.Q4);

    Partition<String> partitionPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod", "pod2");

    PartitionSet ps1 = PartitionSet.build(partitionQ1, partitionPod1);
    PartitionSet ps2 = PartitionSet.build(partitionQ2, partitionPod2);
    PartitionSet ps3 = PartitionSet.build(partitionQ3, partitionPod2);
    PartitionSet ps4 = PartitionSet.build(partitionQ4, partitionPod1); 
    PartitionSet ps5 = PartitionSet.build(partitionQ1, partitionPod2);
    PartitionSet ps6 = PartitionSet.build(partitionQ2, partitionPod1);
    PartitionSet ps7 = PartitionSet.build(partitionQ3, partitionPod1);
    PartitionSet ps8 = PartitionSet.build(partitionQ4, partitionPod2);  
    

    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6, ps7, ps8);
    Collections.sort(list);

    assertThat(list, contains(ps1, ps6, ps7, ps4, ps5, ps2, ps3, ps8));


  }
  
  /**
   * Test sorting by ProjectYear.
   */
  @Test
  public void testShouldSortByProjectYear() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<Integer> partYear1 = new Partition<Integer>("year", 2014);
    Partition<Integer> partYear2 = new Partition<Integer>("year", 2015);
    Partition<Integer> partYear3 = new Partition<Integer>("year", 2016);

    PartitionSet ps1 = PartitionSet.build(partYear1, partProject1);
    PartitionSet ps2 = PartitionSet.build(partProject2, partYear2);
    PartitionSet ps3 = PartitionSet.build(partProject2, partYear3);
    PartitionSet ps4 = PartitionSet.build(partProject1, partYear2);
    PartitionSet ps5 = PartitionSet.build(partYear3, partProject1);
    PartitionSet ps6 = PartitionSet.build(partProject2, partYear1);

    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6);
    Collections.sort(list);

    assertThat(list, contains(ps1, ps4, ps5, ps6, ps2, ps3));

  }
  
  /**
   * Test sorting by Project Year Month.
   */
  @Test
  public void testShouldSortByProjectYearMonth() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<Integer> partYear1 = new Partition<Integer>("year", 2014);
    Partition<Integer> partYear2 = new Partition<Integer>("year", 2015);
    
    Partition<Integer> partMonth1 = new Partition<Integer>("month", 1);
    Partition<Integer> partMonth2 = new Partition<Integer>("month", 2);


    PartitionSet ps1 = PartitionSet.build(partYear1, partProject1, partMonth2);
    PartitionSet ps2 = PartitionSet.build(partProject2, partYear2,partMonth2);
    PartitionSet ps3 = PartitionSet.build(partProject1, partYear2, partMonth2);
    PartitionSet ps4 = PartitionSet.build(partProject2, partYear1, partMonth2);

    PartitionSet ps5 = PartitionSet.build(partYear1, partProject1, partMonth1);
    PartitionSet ps6 = PartitionSet.build(partProject2, partYear2, partMonth1);
    PartitionSet ps7 = PartitionSet.build(partProject1, partYear2, partMonth1);
    PartitionSet ps8 = PartitionSet.build(partProject2, partYear1, partMonth1);  
    
    List<PartitionSet> list = Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6, ps7, ps8);
    Collections.sort(list);

    assertThat(list, contains(ps5, ps1, ps7, ps3, ps8, ps4, ps6, ps2));

  }
  
  /**
   * Test sorting by Project Year Quarter.
   */
  @Test
  public void testShouldSortByProjectYearQuarter() {

    Partition<String> partProject1 = new Partition<String>("project", "project1");
    Partition<String> partProject2 = new Partition<String>("project", "project2");

    Partition<YearQuarter> partYearQuarter1 = new Partition<YearQuarter>(
        "year/quarter", new YearQuarter(2014, Quarter.Q1));
    Partition<YearQuarter> partYearQuarter2 = new Partition<YearQuarter>(
        "year/quarter", new YearQuarter(2014, Quarter.Q2));
    Partition<YearQuarter> partYearQuarter3 = new Partition<YearQuarter>(
        "year/quarter", new YearQuarter(2015, Quarter.Q1));
    Partition<YearQuarter> partYearQuarter4 = new Partition<YearQuarter>(
        "year/quarter", new YearQuarter(2015, Quarter.Q2));



    PartitionSet ps1 = PartitionSet.build(partProject1, partYearQuarter1);
    PartitionSet ps2 = PartitionSet.build(partProject1, partYearQuarter2);
    PartitionSet ps3 = PartitionSet.build(partProject1, partYearQuarter3);
    PartitionSet ps4 = PartitionSet.build(partProject1, partYearQuarter4);

    PartitionSet ps5 = PartitionSet.build(partProject2, partYearQuarter1);
    PartitionSet ps6 = PartitionSet.build(partProject2, partYearQuarter2);
    PartitionSet ps7 = PartitionSet.build(partProject2, partYearQuarter3);
    PartitionSet ps8 = PartitionSet.build(partProject2, partYearQuarter4);
    
    List<PartitionSet> list = Arrays.asList(ps2, ps8, ps3, ps1, ps5, ps6, ps7, ps4);
    Collections.sort(list);

    assertThat(list, contains(ps1, ps2, ps3, ps4, ps5, ps6, ps7, ps8));

  }

}

