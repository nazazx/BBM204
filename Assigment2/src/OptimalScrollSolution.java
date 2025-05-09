import java.util.ArrayList;

public class OptimalScrollSolution {
    private final ArrayList<ArrayList<Integer>> safeSet;
    private final int solution;

    OptimalScrollSolution(ArrayList<ArrayList<Integer>> safeSet, int solution) {
        this.safeSet = safeSet;
        this.solution = solution;
    }

    public int getSolution() {
        return solution;
    }

    public ArrayList<ArrayList<Integer>> getSafeSet() {
        return safeSet;
    }

    public void printSolution(OptimalScrollSolution solution) {
        // Print your OptimalScrollSolution object in the format provided in the assignment pdf.
        ArrayList<ArrayList<Integer>> safe_set=solution.safeSet;
        int sol=solution.solution;
        System.out.println("Maximum scrolls acquired: "+sol);
        System.out.print("For the safe set of :[");
        for (int i =0;i<safe_set.size();i++){
            int first=safe_set.get(i).get(0);
            int second=safe_set.get(i).get(1);
            System.out.print("["+first+", "+second+"]");
            if (i!=safe_set.size()-1){
                System.out.print(", ");
            }
        }
        System.out.println("]");

    }
}
