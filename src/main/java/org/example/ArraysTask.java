package org.example;

import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

/**
 * Задание 1, вариант 1. Найти максимальное значение по модулю в массиве.
 * Входные данные: количество элементов в формируемом массиве n [0, ∞).
 * Выходные данные: в консоль напечатаны массив и результат поиска в соответствии с заданием.
 */
public class ArraysTask {


    /**
     * Нахождение максимального по модулю числа в массиве arr
     */
    public static int maxAbsoluteNumber(int[] arr) {

        int maxAbsValue = 0;
        for (int j : arr) {
            if (Math.abs(j) > Math.abs(maxAbsValue)) {
                maxAbsValue = j;
            }
        }
        return maxAbsValue;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число элементов массива: ");
        int n = scanner.nextInt();

        int[] numbers = new Random()
                .ints(n, -99, 100)
                .toArray();

        System.out.println("Исходный массив:");
        System.out.println(Arrays.toString(numbers));

        System.out.println("Число с максимальным значением по модулю:");
        System.out.println(maxAbsoluteNumber(numbers));
    }
}
