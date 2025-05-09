import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class MinShipsGP {
    private int ship_capacity=100;

    private final ArrayList<Integer> artifactsFound = new ArrayList<>();
    // Weight of artifacts as list will be provided in the input file, and the list
    // should be populated using this format.
    // [3,2,3,4,5,4]

    public ArrayList<Integer> getArtifactsFound() {
        return artifactsFound;
    }

    MinShipsGP(ArrayList<Integer> artifactsFound) {
        this.artifactsFound.addAll(artifactsFound);
    }

    public OptimalShipSolution optimalArtifactCarryingAlgorithm() throws FileNotFoundException {
        // Implement your greedy programming algorithm using the equation 2
        // provided in the assignment file.
        ArrayList<Integer> sorted=new ArrayList<>(artifactsFound);
        Collections.sort(sorted);
        Collections.reverse(sorted);
        int k=0;
        int l=sorted.size();
        boolean [] use=new boolean[sorted.size()];

        for (int i=0;i<l;i++){
            if (use[i]){
                continue;
            }
            use[i]=true;
            int element=sorted.get(i);
            int remain =ship_capacity-element;

            for (int j=i+1;j<l;j++){

                if (use[j]){
                    continue;
                }
                if (remain<sorted.get(j)){
                    continue;
                }

                remain-=sorted.get(j);
                use[j]=true;

            }
            k++;

        }
        OptimalShipSolution solution=new OptimalShipSolution(artifactsFound,k);
        return solution;
        //System.out.println(k);


    }
}
