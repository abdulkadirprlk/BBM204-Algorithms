import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        // TODO: Your code goes here
        // You are expected to read the file given as the first command-line argument to read 
        // the energy demands arriving per hour. Then, use this data to instantiate a 
        // PowerGridOptimization object. You need to call getOptimalPowerGridSolutionDP() method
        // of your PowerGridOptimization object to get the solution, and finally print it to STDOUT.

        String fileName1 = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(fileName1));
        String line = reader.readLine();
        ArrayList<Integer> demandsPerHour = new ArrayList<>();
        for(String demand : line.split(" ")){
            demandsPerHour.add(Integer.parseInt(demand));
        }

        // Initialization of PowerGridOptimization object
        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(demandsPerHour);
        int maxGW = powerGridOptimization.getOptimalPowerGridSolutionDP().getmaxNumberOfSatisfiedDemands();
        ArrayList<Integer> optimalHours = powerGridOptimization.getOptimalPowerGridSolutionDP().getHoursToDischargeBatteriesForMaxEfficiency();

        //OUTPUTS
        System.out.println("The total number of demanded gigawatts: " + sum(demandsPerHour));
        System.out.println("Maximum number of satisfied gigawatts: " + maxGW);
        System.out.print("Hours at which the battery bank should be discharged: ");
        for (int i = 0; i < optimalHours.size(); i++) {
            System.out.print(optimalHours.get(i));
            if (i < optimalHours.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("The number of unsatisfied gigawatts: " + (sum(demandsPerHour) - maxGW));
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        // TODO: Your code goes here
        // You are expected to read the file given as the second command-line argument to read
        // the number of available ESVs, the capacity of each available ESV, and the energy requirements 
        // of the maintenance tasks. Then, use this data to instantiate an OptimalESVDeploymentGP object.
        // You need to call getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) method
        // of your OptimalESVDeploymentGP object to get the solution, and finally print it to STDOUT.

        String fileName2 = args[1];
        BufferedReader reader1 = new BufferedReader(new FileReader(fileName2));
        int esvCount = 0;
        int maxCapacity = 0;
        String[] line2Array = reader1.readLine().split(" ");
        esvCount = Integer.parseInt(line2Array[0]);
        maxCapacity = Integer.parseInt(line2Array[1]);
        ArrayList<Integer> energyDemands = new ArrayList<>();
        String[] line3Array = reader1.readLine().split(" ");
        for(String i : line3Array){
            energyDemands.add(Integer.parseInt(i));
        }

        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(energyDemands);
        int minESVs = optimalESVDeploymentGP.getMinNumESVsToDeploy(esvCount, maxCapacity);
        ArrayList<ArrayList<Integer>> esvTasks = optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs();

        //OUTPUTS
        System.out.println("The minimum number of ESVs to deploy: " + minESVs);
        for (int i = 0; i < esvTasks.size(); i++) {
            System.out.println("ESV " + (i + 1) + " tasks: " + Arrays.toString(esvTasks.get(i).toArray()));
        }

        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
    private static int sum(ArrayList<Integer> arr) {
        int sum = 0;
        for (int i = 0; i < arr.size(); i++)
            sum += arr.get(i);

        return sum;
    }
}
