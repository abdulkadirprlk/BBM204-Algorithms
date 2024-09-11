import java.util.*;
import java.io.*;

public class Quiz3 {
    public static void main(String[] args) throws IOException {
        
        // TODO: Use the first command line argument (args[0]) as the file to read the input from
        // TODO: Your code goes here
        // TODO: Print the solution to STDOUT
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        int numberOfTestCases = Integer.parseInt(reader.readLine());

        // Test Cases Loop = 1, 2, 3, 4 ...
        for (int i = 0; i < numberOfTestCases; i++){
            String[] lineArray = reader.readLine().split(" ");
            int numberOfDrones = Integer.parseInt(lineArray[0]);
            int numberOfStations = Integer.parseInt(lineArray[1]);
            int[][] coordinates = new int[numberOfStations][2];

            // Coordinates Loop = [100, 200], [300, 400] ...
            for (int j = 0; j < numberOfStations; j++){
                String[] coordinatesLineArray = reader.readLine().split(" ");
                coordinates[j][0] = Integer.parseInt(coordinatesLineArray[0]);
                coordinates[j][1] = Integer.parseInt(coordinatesLineArray[1]);
            }
            System.out.println(minimumThreshold(coordinates, numberOfDrones, numberOfStations));
        }
    }

    public static double minimumThreshold(int[][] coordinates, int numberOfDrones, int numberOfStations){
        // Create a list of all possible edges
        double[][] edges = new double[numberOfStations * (numberOfStations - 1) / 2][3];
        int index = 0;
        for (int i = 0; i < numberOfStations; i++) {
            for (int j = i + 1; j < numberOfStations; j++) {
                double distance = calculateDistance(coordinates[i], coordinates[j]);
                edges[index][0] = i;
                edges[index][1] = j;
                edges[index][2] = distance;
                index++;
            }
        }
        // Sort the edges by weight
        Arrays.sort(edges, Comparator.comparingDouble(a -> a[2]));

        // Initialize parent array for union-find
        int[] parent = new int[numberOfStations];
        for (int i = 0; i < numberOfStations; i++) {
            parent[i] = i;
        }

        // Start adding edges to the spanning tree
        double maxWeight = 0;
        int count = 0;
        for (double[] edge : edges) {
            int root1 = find((int)edge[0], parent);
            int root2 = find((int)edge[1], parent);

            // If adding this edge does not form a cycle
            if (root1 != root2) {
                union(root1, root2, parent);
                maxWeight = Math.max(maxWeight, edge[2]);
                count++;

                // If we have added enough edges, stop
                if (count == numberOfStations - numberOfDrones) {
                    break;
                }
            }
        }
        return Math.round(maxWeight * 100.0) / 100.0; // round to 2 decimal places
    }

    // Helper function for union-find
    private static int find(int x, int[] parent) {
        if (parent[x] != x) {
            parent[x] = find(parent[x], parent);
        }
        return parent[x];
    }

    // Helper function for union-find
    private static void union(int x, int y, int[] parent) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }

    // Helper function to calculate distance between two stations
    private static double calculateDistance(int[] station1, int[] station2) {
        int x1 = station1[0];
        int y1 = station1[1];
        int x2 = station2[0];
        int y2 = station2[1];
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
