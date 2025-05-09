import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static boolean  control(String file_path) throws IOException{
        List<String> lines= Files.readAllLines(Paths.get(file_path));
        if (lines.size()==1){
            return false;
        }
        return true;
    }
    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("Error");
            return;
        }
        String path1=args[0];
        String path2=args[1];
        /** Safe-lock Opening Algorithm Below **/


        System.out.println("##Initiate Operation Safe-lock##");
        // TODO: Your code goes here
        ArrayList<ArrayList<Integer>> safes= read_safe_discovered(path1);
        MaxScrollsDP dp=new MaxScrollsDP(safes);

       OptimalScrollSolution solution= dp.optimalSafeOpeningAlgorithm();
       solution.printSolution(solution);
        // You are expected to read the file given as the first command-line argument to read
        // safes arriving each minute. Then, use this data to instantiate a
        // OptimalScrollSolution object. You need to call optimalSafeOpeningAlgorithm() method
        // of your MaxScrollsDP object to get the solution, and finally print it using
        // printSolution() method of OptimalScrollSolution object.
        System.out.println("##Operation Safe-lock Completed##");

        /** Operation Artifact Algorithm Below **/

        System.out.println("##Initiate Operation Artifact##");
        // TODO: Your code goes here
        ArrayList<Integer> artifacts=read_Artifacts(path2);
        MinShipsGP greedy=new MinShipsGP(artifacts);
        OptimalShipSolution solution2 = greedy.optimalArtifactCarryingAlgorithm();
        solution2.printSolution(solution2);

        // You are expected to read the file given as the second command-line argument to read
        // each artifact. Then, use this data to instantiate an OptimalShipSolution object.
        // You need to call optimalArtifactCarryingAlgorithm() method
        // of your MinShipsGP object to get the solution, and finally print it using
        // printSolution() method of OptimalShipSolution object.
        System.out.println("##Operation Artifact Completed##");

    }

    public static ArrayList<ArrayList<Integer>> read_safe_discovered(String file_path) throws IOException {
        ArrayList<ArrayList<Integer>> safes = new ArrayList<>();
        List<String> lines= Files.readAllLines(Paths.get(file_path));

        //int numSafes = Integer.parseInt(lines.get(0));

        for (int i=0;i<lines.size()-1;i++){
            String [] elements=lines.get(i+1).split(",");
            ArrayList<Integer> safe=new ArrayList<>();
            safe.add(Integer.parseInt(elements[0]));
            safe.add(Integer.parseInt(elements[1]));
            safes.add(safe);
        }
        return safes;

    }
    public static ArrayList<Integer> read_Artifacts(String file_path) throws IOException {
        ArrayList<Integer> artifacts = new ArrayList<>();
        List<String> lines= Files.readAllLines(Paths.get(file_path));

        String [] elements=lines.get(0).split(",");

        for (int i=0;i< elements.length;i++){
            artifacts.add(Integer.parseInt(elements[i]));
        }
        return artifacts;

    }
}