import java.util.ArrayList;

public class OptimalShipSolution {
    private final ArrayList<Integer> artifactSet;
    private final int solution;

    OptimalShipSolution(ArrayList<Integer> artifactSet, int solution) {
        this.artifactSet = artifactSet;
        this.solution = solution;
    }

    public int getSolution() {
        return solution;
    }

    public ArrayList<Integer> getArtifactSet() {
        return artifactSet;
    }

    public void printSolution(OptimalShipSolution solution) {
        // Print your OptimalShipSolution object in the format provided in the assignment pdf.
        int sol = solution.solution;
        ArrayList<Integer> artifacts=solution.artifactSet;

        System.out.println("Minimum spaceships required: "+sol);
        System.out.print("For the artifact set of :[");
        for (int i=0;i<artifacts.size();i++){
            int weight=artifacts.get(i);
            System.out.print(weight);
            if (i!=artifacts.size()-1){
                System.out.print(", ");
            }

        }
        System.out.println("]");
    }
}
