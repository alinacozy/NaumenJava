package org.example;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Задание 3, вариант 1. Отфильтровать сотрудников, оставив только тех, кто старше 30 лет. (Stream API)
 * Выходные данные: в консоль напечатан результат выполнения задания.
 * Объекты должны быть напечатаны в читаемом виде.
 */
public class EmployeeTask {
    public static void main(String[] args){
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee(
                "Иван Иванович Иванов",
                38,
                "IT",
                200000.0)
        );
        employees.add(new Employee(
                "Пётр Петрович Петров",
                67,
                "IT",
                676767.0)
        );
        employees.add(new Employee(
                "Алина Вячеславовна Королева",
                21,
                "IT",
                80000.0)
        );
        employees.add(new Employee(
                "Кирилл Чудомирович Флинс",
                100,
                "Lightkeepers",
                1000000.0)
        );
        employees.add(new Employee(
                "Виктор Талис",
                30,
                "Laboratory",
                50000.0)
        );
        employees.add(new Employee(
                "Джейс Талис",
                28,
                "Laboratory",
                60000.0)
        );

        // фильтр с помощью Stream API
        ArrayList<Employee> filteredEmployees = employees.stream()
                .filter(empl -> empl.getAge()>30)
                .collect(Collectors.toCollection(ArrayList<Employee>::new));

        System.out.println("Сотрудники, которые старше 30:");
        filteredEmployees.forEach(System.out::println);
    }
}
