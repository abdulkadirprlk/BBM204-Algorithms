import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing number: " + e.getMessage());
                return 0;
            }
        } else {
            System.out.println("No match found for variable: " + varName);
            return 0;
        }
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("\\s*" + varName + "\\s*=\\s*\"(.+)\"");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return m.group(1);
        } else {
            System.out.println("No match found for variable: " + varName);
            return "";
        }
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("\\s*" + varName + "\\s*=\\s*([0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing number: " + e.getMessage());
                return 0.0;
            }
        } else {
            System.out.println("No match found for variable: " + varName);
            return 0.0;
        }
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Point p = new Point(0, 0);
        Pattern x = Pattern.compile("\\s*" + varName + "\\s*=\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher m = x.matcher(fileContent);
        if (m.find()) {
            p.x = Integer.parseInt(m.group(1));
            p.y = Integer.parseInt(m.group(2));
        } else {
            System.out.println("No match found for variable: " + varName);
        }
        return p;
    }

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
        String[] lines = fileContent.split("\n");

        String trainLineName = null;
        List<Station> stations = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("train_line_name")) {
                // If a new train line name is found and there are at least two stations for the previous train line, add it to the list
                if (trainLineName != null && stations.size() >= 2) {
                    trainLines.add(new TrainLine(trainLineName, new ArrayList<>(stations)));
                }
                stations.clear();
                trainLineName = getStringVar("train_line_name", line);
            } else if (line.startsWith("train_line_stations")) {
                Pattern stationPattern = Pattern.compile("\\(([\\t ]*\\d+)[\\t ]*,[\\t ]*(\\d+)[\\t ]*\\)");
                Matcher stationMatcher = stationPattern.matcher(line);

                int stationCount = 1;
                while (stationMatcher.find()) {
                    try {
                        int x = Integer.parseInt(stationMatcher.group(1).trim());
                        int y = Integer.parseInt(stationMatcher.group(2).trim());
                        String stationName = trainLineName + " Line Station " + stationCount;
                        stations.add(new Station(new Point(x, y), stationName));
                        stationCount++;
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing number: " + e.getMessage());
                    }
                }
            }
        }
        // Add the last train line if it has at least two stations
        if (trainLineName != null && stations.size() >= 2) {
            trainLines.add(new TrainLine(trainLineName, stations));
        }

        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        StringBuilder fileContent = new StringBuilder();
        // TODO: Your code goes here
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
                if (reader.ready()) { // to check if there are more lines to read
                    fileContent.append("\n");
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        numTrainLines = getIntVar("num_train_lines", fileContent.toString());
        averageTrainSpeed = (getDoubleVar("average_train_speed", fileContent.toString()) * 1000) / 60;
        Point startingPoint = getPointVar("starting_point", fileContent.toString());
        Point endPoint = getPointVar("destination_point", fileContent.toString());
        startPoint = new Station(startingPoint, "Starting Point");
        destinationPoint = new Station(endPoint, "Final Destination");
        lines = getTrainLines(fileContent.toString());
    }

    public double calculateDuration(Point point1, Point point2, double speed) {
        double dx = point1.x - point2.x;
        double dy = point1.y - point2.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance / speed;
    }
}