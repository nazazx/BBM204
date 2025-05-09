import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.font.GraphicAttribute;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Main {


    // This Funciton is written for just testing or debugging
    public static void print(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

    }

    // This function reads the CSV file and extracts the third column values as an integer array.
    public static  int[] csv(String path) throws FileNotFoundException {
        ArrayList<Integer> third_col=new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            line= br.readLine();
            while (line != null) {
                String[] values = line.split(",");
                int element = Integer.parseInt(values[2].trim());
                third_col.add(element);
                line= br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return third_col.stream().mapToInt(i -> i).toArray();



    }
    // This function extracts a subarray of size 'x' from the given array.
    public static int[] fetch_data(int [] flow_duration,int x){
        int arr[]=new int[x];

        for (int i =0;i<x;i++){
            arr[i]=flow_duration[i];
        }
        return arr;
    }

    // This function measures the execution time of the specified sorting algorithm.
    public static double measure(int [] arr,String algo){
        int[] copy=arr.clone();
        long start=System.nanoTime();

        switch (algo){

            case "comb":
                Sorting.comb(copy);
                break;
            case "insertion":
                Sorting.insertion(copy);
                break;
            case "shaker":
                Sorting.ShakerSort(copy);
                break;
            case "shell":
                Sorting.shellSort(copy);
                break;
            case "radix":
                Sorting.radixSort(copy);
                break;
            default:
                return -1;

        }
        long end=System.nanoTime();
        long result=end-start;
        return result/1_000_000.0; // Convert nanoseconds to milliseconds
    }

    // This function handles sorting experiments for different input sizes and algorithms.
    public static double [][] handle(int [] arr,int [] input_size,String [] algorithms){
       double [][] result=new double[algorithms.length][input_size.length];

        for (int i=0;i<10;i++){ // Run 10 times for averaging

        double [][] Y_elements=new double[5][10];
        int b=0;
        for (String s : algorithms){
            int a=0;
            for (int size:input_size){
                int [] data=fetch_data(arr,size);
                double time =measure(data,s);
                Y_elements[b][a]=time;
                a++;
            }
            b++;

        }
        result=add(result,Y_elements);
        }
        return divide(result,10);  // Compute the average over 10 iterations

    }

    // This function sorts the array using Comb Sort.
    public static int [] sort(int [] arr){
        int[] copy=arr.clone();
        Sorting.comb(copy);
        return copy;
    }

    // This function reverses the sorted array.
    public static int [] reverse(int [] arr){
        int[] reversed = arr.clone();
        int l = 0;
        int r = reversed.length - 1;
        while (l<r){
            int temp=reversed[l];
            reversed[l]=reversed[r];
            reversed[r]=temp;
            l++;
            r--;
        }
        return reversed;
    }


    // This function adds two 2D arrays element-wise. It is similiar to numPy implementation
    public static double [][] add(double[][] arr1, double[][] arr2){
        double [][] result=new double[arr1.length][arr1[0].length];
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                result[i][j]=arr1[i][j]+arr2[i][j];
            }
        }
        return result;
    }

    // This function divides all elements of a 2D array by 'x'.
    public static double [][] divide(double[][] arr,int x){
        double [][] result=new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                result[i][j]=arr[i][j]/x;
            }
        }
        return result;
    }

    // This function prints a 2D Double array.
    public static void  print(double[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.printf("%.2f ",arr[i][j]);
            }
            System.out.println();
        }
    }

    // This function runs sorting tests for one algorithm across different data types.

    public static double [][] handle_one_algorithm(int[] flowDurations,int [] sorted,int []  reversed,int [] input_size,String algo){
        double [][] y=new double[3][10];

        // Measure sorting times for random dataset
        int i=0;
        for (int size:input_size){
            int [] data=fetch_data(flowDurations,size);
            double time =measure(data,algo);
            y[0][i]=time;
            i++;
        }
        // Measure sorting times for sorted dataset
        int j=0;
        for (int size:input_size){
            int [] data=fetch_data(sorted,size);
            double time =measure(data,algo);
            y[1][j]=time;
            j++;
        }
        // Measure sorting times for reversed dataset
        int k=0;
        for (int size:input_size){
            int [] data=fetch_data(reversed,size);
            double time =measure(data,algo);
            y[2][k]=time;
            k++;
        }
        return y;



    }


    public static void main(String args[]) throws IOException {

        int[] flowDurations =csv("TrafficFlowDataset.csv");
        int [] sorted=sort(flowDurations);
        int [] reversed=reverse(sorted);

        int [] input_sizes={500,1000,2000,4000,8000,16000,32000,64000,128000,250000};
        String [] algorithms={"comb","insertion","shaker","shell","radix"};

        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};


        //CombSort
        double [][] y_comb=handle_one_algorithm(flowDurations,sorted,reversed,input_sizes,"comb");
        showAndSaveChart2("CombSort",inputAxis,y_comb);

        //InsertionSort
        double [][] y_insertion=handle_one_algorithm(flowDurations,sorted,reversed,input_sizes,"insertion");
        showAndSaveChart2("InsertionSort",inputAxis,y_insertion);

        //ShakerSort
        double [][] y_shaker=handle_one_algorithm(flowDurations,sorted,reversed,input_sizes,"shaker");
        showAndSaveChart2("ShakerSort",inputAxis,y_shaker);
        //ShellSort
        double [][] y_shell=handle_one_algorithm(flowDurations,sorted,reversed,input_sizes,"shell");
        showAndSaveChart2("ShellSort",inputAxis,y_shell);
        //RadixSort
        double [][] y_radix=handle_one_algorithm(flowDurations,sorted,reversed,input_sizes,"radix");
        showAndSaveChart2("RadixSort",inputAxis,y_radix);



        // Random Dataset For all Algorithm
        double [][] Random=handle(flowDurations,input_sizes,algorithms);
        //print(Random);
        showAndSaveChart("Random",inputAxis,Random);



        // Sorted Dataset For all Algorithm
        double [][] Sorted=handle(sorted,input_sizes,algorithms);
        //print(Sorted);
        showAndSaveChart("Sorted",inputAxis,Sorted);

        // Reversed Dataset For all Algorithm
        double [][] reverse=handle(reversed,input_sizes,algorithms);
        //print(reverse);
        showAndSaveChart("Reversed",inputAxis,reverse);

    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Comb", doubleX, yAxis[0]);
        chart.addSeries("Insertion", doubleX, yAxis[1]);
        chart.addSeries("Shaker", doubleX, yAxis[2]);
        chart.addSeries("Shell", doubleX, yAxis[3]);
        chart.addSeries("Radix", doubleX, yAxis[4]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }


public static void showAndSaveChart2(String title, int[] xAxis, double[][] yAxis) throws IOException {
    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
            .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

    // Convert x axis to double[]
    double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);



    // Add a plot for a sorting algorithm
    chart.addSeries("Random", doubleX, yAxis[0]);
    chart.addSeries("Sorted", doubleX, yAxis[1]);
    chart.addSeries("Reversed", doubleX, yAxis[2]);


    // Save the chart as PNG
    BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

    // Show the chart
    new SwingWrapper(chart).displayChart();
}
}
