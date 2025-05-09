import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class Element {
    int scroll;
    int knowledge;

    public Element(int scroll, int knowledge) {
        this.scroll = scroll;
        this.knowledge = knowledge;
    }
}

public class MaxScrollsDP {
    private ArrayList<ArrayList<Integer>> safesDiscovered = new ArrayList<>();
    // Input format will be the same as following:
    // Number of safes
    // [Complexity,Scroll] Pair
    // [Complexity,Scroll] Pair
    // .
    // .
    // .
    // [Complexity,Scroll] Pair
    // See example provided below:
    // 3
    // [5,10]
    // [10,10]
    // [5,20]

    public MaxScrollsDP(ArrayList<ArrayList<Integer>> safesDiscovered) {
        this.safesDiscovered = safesDiscovered;
    }

    public ArrayList<ArrayList<Integer>> getSafesDiscovered() {
        return safesDiscovered;
    }

    public OptimalScrollSolution optimalSafeOpeningAlgorithm() throws FileNotFoundException {
        // Implement your dynamic programming algorithm using the equation 1
        // provided in the assignment file.
        int l=safesDiscovered.size();

        Element [][] dp=new Element[l+1][l+1];
        dp[0][0]=new Element(0,0);

        for (int i =1;i<=l;i++){
            for (int j=0;j<=l;j++){

                int knowledge=safesDiscovered.get(i-1).get(0);
                int scroll=safesDiscovered.get(i-1).get(1);

                if (dp[i - 1][j] == null) {
                    continue;

                }

                //maintain state
                if (dp[i][j] == null || dp[i][j].scroll<dp[i-1][j].scroll){
                    dp[i][j]=new Element(dp[i-1][j].scroll,j*5);
                }


                if (j+1<=l){
                    if (dp[i][j + 1] == null || dp[i][j + 1].scroll < dp[i - 1][j].scroll) {
                        dp[i][j + 1] = new Element(dp[i - 1][j].scroll, (j + 1)*5);
                    }
                }

                if (j >= knowledge/5) {
                    int newScrol = dp[i - 1][j].scroll + scroll;

                    if (dp[i][j-knowledge/5] == null || dp[i][j-knowledge/5].scroll < dp[i - 1][j].scroll + scroll) {
                        dp[i][j-knowledge/5] = new Element(newScrol, (j-knowledge/5)*5);
                    }
                }
            }

        }
       // print2DArray(dp);
        //System.out.println(dp[l][0].scroll);
        OptimalScrollSolution solution=new OptimalScrollSolution(safesDiscovered,dp[l][0].scroll);
        return solution;
    }


    public  void print2DArray(Element[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j]==null){
                    System.out.print(0+ "\t");
                    continue;
                }
                System.out.print(array[i][j].scroll + "\t");
            }
            System.out.println();
        }
    }
}
