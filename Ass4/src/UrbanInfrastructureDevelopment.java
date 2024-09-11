import java.io.Serializable;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.File;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HERE

        try{
            // specify the file path as a File object
            File xmlFile = new File(filename);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file
            Document document = builder.parse(xmlFile);

            // Access elements by tag name
            NodeList projectNodes = document.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Node projectNode = projectNodes.item(i);
                if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element projectElement = (Element) projectNode;

                    String name = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                    NodeList taskNodes = projectElement.getElementsByTagName("Task");

                    List<Task> tasks = new ArrayList<>();
                    for (int j = 0; j < taskNodes.getLength(); j++) {
                        Node taskNode = taskNodes.item(j);
                        if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element taskElement = (Element) taskNode;

                            int taskID = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());

                            NodeList dependenciesNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                            List<Integer> dependencies = new ArrayList<>();
                            for (int k = 0; k < dependenciesNodes.getLength(); k++) {
                                dependencies.add(Integer.parseInt(dependenciesNodes.item(k).getTextContent()));
                            }
                            tasks.add(new Task(taskID, description, duration, dependencies));
                        }
                    }
                    projectList.add(new Project(name, tasks));
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return projectList;
    }
}
