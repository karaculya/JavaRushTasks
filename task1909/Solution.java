package com.javarush.task.task19.task1909;

import java.io.*;
import java.util.ArrayList;

/* 
Замена знаков
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        String inputFile;
        String outputFile;

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            inputFile = bufferedReader.readLine();
            outputFile = bufferedReader.readLine();
        }

        ArrayList<String> fileContent = new ArrayList<>();
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile))) {
            while (inputFileReader.ready()) {
                fileContent.add(inputFileReader.readLine());
            }
        }
        try (BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile))) {
            for (String s : fileContent) {
                outputFileWriter.write(s.replaceAll("\\.", "!"));
            }
        }
    }
}
