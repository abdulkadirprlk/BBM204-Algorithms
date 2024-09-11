import java.util.*;
import java.io.*;

public class Quiz2 {
    public static void main(String[] args) throws IOException {
        
        // TODO: Use the first command line argument (args[0]) as the file to read the input from

        String fileName = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line1 = reader.readLine();
        int capacity = Integer.parseInt(line1.split(" ")[0]);// capacity is read
        int n = Integer.parseInt(line1.split(" ")[1]);// n is the number of resources

        int[] masses = new int[n];
        String line2 = reader.readLine();
        String[] tokens = line2.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            masses[i] = Integer.parseInt(tokens[i]);// masses are read
        }

        // TODO: Your code goes here
        findMaxMass(capacity, masses);

        // TODO: Print the solution to STDOUT

    }

    public static void findMaxMass(int M, int[] masses) {
        int n = masses.length;
        int[][] L = new int[M + 1][n + 1];

        // Building the L(m,i) matrix(implementation of the function)
        for (int m = 0; m <= M; m++) {
            for (int i = 0; i <= n; i++) {
                if (i == 0 && m == 0)
                    L[m][i] = 1;
                else if (i == 0) // m is already greater than 0
                    L[m][i] = 0;
                else if (masses[i - 1] > m) // i is already greater than 0
                    L[m][i] = L[m][i - 1];
                else {
                    L[m][i] = Math.max(L[m][i - 1], L[m - masses[i - 1]][i - 1]);
                }
            }
        }
        int maxMass = 0;
        for (int m = M; m >= 0; m--) {
            if (L[m][n] == 1) { // L(m,n) means is it possible to achieve m mass using first n resources
                maxMass = m;
                break;
            }
        }
        System.out.println(maxMass); // max mass can be loaded
        // printing the L(m,i) matrix
        for (int m = 0; m <= M; m++) {
            for (int i = 0; i <= n; i++) {
                System.out.print(L[m][i]);
            }
            System.out.println();
        }
    }
}
