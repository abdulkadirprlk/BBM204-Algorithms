import java.io.IOException;

public class Main {
    public static void insertionSort(int[] arr){
        for (int i = 1; i < arr.length; i++) {
            int keyValue = arr[i];
            int j = i - 1;
            while(j >= 0 && arr[j] > keyValue){
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = keyValue;
        }
    }
    public static int[] mergeSort(int[] arr){
        int n = arr.length;
        if(n <= 1){// base case
            return arr;
        }
        int midIndex = n / 2;
        int[] leftArray = new int[midIndex];
        int[] rightArray = new int[n - midIndex];

        for (int i = 0; i < midIndex; i++) {
            leftArray[i] = arr[i];
        }
        for(int i = midIndex; i < n; i++){
            rightArray[i - midIndex] = arr[i];
        }

        leftArray = mergeSort(leftArray);
        rightArray = mergeSort(rightArray);

        return mergeArrays(leftArray, rightArray);
    }
    private static int[] mergeArrays(int[] leftArray, int[] rightArray){
        int leftSize = leftArray.length;
        int rightSize = rightArray.length;

        int[] finalArr = new int[leftSize + rightSize];

        int i = 0, j = 0, k = 0;
        while(i < leftSize && j < rightSize){
            if (leftArray[i] <= rightArray[j]) {
                finalArr[k] = leftArray[i];
                i++;
            }
            else {
                finalArr[k] = rightArray[j];
                j++;
            }
            k++;
        }
        while(i < leftSize){
            finalArr[k] = leftArray[i];
            i++;
            k++;
        }
        while(j < rightSize){
            finalArr[k] = rightArray[j];
            j++;
            k++;
        }
        return finalArr;
    }
    public static int[] countingSort(int[] input) {
        int k = getMax(input, input.length);

        int[] count = new int[k + 1];
        int[] output = new int[input.length];

        // Count occurrences of each element
        for (int i = 0; i < input.length; i++) {
            int j = input[i];
            count[j]++;
        }

        // Accumulate count
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (int i = input.length - 1; i >= 0; i--) {
            int j = input[i];
            count[j]--;
            output[count[j]] = input[i];
        }

        return output;
    }

    // Function to return the maximum element from the array
    private static int getMax(int arr[], int n)
    {
        int res = arr[0];
        for (int i = 1; i < n; i++)
            res = Math.max(res, arr[i]);
        return res;
    }

    public static int linearSearch(int[] input, int key){
        for (int i = 0; i < input.length; i++) {
            if(input[i] == key){
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] sortedArr, int key){
        int left = 0;
        int right = sortedArr.length - 1;
        while(left <= right){
            int mid = (left + right) / 2;
            if(sortedArr[mid] == key){
                return mid;
            }
            if(sortedArr[mid] < key){
                left = mid + 1;
            }
            else{
                right = mid - 1;
            }
        }
        return -1;
    }

    private static int binarySearchRecursive(int[] sortedArr,int left, int right, int key){
        int mid = (left + right) / 2;
        if(sortedArr[mid] == key){
            return mid;
        }
        if(left > right){
            return -1;
        }
        if(key > sortedArr[mid]){
            return binarySearchRecursive(sortedArr, mid + 1, right, key);
        }
        else{
            return binarySearchRecursive(sortedArr, left, mid - 1, key);
        }
    }

    public static void main(String[] args) throws IOException{

    }
}