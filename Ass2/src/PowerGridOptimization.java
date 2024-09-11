import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        // TODO: YOUR CODE HERE

        Integer[] solutions = new Integer[amountOfEnergyDemandsArrivingPerHour.size() + 1];
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>();

        solutions[0] = 0;
        ArrayList<Integer> array1 = new ArrayList<>();
        hours.add(array1);

        for(int j = 1; j < solutions.length; j++){
            ArrayList<Integer> new_element = new ArrayList<>();
            int max = 0;
            int max_i = 0;
            for(int i = 0; i < j; i++){
                int current_solution = (int) (solutions[i] + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), E(j - i)));
                if(current_solution > max){
                    max = current_solution;
                    max_i = i;
                }
            }
            solutions[j] = max;
            if(max_i > 0) new_element.add(max_i);
            new_element.add(j);
            hours.add(new_element);
        }
        int max = 0;
        for(int i = 0; i < solutions.length; i++){
            int j = solutions.length - 1;
            int current_solution = (int) (solutions[i] + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), E(j - i)));
            if(current_solution > max){
                max = current_solution;
            }
        }

        return new OptimalPowerGridSolution(max, hours.get(hours.size() - 1));
    }
    private double E(int i){
        return Math.pow(i, 2);
    }
}
