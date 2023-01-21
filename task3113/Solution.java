package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* 
Что внутри папки?
*/

public class Solution {

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            Path folder = Paths.get(reader.readLine());

            if (!Files.isDirectory(folder)) System.out.println(folder + " - не папка.");
            else {
                AtomicInteger folderCounter = new AtomicInteger();
                AtomicInteger fileCounter = new AtomicInteger();
                AtomicLong sizeCounter = new AtomicLong();

                Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (!dir.equals(folder)) folderCounter.incrementAndGet();
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        fileCounter.incrementAndGet();
                        sizeCounter.addAndGet(attrs.size());
                        return FileVisitResult.CONTINUE;
                    }
                });

                System.out.println("Параметры папки " + folder + ":");
                System.out.println("Всего папок - " + folderCounter.get());
                System.out.println("Всего файлов - " + fileCounter.get());
                System.out.println("Общий размер - " + sizeCounter.get() + " байт");
            }
        }
    }
}
