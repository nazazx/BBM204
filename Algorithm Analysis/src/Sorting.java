public class Sorting {

    public static void comb(int arr[]){
        int l=arr.length;
        int gap=l;
        double shrink=1.3;
        boolean sorted=false;

        while (sorted==false){
            gap= (int ) Math.floor((gap/shrink));
            if (gap<1){
                gap=1;
            }
            if (gap==1){
                sorted=true;
            }
            for (int i=0;i<l-gap;i++){
                if (arr[i]>arr[i+gap]){
                    int temp=arr[i];
                    arr[i]=arr[i+gap];
                    arr[i+gap]=temp;
                    sorted=false;
                }
            }
        }
    }

    public static void insertion(int arr[]){
        int l=arr.length;
        for (int i=1;i<l;i++){
            int key=arr[i];
            int j=i-1;

            while (j>=0 && arr[j]>key){
                arr[j+1]=arr[j];
                j--;
            }
            arr[j+1]=key;
        }
    }
    public static void ShakerSort(int[] arr) {
        int l=arr.length;
        boolean swap= true;

        while (swap==true) {
            swap = false;

            for (int i = 0; i < l-1; i++) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swap = true;
                }
            }

            if (swap==false){
                break;
            }

            swap = false;

            for (int i = l-2; i >= 0; i--) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swap = true;

                }
            }
        }
    }


    public static void shellSort(int arr[]){
        int l=arr.length;
        int gap=l/2;
        while (gap>0){
            for (int i=gap;i<l;i++){
                int temp=arr[i];
                int j=i;
                while ( j>=gap && arr[j-gap]>temp){
                    arr[j]=arr[j-gap];
                    j-=gap;
                }
                arr[j]=temp;
            }
            gap=gap/2;
        }
    }

    public static int get_digit(int n, int index) {
        if (index < 0){
            return 0;
        }
        int divisor=1;
        for (int i=0;i<index;i++){
            divisor*=10;
        }
        return (n / divisor) % 10;
    }

    public static void countingSort(int arr[],int pos){
        int l=arr.length;
        int count[] =new int[10];
        int output[]=new int[l];

        for (int i=0;i<l;i++){
            int digit=get_digit(arr[i],pos);
            count[digit]++;
        }
        for (int i=1;i<10;i++){
            count[i]=count[i]+count[i-1];
        }
        for (int i=l-1;i>=0;i--){
            int digit=get_digit(arr[i],pos);
                count[digit]--;
                output[count[digit]]=arr[i];
        }
        System.arraycopy(output, 0, arr, 0, l);
    }

    public static void radixSort(int arr[]){
        int maxi=0;
        for (int number : arr){
            maxi=Math.max(maxi,number);
        }
        int d = (int)Math.log10(maxi) + 1;
        for (int i=0;i<d;i++){
            countingSort(arr,i);
        }
    }
}
