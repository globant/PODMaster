import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

class JacocoCoverageCheck extends DefaultTask {
  Double lineThreshold = 0.78
  Double branchThreshold = 0.11
  Double instructionThreshold = 0.39
  def reportTask
  
  def setReportTask(task) {
    if (task != null) {
      this.reportTask = task
      this.dependsOn reportTask
    }
  }

  @TaskAction
  def check() {
    def reportFile = reportTask.outputs.files.filter{it.name == (reportTask.name + '.xml')}.singleFile
    JacocoXMLReport report = new JacocoXMLReport(reportFile)
    
    assert report.coverage('LINE') >= lineThreshold
    assert report.coverage('BRANCH') >= branchThreshold
    assert report.coverage('INSTRUCTION') >= instructionThreshold
  }

  class JacocoXMLReport {
    def records

    JacocoXMLReport(reportFile) {
      def parser = new XmlSlurper()
      parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
      parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      parser.setFeature("http://xml.org/sax/features/namespaces", false)

      def xml = reportFile.text
      records = parser.parseText(xml)
    }
    
    def missed = { counter -> counter.@missed.text().toInteger() }
    def covered = { counter -> counter.@covered.text().toInteger() }

    def coverage(name) {
      def counter = records.counter.find{ it.@type==name }
      covered(counter) / (missed(counter) + covered(counter)) 
    }
  }
}