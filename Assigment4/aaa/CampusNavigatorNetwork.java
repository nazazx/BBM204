import java.io.BufferedInputStream;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampusNavigatorNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageCartSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numCartLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<CartLine> lines;

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "\\s*=\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "\\s*=\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Point p = new Point(0, 0);

        Pattern pattern = Pattern.compile("\\b" + varName + "\\b\\s*=\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher m = pattern.matcher(fileContent);
        m.find();
        p.x = Integer.parseInt(m.group(1));
        p.y = Integer.parseInt(m.group(2));


        return p;
    }

    /**
     * Function to extract the cart lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of CartLine instances
     */
    public List<CartLine> getCartLines(String fileContent) {
        List<CartLine> cartLines = new ArrayList<>();

        Pattern p = Pattern.compile(
                "cart_line_name\\s*=\\s*\"([^\"]+)\"\\s*cart_line_stations\\s*=\\s*((?:\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)+)"
        );
        Matcher m = p.matcher(fileContent);

        while (m.find()) {
            String lineName = m.group(1).trim();
            String stationsRaw = m.group(2).trim();

            List<Station> stationList = new ArrayList<>();
            Pattern stationP = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
            Matcher stationM = stationP.matcher(stationsRaw);

            int stationCounter = 1;
            while (stationM.find()) {
                int x = Integer.parseInt(stationM.group(1));
                int y = Integer.parseInt(stationM.group(2));
                String stationName = lineName + " Station " + stationCounter++;
                stationList.add(new Station(new Point(x, y), stationName));
            }

            cartLines.add(new CartLine(lineName, stationList));
        }

        return cartLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename){
            try {


            // TODO: Your code goes here
            String fileContent = Files.readString(Paths.get(filename));
            this.numCartLines = getIntVar("num_cart_lines", fileContent);
            this.averageCartSpeed = getDoubleVar("average_cart_speed", fileContent)*100.0/6.0;

            Point start = getPointVar("starting_point", fileContent);
            Point end = getPointVar("destination_point", fileContent);

            this.startPoint = new Station(start, "Starting Point");
            this.destinationPoint = new Station(end, "Final Destination");

            this.lines = getCartLines(fileContent);

            }
            catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
    }

}