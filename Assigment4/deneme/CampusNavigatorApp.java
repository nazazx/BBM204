import java.io.Serializable;
import java.util.*;

class Edge{
    Station source;
    Station destination;
    double w;

    public Edge(Station source, Station destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.w= weight;
    }
}
class Graph {
    public final HashMap<Station, ArrayList<Edge>> adjacencyList = new HashMap<>();
    public HashMap<Station, Station> predecessors = new HashMap<>();
    public HashMap<Set<Station>, Double> times = new HashMap<>();

    public void addEdge(Station source, Station destination, double weight) {
        if (!adjacencyList.containsKey(source)) {
            adjacencyList.put(source, new ArrayList<>());
        }
        if (!adjacencyList.containsKey(destination)) {
            adjacencyList.put(destination, new ArrayList<>());
        }

        Set<Station> key = new HashSet<>(Arrays.asList(source, destination));
        if (!times.containsKey(key) || weight < times.get(key)) {

            adjacencyList.get(source).removeIf(e -> e.destination.equals(destination));
            adjacencyList.get(destination).removeIf(e -> e.destination.equals(source));

            adjacencyList.get(source).add(new Edge(source, destination, weight));
            adjacencyList.get(destination).add(new Edge(destination, source, weight));
            times.put(key, weight);
        }
    }
    public double dijkstra(Station station_start, Station station_end) {
        Map<Station, Double> costs = new HashMap<>();
        for (Station station:adjacencyList.keySet()){
            costs.put(station,Double.MAX_VALUE);
        }

        PriorityQueue<Station> queue=new PriorityQueue<>(Comparator.comparing(costs::get));
        costs.put(station_start,0.0);
        queue.offer(station_start);


        while (!queue.isEmpty()) {
            Station current_station = queue.poll();


            double current_cost=costs.get(current_station);


            if (current_station.equals(station_end)){
                return current_cost;
            }

            for (Edge link : adjacencyList.get(current_station)){
                Station neighbor=link.destination;
                double distance=link.w;
                double value=current_cost+distance;
                if (value<costs.get(neighbor)){

                    predecessors.put(neighbor,current_station);

                    costs.put(neighbor,value);
                    queue.offer(neighbor);
                }
            }
        }

        return -1;
    }

}
public class CampusNavigatorApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HashMap<Station, Station> predecessors = new HashMap<>();
    public HashMap<Set<Station>, Double> times = new HashMap<>();
    public double val_short=Double.MAX_VALUE;
    public CampusNavigatorNetwork readCampusNavigatorNetwork(String filename) throws Exception {

        CampusNavigatorNetwork network = new CampusNavigatorNetwork();
        network.readInput(filename);
        return network;
    }

    /**
     * Calculates the fastest route from the user's selected starting point to 
     * the desired destination, using the campus golf cart network and walking paths.
     * @return List of RouteDirection instances
     */
    public double calculate_euclidean(Station station1, Station station2){
        double x1=station1.coordinates.x;
        double y1=station1.coordinates.y;
        double x2=station2.coordinates.x;
        double y2=station2.coordinates.y;

        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));

    }

    public List<RouteDirection> getFastestRouteDirections(CampusNavigatorNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();

        int n=network.numCartLines;
        double CartSpeed = (network.averageCartSpeed);
        double WalkSpeed=network.averageWalkingSpeed;
        Station start=network.startPoint;
        Station end=network.destinationPoint;

        Graph graph=new Graph();

        List<Station> allStations = new ArrayList<>();
        for (CartLine line : network.lines) {
            allStations.addAll(line.cartLineStations);
        }

        for (int i = 0; i < allStations.size(); i++) {
            for (int j = i + 1; j < allStations.size(); j++) {
                Station s1 = allStations.get(i);
                Station s2 = allStations.get(j);

                if (!s1.equals(s2)) {
                    double walkDist = calculate_euclidean(s1, s2);
                    graph.addEdge(s1, s2, walkDist / WalkSpeed);
                }
            }
        }

        for (CartLine line:network.lines){
            List<Station> stations=line.cartLineStations;
            for (int i=0;i<stations.size()-1;i++){
                Station station1=stations.get(i);
                Station station2=stations.get(i+1);
                double distance=calculate_euclidean(station1,station2);
                graph.addEdge(station1,station2,distance/CartSpeed);

            }
        }
        for (CartLine line : network.lines) {
            for (Station station : line.cartLineStations) {
                double ds = calculate_euclidean(start, station);
                double de = calculate_euclidean(end, station);
                graph.addEdge(start, station, ds / WalkSpeed);
                graph.addEdge(end, station, de / WalkSpeed);

            }
        }


        graph.addEdge(start,end,calculate_euclidean(start,end)/WalkSpeed);

        val_short=graph.dijkstra(start,end);
        predecessors=graph.predecessors;
        times=graph.times;

        Station cur=end;

        while (cur!=start){
            Station prev=predecessors.get(cur);

            if (prev==null){
                break;
            }
            double time=times.get(new HashSet<>(Arrays.asList(cur,prev)));
            boolean isCartline=isCartline(prev,cur,network);
            RouteDirection direction=new RouteDirection(prev.description,cur.description,time,isCartline);
            routeDirections.add(0,direction);
            cur=prev;
        }
        // TODO: Your code goes here

        return routeDirections;
    }
    private boolean isCartline(Station a, Station b, CampusNavigatorNetwork network) {
        for (CartLine line : network.lines) {
            List<Station> stations = line.cartLineStations;
            for (int i = 0; i < stations.size() - 1; i++) {
                Station s1 = stations.get(i);
                Station s2 = stations.get(i + 1);
                if ((s1.equals(a) && s2.equals(b)) || (s1.equals(b) && s2.equals(a))) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {

        System.out.printf("The fastest route takes %.0f minute(s).\n", val_short);
        System.out.println("Directions");
        System.out.println("----------");

        int i = 1;
        for (RouteDirection direction : directions) {
            String type_name="";
            if (direction.cartRide){
                type_name="Ride the cart";
            }else{
                type_name="Walk";
            }

            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.\n",
                    i++, type_name, direction.startStationName, direction.endStationName, direction.duration);
        }
        // TODO: Your code goes here

    }
}
