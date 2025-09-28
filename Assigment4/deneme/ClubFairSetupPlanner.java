import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class ClubFairSetupPlanner implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            int [] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename)  {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HERE
        try{


        File xmlFile = new File(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("Project");

        for (int i = 0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            Element projectElement = (Element) node;

            String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
            List<Task> task_list = new ArrayList<>();

            NodeList taskNodes = projectElement.getElementsByTagName("Task");

            for (int j = 0; j < taskNodes.getLength(); j++) {
                Element taskElement = (Element) taskNodes.item(j);

                int taskID = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());

                List<Integer> dependencies = new ArrayList<>();
                NodeList dependsNodes = taskElement.getElementsByTagName("DependsOnTaskID");

                for (int k = 0; k < dependsNodes.getLength(); k++) {
                    String dep = dependsNodes.item(k).getTextContent();
                    dependencies.add(Integer.parseInt(dep));

                }

                Task task = new Task(taskID, description, duration, dependencies);
                task_list.add(task);
            }

            Project project = new Project(projectName, task_list);
            projectList.add(project);
        }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (Exception e) {
            System.out.println("Error reading XML file: " + e.getMessage());
        }
        return projectList;
    }
    public void printProjectsForControl(List<Project> projectList) {
        for (Project project : projectList) {
            System.out.println("Project Name: " +project.getName());

            for (Task task : project.getTasks()) {
                System.out.println("  Task ID: " +task.getTaskID());
                System.out.println("    Description: " +task.getDescription());
                System.out.println("    Duration: " +task.getDuration());
                System.out.println("    Dependencies: " +task.getDependencies());
                System.out.println();
            }
            System.out.println("--------------------------------------");
        }
    }


}
