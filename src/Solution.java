package com.javarush.task.task18.task1828;

/*
Прайсы 2
*/

import java.io.*;
import java.util.ArrayList;

public class Solution {
    public static class Product {
        int id;
        String nameProduct;
        String priceProduct;
        String quantityProduct;

        public Product(int id, String nameProduct, String priceProduct, String quantityProduct) {
            this.id = id;
            this.nameProduct = nameProduct;
            this.priceProduct = priceProduct;
            this.quantityProduct = quantityProduct;
        }

        public String toString() {
            return String.format("%-8d%-30s%-8s%-4s", id, nameProduct, priceProduct, quantityProduct);
        }

        public static Product getProduct(String string) {
            String id = string.substring(0, 8).trim();
            String nameProduct = string.substring(8, 38).trim();
            String priceProduct = string.substring(38, 46).trim();
            String quantityProduct = string.substring(46, 50).trim();
            return new Product(Integer.parseInt(id), nameProduct, priceProduct, quantityProduct);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) return;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();

        ArrayList<Product> products = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while (bufferedReader.ready()) {
                Product product = Product.getProduct(bufferedReader.readLine());
                products.add(product);
            }
        }

        switch (args[0]) {
            case "-c": {
                int id = 0;
                for (Product product : products) {
                    if (product.id > id) id = product.id;
                }
                String nameProduct = "";
                for (int i = 1; i < args.length - 2; i++) {
                    nameProduct += args[i] + " ";
                }
                if (nameProduct.length() > 30) {
                    nameProduct = nameProduct.substring(0, 30);
                }
                String priceProduct = args[args.length - 2];
                if (priceProduct.length() > 8) {
                    priceProduct = priceProduct.substring(0, 8);
                }
                String quantityProduct = args[args.length - 1];
                if (quantityProduct.length() > 4) {
                    quantityProduct = quantityProduct.substring(0, 4);
                }
                Product product = new Product(++id, nameProduct.trim(), priceProduct, quantityProduct);
            }

            case "-u": {
                int id = Integer.parseInt(args[1]);
                String nameProduct = "";
                for (int i = 2; i < args.length - 2; i++) {
                    nameProduct += args[i] + " ";
                }
                if (nameProduct.length() > 30) {
                    nameProduct = nameProduct.substring(0, 30);
                }
                String priceProduct = args[args.length - 2];
                if (priceProduct.length() > 8) {
                    priceProduct = priceProduct.substring(0, 8);
                }
                String quantityProduct = args[args.length - 1];
                if (quantityProduct.length() > 4) {
                    quantityProduct = quantityProduct.substring(0, 4);
                }
                Product productToUpdate = null;
                for (Product product : products)
                    if (product.id == id) productToUpdate = product;
                if (productToUpdate != null) {
                    productToUpdate.nameProduct = nameProduct;
                    productToUpdate.priceProduct = priceProduct;
                    productToUpdate.quantityProduct = quantityProduct;
                }
                break;
            }

            case "-d": {
                int id = Integer.parseInt(args[1]);
                Product productToDelete = null;
                for (Product product : products)
                    if (product.id == id) productToDelete = product;
                if (productToDelete != null) products.remove(productToDelete);
                break;
            }
        }
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (Product product : products) {
                fileWriter.write(product.toString());
                fileWriter.write("\n");
            }
        }
    }
}
