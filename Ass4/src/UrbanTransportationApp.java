import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        try{
            hyperloopTrainNetwork.readInput(filename);
        } catch (Exception e){
            System.out.println(e);
        }
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<RouteDirection> edgesOfDigraph = getRouteDirections(network);

        // map to store the shortest distance of every vertex from the source
        Map<String, Double> shortestDistances = new HashMap<>();
        // map to store the previous vertex in the shortest path
        Map<String, String> previousVertices = new HashMap<>();
        // a priority queue to store the vertices to be processed, sorted by their shortest distance
        PriorityQueue<Map.Entry<String, Double>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());

        // Initialize the shortest distance of every vertex as infinity
        for (RouteDirection edge : edgesOfDigraph) {
            shortestDistances.put(edge.startStationName, Double.MAX_VALUE);
            shortestDistances.put(edge.endStationName, Double.MAX_VALUE);
        }

        // The shortest distance from the source to itself is 0
        shortestDistances.put(network.startPoint.description, 0.0);
        queue.add(new AbstractMap.SimpleEntry<>(network.startPoint.description, 0.0));

        while (!queue.isEmpty()) {
            String currentVertex = queue.poll().getKey();

            for (RouteDirection edge : edgesOfDigraph) {
                if (edge.startStationName.equals(currentVertex)) {
                    double newDistance = shortestDistances.get(currentVertex) + edge.duration;
                    if (newDistance < shortestDistances.get(edge.endStationName)) {
                        queue.removeIf(entry -> entry.getKey().equals(edge.endStationName));
                        queue.add(new AbstractMap.SimpleEntry<>(edge.endStationName, newDistance));
                        shortestDistances.put(edge.endStationName, newDistance);
                        previousVertices.put(edge.endStationName, currentVertex);
                    }
                }
            }
        }

        // Build the shortest path
        String vertex = network.destinationPoint.description;
        while (vertex != null) {
            String previousVertex = previousVertices.get(vertex);
            if (previousVertex != null) {
                for (RouteDirection edge : edgesOfDigraph) {
                    if (edge.startStationName.equals(previousVertex) && edge.endStationName.equals(vertex)) {
                        routeDirections.add(0, edge);
                        break;
                    }
                }
            }
            vertex = previousVertex;
        }
        return routeDirections;
    }

    public List<RouteDirection> getRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> allRouteDirections = new ArrayList<>();

        // Create the digraph (edges are routeDirections)
        // add edges from the "start point" to every other node including destination point : ok
        // add edges from all stations to destination point : ok
        for(int i = 0; i < network.lines.size(); i++){
            TrainLine trainLine = network.lines.get(i);
            for(int j = 0; j < trainLine.trainLineStations.size(); j++) {
                Station station1 = trainLine.trainLineStations.get(j);
                double duration1 = network.calculateDuration(network.startPoint.coordinates,
                        station1.coordinates, network.averageWalkingSpeed);
                RouteDirection routeDirection1 = new RouteDirection(network.startPoint.description, station1.description,
                        duration1, false);
                double durationFromStationToDestination = network.calculateDuration(station1.coordinates,
                        network.destinationPoint.coordinates, network.averageWalkingSpeed);
                RouteDirection routeDirectionToDestination = new RouteDirection(station1.description,
                        network.destinationPoint.description, durationFromStationToDestination, false);
                allRouteDirections.add(routeDirection1);
                allRouteDirections.add(routeDirectionToDestination);

                // add edges from station to stations by train : ok
                for (int k = 0; k < trainLine.trainLineStations.size(); k++) {
                    if (k == j + 1 || k == j - 1) {
                        Station station2 = trainLine.trainLineStations.get(k);
                        double duration = network.calculateDuration(station1.coordinates, station2.coordinates,
                                network.averageTrainSpeed);
                        // create an edge from station1 to station2
                        RouteDirection routeDirection2 = new RouteDirection(station1.description, station2.description,
                                duration, true);
                        allRouteDirections.add(routeDirection2);
                    }
                }
            }
        }
        // from start point to destination point
        double duration = network.calculateDuration(network.startPoint.coordinates,
                network.destinationPoint.coordinates, network.averageWalkingSpeed);
        RouteDirection routeDirection = new RouteDirection(network.startPoint.description,
                network.destinationPoint.description, duration, false);
        allRouteDirections.add(routeDirection);

        // add edges from stations to other stations(on the other train lines) by walk
        for (int i = 0; i < network.lines.size(); i++){
            TrainLine trainLine1 = network.lines.get(i);
            for (int j = 0; j < network.lines.size(); j++) {
                if (i != j) {
                    TrainLine trainLine2 = network.lines.get(j);
                    for (Station station1 : trainLine1.trainLineStations) {
                        for (Station station2 : trainLine2.trainLineStations) {
                            double durationToOtherStations = network.calculateDuration(station1.coordinates,
                                    station2.coordinates, network.averageWalkingSpeed);
                            RouteDirection routeDirectionForStationsByWalk = new RouteDirection(station1.description, station2.description,
                                    durationToOtherStations, false);
                            allRouteDirections.add(routeDirectionForStationsByWalk);
                        }
                    }
                }
            }
        }
        return allRouteDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        
        // TODO: Your code goes here

        double totalMinutes = 0;
        for (int i = 0; i < directions.size(); i++){
            totalMinutes += directions.get(i).duration;
        }
        totalMinutes = Math.round(totalMinutes);
        // print to STDOUT
        System.out.println("The fastest route takes " + (int) totalMinutes + " minute(s).");
        System.out.println("Directions");
        System.out.println("----------");
        for (int i = 0; i < directions.size(); i++) {
            System.out.print(i + 1 + ". ");
            String startPointName = directions.get(i).startStationName;
            String endPointName = directions.get(i).endStationName;
            double duration = directions.get(i).duration;
            boolean isTrain = directions.get(i).trainRide;
            if (!isTrain){ // not train, walking
                System.out.println("Walk from \"" + startPointName + "\" to \"" + endPointName + "\" for " +
                        String.format("%.2f", duration) + " minutes.");
            }
            else { // by train
                System.out.println("Get on the train from \"" + startPointName + "\" to \"" + endPointName + "\" for " +
                        String.format("%.2f", duration) + " minutes.");
            }
        }
    }
}