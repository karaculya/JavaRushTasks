package com.javarush.task.task19.task1910;

import java.io.*;
import java.util.ArrayList;

/* 
Пунктуация
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        String inputFileName;
        String outputFileName;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            inputFileName = br.readLine();
            outputFileName = br.readLine();
        }

        ArrayList<String> fileContent = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFileName))){
            while (reader.ready()) {
                fileContent.add(reader.readLine());
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String s: fileContent) {
                writer.write(s.replaceAll("\\p{Punct}", ""));
            }
        }
    }
}
