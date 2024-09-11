import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.*;
import java.util.*;

@FunctionalInterface
interface Sorter {
    void sort(int[] array);
}

@FunctionalInterface
interface Searcher {
    void search(int[] array, int key);
}


public class Experiments {
    public static void showAndSaveChart1(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
        chart.addSeries("Merge Sort", doubleX, yAxis[1]);
        chart.addSeries("Counting Sort", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    public static void showAndSaveChart2(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Nanoseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Linear Search on Random Data", doubleX, yAxis[0]);
        chart.addSeries("Linear Search on Sorted Data", doubleX, yAxis[1]);
        chart.addSeries("Binary Search on Sorted Data", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    public static int[] csvExtractor(String fileName, int sampleSize) throws IOException{
        int[] result = new int[sampleSize];
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        reader.readLine(); // avoiding header line
        for (int i = 0; i < sampleSize; i++) {
            String[] splitted = reader.readLine().split(",");
            result[i] = Integer.parseInt(splitted[6]);
        }
        return result;
    }

    public static double sortTimer(int[] sample, Sorter sorter) {
        double[] tenTimesRun = new double[10]; // all experiments are tested 10 times
        for(int i = 0; i < 10; i++){
            long time1 = System.currentTimeMillis();

            // sort or search algorithm comes here
            sorter.sort(sample);

            long time2 = System.currentTimeMillis();
            long duration = time2 - time1;
            tenTimesRun[i] = duration;
        }
        return Arrays.stream(tenTimesRun).sum() / 10; // average of 10 tests
    }

    public static double searchTimer(int[] sample, Searcher searcher) {
        double[] thousandTimesRun = new double[1000]; // all experiments are tested 1000 times
        for(int i = 0; i < 1000; i++){
            int randomIndex = new Random().nextInt(sample.length);// picking a random elements for each iteration
            int key = sample[randomIndex];
            long time1 = System.currentTimeMillis();

            // sort or search algorithm comes here
            searcher.search(sample, key);

            long time2 = System.currentTimeMillis();
            long durationInNano = (long) ((time2 - time1) * Math.pow(10, 6)); // millisecond to nanosecond
            thousandTimesRun[i] = durationInNano;
        }
        return Arrays.stream(thousandTimesRun).sum() / 1000; // average of 1000 tests
    }

    public static void randomSearcher(int[] inputAxis, double[] result, Searcher searcher) throws IOException{
        for(int i = 0; i < inputAxis.length; i++){
            int[] sample = csvExtractor("TrafficFlowDataset.csv", inputAxis[i]);
            result[i] = searchTimer(sample, searcher);
            System.out.println("Average Time with " + inputAxis[i] + " input: " + result[i] + " ns");
        }
    }

    public static void sortedSearcher(int[] inputAxis, double[] result, Searcher searcher) throws IOException{
        for(int i = 0; i < inputAxis.length; i++){
            int[] sample = csvExtractor("TrafficFlowDataset.csv", inputAxis[i]);
            Arrays.sort(sample);
            result[i] = searchTimer(sample, searcher);
            System.out.println("Average Time with " + inputAxis[i] + " input: " + result[i] + " ns");
        }
    }

    public static void randomSorter(int[] inputAxis, double[] result, Sorter sorter) throws IOException{
        for(int i = 0; i < inputAxis.length; i++){
            int[] sample = csvExtractor("TrafficFlowDataset.csv", inputAxis[i]);
            result[i] = sortTimer(sample, sorter);
            System.out.println("Average Time with " + inputAxis[i] + " input: " + result[i] + " ms");
        }
    }

    public static void sortedSorter(int[] inputAxis, double[] result, Sorter sorter) throws IOException{
        for(int i = 0; i < inputAxis.length; i++){
            int[] sample = csvExtractor("TrafficFlowDataset.csv", inputAxis[i]);
            Arrays.sort(sample);
            result[i] = sortTimer(sample, sorter);
            System.out.println("Average Time with " + inputAxis[i] + " input: " + result[i] + " ms");
        }
    }

    public static void reverselySortedSorter(int[] inputAxis, double[] result, Sorter sorter) throws IOException{
        for(int i = 0; i < inputAxis.length; i++){
            int[] sample = csvExtractor("TrafficFlowDataset.csv", inputAxis[i]);
            // effectively reverse order sorting
            for (int j = 0; j < sample.length; j++) {
                sample[j] = -sample[j];
            }
            Arrays.sort(sample);
            for (int j = 0; j < sample.length; j++) {
                sample[j] = -sample[j];
            }
            result[i] = sortTimer(sample, sorter);
            System.out.println("Average Time with " + inputAxis[i] + " input: " + result[i] + " ms");
        }
    }

    public static void main(String[] args) throws IOException{
        // X axis data
        final int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16_000, 32_000, 64_000, 128_000, 250_000}; // fixed

        // SORT

        // INSERTION SORT WITH RANDOM
        System.out.println("INSERTION SORT WITH RANDOM");
        double[] insertionSortRandomResults = new double[inputAxis.length];
        randomSorter(inputAxis, insertionSortRandomResults, Main::insertionSort);

        // MERGE SORT WITH RANDOM
        System.out.println("MERGE SORT WITH RANDOM");
        double[] mergeSortRandomResults = new double[inputAxis.length];
        randomSorter(inputAxis, mergeSortRandomResults, Main::mergeSort);

        // COUNTING SORT WITH RANDOM
        System.out.println("COUNTING SORT WITH RANDOM");
        double[] countingSortRandomResults = new double[inputAxis.length];
        randomSorter(inputAxis, countingSortRandomResults, Main::countingSort);

        double[][] resultsRandom = new double[3][10];
        resultsRandom[0] = insertionSortRandomResults;
        resultsRandom[1] = mergeSortRandomResults;
        resultsRandom[2] = countingSortRandomResults;
        showAndSaveChart1("Sort Algorithms on Random Data", inputAxis, resultsRandom);

        // INSERTION SORT WITH SORTED
        System.out.println("INSERTION SORT WITH SORTED");
        double[] insertionSortSortedResults = new double[inputAxis.length];
        sortedSorter(inputAxis, insertionSortSortedResults, Main::insertionSort);

        // MERGE SORT WITH SORTED
        System.out.println("MERGE SORT WITH SORTED");
        double[] mergeSortSortedResults = new double[inputAxis.length];
        sortedSorter(inputAxis, mergeSortSortedResults, Main::mergeSort);

        // COUNTING SORT WITH SORTED
        System.out.println("COUNTING SORT WITH SORTED");
        double[] countingSortSortedResults = new double[inputAxis.length];
        sortedSorter(inputAxis, countingSortSortedResults, Main::countingSort);

        double[][] resultsSorted = new double[3][10];
        resultsSorted[0] = insertionSortSortedResults;
        resultsSorted[1] = mergeSortSortedResults;
        resultsSorted[2] = countingSortSortedResults;
        showAndSaveChart1("Sort Algorithms on Sorted Data", inputAxis, resultsSorted);

        // INSERTION SORT WITH REVERSELY SORTED
        System.out.println("INSERTION SORT WITH REVERSELY SORTED");
        double[] insertionSortReverselySortedResults = new double[inputAxis.length];
        reverselySortedSorter(inputAxis, insertionSortReverselySortedResults, Main::insertionSort);

        // MERGE SORT WITH REVERSELY SORTED
        System.out.println("MERGE SORT WITH REVERSELY SORTED");
        double[] mergeSortReverselySortedResults = new double[inputAxis.length];
        reverselySortedSorter(inputAxis, mergeSortReverselySortedResults, Main::mergeSort);

        // COUNTING SORT WITH REVERSELY SORTED
        System.out.println("COUNTING SORT WITH REVERSELY SORTED");
        double[] countingSortReverselySortedResults = new double[inputAxis.length];
        reverselySortedSorter(inputAxis, countingSortReverselySortedResults, Main::countingSort);

        double[][] resultsReverselySorted = new double[3][10];
        resultsReverselySorted[0] = insertionSortReverselySortedResults;
        resultsReverselySorted[1] = mergeSortReverselySortedResults;
        resultsReverselySorted[2] = countingSortReverselySortedResults;
        showAndSaveChart1("Sort Algorithms on Reversely Sorted Data", inputAxis, resultsReverselySorted);

        System.out.println("SORTS ARE DONE!");

        // SEARCH
        System.out.println("LINEAR SEARCH WITH RANDOM DATA");
        double[] linearSearchRandomResults = new double[inputAxis.length];
        randomSearcher(inputAxis, linearSearchRandomResults, Main::linearSearch);

        System.out.println("LINEAR SEARCH WITH SORTED DATA");
        double[] linearSearchSortedResults = new double[inputAxis.length];
        sortedSearcher(inputAxis, linearSearchSortedResults, Main::linearSearch);

        System.out.println("BINARY SEARCH WITH SORTED DATA");
        double[] binarySearchSortedResults = new double[inputAxis.length];
        sortedSearcher(inputAxis, binarySearchSortedResults, Main::binarySearch);

        double[][] resultsSearch = new double[3][10];
        resultsSearch[0] = linearSearchRandomResults;
        resultsSearch[1] = linearSearchSortedResults;
        resultsSearch[2] = binarySearchSortedResults;
        showAndSaveChart2("Search Algorithms on Random and Sorted Data", inputAxis, resultsSearch);

        System.out.println("SEARCH OPERATIONS ARE DONE!");
    }
}
