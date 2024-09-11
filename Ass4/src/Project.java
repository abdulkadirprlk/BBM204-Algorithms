import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;
    private int[] earliestSchedule;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName(){
        return name;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        if (earliestSchedule == null) {
            getEarliestSchedule();
        }

        int projectDuration = 0;

        // TODO: YOUR CODE HERE
        projectDuration = earliestSchedule[earliestSchedule.length - 1] + tasks.get(earliestSchedule.length - 1).getDuration();
        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        // TODO: YOUR CODE HERE

        int[] startTime = new int[tasks.size()];
        Arrays.fill(startTime, 0);

        // create adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            adj.add(new ArrayList<>());
        }

        // Initialize in-degree array and fill adjacency list
        int[] indegree = new int[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            for (int dependency : tasks.get(i).getDependencies()) {
                adj.get(dependency).add(i);
                indegree[i]++;
            }
        }

        // Add tasks with 0 in-degree to the queue
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> result = new ArrayList<>(); // list to store the topologically sorted task IDs
        while (!q.isEmpty()) {
            int node = q.poll();
            result.add(node);
            for (int dependent : adj.get(node)) {
                indegree[dependent]--;
                if (indegree[dependent] == 0) {
                    q.add(dependent);
                }
            }
        }

        // Check for cycles
        if (result.size() != tasks.size()) {
            System.out.println("Graph contains cycle!");
            return new int[0];
        }

        // Calculate the earliest start time for each task
        for (int task : result) {
            int maxEndTime = 0;
            for (int dependency : tasks.get(task).getDependencies()) {
                maxEndTime = Math.max(maxEndTime, startTime[dependency] + tasks.get(dependency).getDuration());
            }
            startTime[task] = maxEndTime;
        }

        earliestSchedule = startTime;
        return startTime;
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

}
