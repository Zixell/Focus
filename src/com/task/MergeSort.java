package com.task;

import java.util.Arrays;

public class MergeSort {
    public static String[] sortAsString(String[] strings, boolean asc){
        sortStrings(strings, 0, strings.length - 1);
        return strings;
    }

    public static String[] sortAsInts(String[] strings, boolean asc){
        int[] array = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
        sortInts(array, array.length);
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
    }

    private static void sortInts(int arr[], int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        int[] l = new int[mid];
        int[] r = new int[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = arr[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = arr[i];
        }
        sortInts(l, mid);
        sortInts(r, n - mid);

        mergeInts(arr, l, r, mid, n - mid);
    }

    private static void mergeInts(int[] a, int[] l, int[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i] <= r[j]) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

    private static void sortStrings(String[] a, int from, int to) {
        if (from == to) {
            return;
        }
        int mid = (from + to) / 2;
        // sort the first and the second half
        sortStrings(a, from, mid);
        sortStrings(a, mid + 1, to);
        mergeStrings(a, from, mid, to);
    }

    private static void mergeStrings(String[] a, int from, int mid, int to) {
        int n = to - from + 1;       // size of the range to be merged
        String[] b = new String[n];   // merge both halves into a temporary array b
        int i1 = from;               // next element to consider in the first range
        int i2 = mid + 1;            // next element to consider in the second range
        int j = 0;                   // next open position in b

        // as long as neither i1 nor i2 past the end, move the smaller into b
        while (i1 <= mid && i2 <= to) {
            if (a[i1].compareTo(a[i2]) < 0) {
                b[j] = a[i1];
                i1++;
            } else {
                b[j] = a[i2];
                i2++;
            }
            j++;
        }

        // note that only one of the two while loops below is executed
        // copy any remaining entries of the first half
        while (i1 <= mid) {
            b[j] = a[i1];
            i1++;
            j++;
        }

        // copy any remaining entries of the second half
        while (i2 <= to) {
            b[j] = a[i2];
            i2++;
            j++;
        }

        // copy back from the temporary array
        for (j = 0; j < n; j++) {
            a[from + j] = b[j];
        }
    }
}
