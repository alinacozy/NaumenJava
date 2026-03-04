package org.example;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Задание 2, вариант 1. Сортировка слиянием (Merge Sort).
 * Входные данные: количество элементов в формируемом массиве n [0, ∞).
 * Выходные данные: в консоль напечатаны исходный и отсортированный списки.
 */
public class ListTask {

    /**
     * Слияние двух отсортированных частей массива:
     * 1 часть: [left, mid)
     * 2 часть: [mid, right)
     * В результате в списке list отсортированная часть [left, right)
     */
    public static void merge(ArrayList<Double> list, int left, int mid, int right){
        int it1=0;
        int it2=0;
        Double[] result=new Double[right-left]; // массив для временной записи результата слияния

        while (left+it1<mid && mid+it2<right){
            if (list.get(left+it1)<list.get(mid+it2)){
                result[it1+it2]=list.get(left+it1);
                it1++;
            }
            else{
                result[it1+it2]=list.get(mid+it2);
                it2++;
            }
        }

        while (left+it1<mid){
            result[it1+it2]=list.get(left+it1);
            it1++;
        }

        while (mid+it2<right){
            result[it1+it2]=list.get(mid+it2);
            it2++;
        }

        for (int i=0; i<it1+it2; i++){
            list.set(left+i, result[i]);
        }

    }


    /**
     * Итеративный алгоритм сортировки слиянием
     */
    public static ArrayList<Double> merge_sort_iterative(ArrayList<Double> list) {
        int n=list.size();
        // итеративный алгоритм сортировки слиянием
        for (int i=1; i<n; i*=2){
            for (int j=0; j<n-i; j+=2*i){
                merge(list, j, j+i, Math.min(j+2*i, n));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число элементов в списке: ");
        int n = scanner.nextInt();

        ArrayList<Double> numbers = new Random()
                .doubles(n, -99, 100)
                .limit(n)
                .boxed()   // преобразует DoubleStream в Stream<Double>
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Исходный список:");
        System.out.println(numbers);

        System.out.println("Отсортированный список:");
        System.out.println(merge_sort_iterative(numbers));
    }
}
