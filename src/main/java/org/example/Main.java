package org.example;

import java.util.Scanner;

public class Main {
    private static void printMenu() {
        System.out.println("\n=================================");
        System.out.println("      Выполненные задания");
        System.out.println("=================================");
        System.out.println("1. Задание 1 (работа с массивом)");
        System.out.println("2. Задание 2 (работа со списками)");
        System.out.println("3. Задание 3 (Stream API)");
        System.out.println("4. Задание 4 (HTTP клиент и JSON)");
        System.out.println("5. Задание 5 (Синхронизация папок)");
        System.out.println("=================================");
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();

            System.out.print("Выберите задание (1-5) или 0 для выхода: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n=== Запуск задания 1 ===");
                    ArraysTask.main(new String[0]);
                    break;

                case "2":
                    System.out.println("\n=== Запуск задания 2 ===");
                    ListTask.main(new String[0]);
                    break;

                case "3":
                    System.out.println("\n=== Запуск задания 3 ===");
                    EmployeeTask.main(new String[0]);
                    break;

                case "4":
                    System.out.println("\n=== Запуск задания 4 ===");
                    HttpTask.main(new String[0]);
                    break;

                case "5":
                    System.out.println("\n=== Запуск задания 5 ===");
                    TaskTest.main(new String[0]);
                    break;

                case "0":
                    System.out.println("Выход из программы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }

            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }
}