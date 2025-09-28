import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class Main {
    public static void main(String[] args)  {
        try{

        // Set the default locale to English
        Locale locale = new Locale("en", "EN");
        Locale.setDefault(locale);

        System.out.println("### CLUB FAIR SETUP START ###");
        ClubFairSetupPlanner planner = new ClubFairSetupPlanner();
        List<Project> projectList = planner.readXML(args[0]);
        planner.printSchedule(projectList);
        //planner.printProjectsForControl(projectList);
        //printStack(projectList.get(0).topological_order(),projectList.get(0).getTasks());
        //projectList.get(0).printSchedule(projectList.get(0).getEarliestSchedule());
        //System.out.println(projectList.get(0).getProjectDuration());
        System.out.println("### CLUB FAIR SETUP END ###");

        System.out.println("### CAMPUS NAVIGATOR START ###");
        CampusNavigatorApp navigatorApp = new CampusNavigatorApp();
        CampusNavigatorNetwork network = navigatorApp.readCampusNavigatorNetwork(args[1]);

        List<RouteDirection> directions = navigatorApp.getFastestRouteDirections(network);
        navigatorApp.printRouteDirections(directions);
        System.out.println("### CAMPUS NAVIGATOR END ###");
        }
         catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void printStack(Stack<Integer> stack, List<Task> tasks) {
        System.out.println("Stack (top → bottom):");
        for (int i = 0; i < stack.size(); i++) {
            int taskId = stack.get(i);
            Task task = tasks.get(taskId);
            System.out.println("Task ID: " + task.getTaskID() +
                    ", Description: " + task.getDescription() +
                    ", Duration: " + task.getDuration() +
                    ", Dependencies: " + task.getDependencies());
        }

    }
}


