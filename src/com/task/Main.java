package com.task;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static boolean ascSort = true;
    private static boolean isStringType;
    private static List<String> fileNames = new ArrayList<>();

    public static void main(String[] args) {
        try {
            parseArgs(args);
            sortFiles(fileNames);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void parseArgs(String[] args) {
        if (args.length < 3) {
            alert("Wrong amount of arguments");
        }

        String firstArg = args[0];
        int pos = 1;

        if (firstArg.equals("-a") || firstArg.equals("-d")) {
            ascSort = firstArg.equals("-a");
            String secondArg = args[1];
            if (!secondArg.equals("-s") && !secondArg.equals("-i")) {
                alert("Wrong second argument.");
            }
            isStringType = args[1].equals("-s");
            pos = 2;
        } else if (firstArg.equals("-s") || firstArg.equals("-i")){
            isStringType = firstArg.equals("-s");
        } else {
            alert("Wrong arguments. Expected -a/-d or -s/-i");
        }

        if (args.length - pos < 2) {
            alert("Wrong amount of input files");
        }

        fileNames.addAll(Arrays.asList(args).subList(pos, args.length));
    }

    private static void sortFiles(List<String> fileNames) throws IOException {
        InputStream firstInputStream = getInputStream(fileNames.get(0));
        String[] currentArray = readFromInputStream(firstInputStream);

        for (int i = 1; i < fileNames.size(); i++) {
            InputStream inputStream = getInputStream(fileNames.get(i));
            String[] stringValues = readFromInputStream(inputStream);

            String[] concatenate = concatenate(currentArray, stringValues);

            currentArray = isStringType
                    ? MergeSort.sortAsString(concatenate, ascSort)
                    : MergeSort.sortAsInts(concatenate, ascSort);
        }

        if (!ascSort) {
            invert(currentArray);
        }

        writeToResultFile(currentArray);
    }

    private static InputStream getInputStream(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        if (Runtime.getRuntime().freeMemory() < file.length()) {
            alert("Not enough free heap memory space!");
        }
        return new FileInputStream(file);
    }

    private static String[] readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString().split("\n");
    }

    private static void writeToResultFile(String[] currentArray) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt"))) {
            for (String str : currentArray) {
                writer.write(str + "\n");
            }
        } catch (IOException e) {
            alert(e.getMessage());
        }
    }

    private static void alert(String message) {
        throw new RuntimeException(message);
    }

    private static  <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    private static void invert(String[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            String temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
