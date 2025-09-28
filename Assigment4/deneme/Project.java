import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;
        int[] schedule = getEarliestSchedule();
        for (int i=0; i< schedule.length; i++){
            int end= schedule[i]+tasks.get(i).getDuration();
            projectDuration= Math.max(projectDuration,end);
        }
        // TODO: YOUR CODE HERE

        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        int[] schedule = new int[tasks.size()];
        Stack<Integer> order = topological_order();

        while (!order.isEmpty()) {
            int task_id = order.pop();
            Task task = tasks.get(task_id);
            int max = 0;
            for (int dep : task.getDependencies()) {
                int end = schedule[dep] + tasks.get(dep).getDuration();
                max = Math.max(max, end);
            }
            schedule[task_id] = max;
        }

        return schedule;
    }
    public void printSchedulee(int [] schedule){
        for (int i = 0; i < schedule.length; i++) {
            System.out.println(schedule[i]+" ");        }
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Stack<Integer> topological_order(){
        boolean[] marked;
         Stack<Integer> reversePost=new Stack<>();
        marked = new boolean[tasks.size()];
        for (int i= 0; i < tasks.size(); i++){
            if (!marked[i]) dfs(marked, i,reversePost);
    }
        return reversePost;


    }
    public void dfs(boolean[] marked, int task_id, Stack<Integer> reversePost){
        marked[task_id] = true;

        for (Task task : tasks) {
            if (task.getDependencies().contains(task_id)) {
                if (!marked[task.getTaskID()]) {
                    dfs(marked, task.getTaskID(), reversePost);
                }
            }
        }

        reversePost.push(task_id);
    }


}
