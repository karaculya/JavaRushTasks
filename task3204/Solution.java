package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/* 
Генератор паролей
*/

public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password);
    }

    public static ByteArrayOutputStream getPassword(){
        ByteArrayOutputStream passwordStream = new ByteArrayOutputStream();
        char[] symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] pass = new char[8];
        Random random = new Random();
        String password = "";
        while (!(password.matches(".*\\d.*") && password.matches(".*[a-z].*")
                && password.matches(".*[A-Z].*"))){
            for (int i = 0; i < pass.length; i++) pass[i] = symbols[random.nextInt(symbols.length)];
            password = String.copyValueOf(pass);
        }
        try {
            passwordStream.write(password.getBytes());
        } catch (IOException e) {
            System.out.println("Не удалось сгенирировать пароль");
        }
        return passwordStream;
    }
}
