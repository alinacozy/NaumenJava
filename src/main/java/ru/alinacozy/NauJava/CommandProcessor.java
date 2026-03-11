package ru.alinacozy.NauJava;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.service.FlossService;

@Component
public class CommandProcessor {

    private final FlossService flossService;
    private final MeterRegistry meterRegistry;

    @Autowired
    public CommandProcessor(FlossService flossService, MeterRegistry meterRegistry) {
        this.flossService = flossService;
        this.meterRegistry = meterRegistry;
    }

    private void printMetrics() {
        System.out.println("\n=== МЕТРИКИ ===");

        // JVM Memory
        var memoryUsed = meterRegistry.find("jvm.memory.used").meter();
        if (memoryUsed != null) {
            memoryUsed.measure().forEach(m ->
                    System.out.println("Память: " + m.getValue() + " байт"));
        }

        // CPU
        var cpuUsage = meterRegistry.find("system.cpu.usage").meter();
        if (cpuUsage != null) {
            cpuUsage.measure().forEach(m ->
                    System.out.println("CPU: " + (m.getValue() * 100) + "%"));
        }

        // Потоки
        var threads = meterRegistry.find("jvm.threads.live").meter();
        if (threads != null) {
            threads.measure().forEach(m ->
                    System.out.println("Потоки: " + m.getValue()));
        }

        System.out.println("================\n");
    }

    private void initMetrics() {
        System.out.println("Инициализация метрик...");
        meterRegistry.get("jvm.memory.used").meters();
        meterRegistry.get("jvm.threads.live").meters();
        meterRegistry.get("system.cpu.usage").meters();
    }

    public void processCommand(String input) {
        String[] cmd = input.split(" ");

        switch (cmd[0]) {
            case "create" -> {
                // create <brand> <number> <colorName> <colorGroup> <red> <green> <blue>
                // Пример: create DMC 310 Черный Нейтральные 0 0 0

                if (cmd.length < 8) {
                    System.out.println("Ошибка: команда create требует 7 аргументов: бренд, номер, название цвета, цветовая группа, red, green, blue");
                    break;
                }
                System.out.println("Создание новой нитки...");
                String brand = cmd[1];
                String number = cmd[2];
                String colorName = cmd[3];
                String colorGroup = cmd[4];
                int red = Integer.parseInt(cmd[5]);
                int green = Integer.parseInt(cmd[6]);
                int blue = Integer.parseInt(cmd[7]);
                flossService.createFloss(brand, number, colorName, colorGroup, red, green, blue);
                System.out.println("Нитка успешно создана");
            }

            case "find" -> {
                // find <brand> <number>
                // Пример: find DMC 310
                if (cmd.length < 3) {
                    System.out.println("Ошибка: команда find требует 2 аргумента: бренд и номер");
                    break;
                }
                System.out.println("Поиск нитки по бренду и номеру...");
                try {
                    String brand = cmd[1];
                    String number = cmd[2];
                    Floss floss = flossService.findByBrandAndNumber(brand, number);
                    if (floss == null){
                        System.out.println("Нитка с таким номером и брендом не найдена");
                    }
                    else {
                        System.out.println("Найдена нитка:");
                        System.out.println("  ID: " + floss.getId());
                        System.out.println("  Бренд: " + floss.getBrand());
                        System.out.println("  Номер: " + floss.getColorNumber());
                        System.out.println("  Название: " + floss.getColorName());
                        System.out.println("  Группа: " + floss.getColorGroup());
                        System.out.println("  RGB: (" + floss.getRed() + ", " + floss.getGreen() + ", " + floss.getBlue() + ")");
                        System.out.println("  HEX: " + floss.getHexCode());
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            case "delete" -> {
                // delete id
                // Пример: delete 5
                if (cmd.length < 2) {
                    System.out.println("Ошибка: команда delete требует аргумент: id");
                    break;
                }
                System.out.println("Удаление нитки по ID...");
                try {
                    Long id = Long.valueOf(cmd[1]);
                    flossService.deleteById(id);
                    System.out.println("Удалена нитка с ID "+id);
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            case "update" -> {
                // update id красный зеленый синий
                // Пример: update 5 255 0 0
                if (cmd.length < 5) {
                    System.out.println("Ошибка: команда update требует 4 аргумента: id, red, green, blue");
                    break;
                }
                System.out.println("Обновление RGB-аналога нитки...");
                try{
                    Long id = Long.valueOf(cmd[1]);
                    int red = Integer.parseInt(cmd[2]);
                    int green = Integer.parseInt(cmd[3]);
                    int blue = Integer.parseInt(cmd[4]);
                    flossService.updateRGB(id, red, green, blue);
                    System.out.println("RGB-аналог нитки обновлен");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }

            }

            case "findbycolor" -> {
                // findbycolor красный зеленый синий
                // Пример: findbycolor 200 50 50

                if (cmd.length < 4) {
                    System.out.println("Ошибка: команда findbycolor требует 3 аргумента: red, green, blue");
                    break;
                }
                System.out.println("Поиск ближайшего цвета по RGB...");
                try {
                    int red = Integer.parseInt(cmd[1]);
                    int green = Integer.parseInt(cmd[2]);
                    int blue = Integer.parseInt(cmd[3]);
                    Floss floss = flossService.findByColor(red, green, blue);
                    if (floss == null){
                        System.out.println("Похожая нитка не найдена");
                    }
                    else {
                        System.out.println("Найдена похожая нитка:");
                        System.out.println("  ID: " + floss.getId());
                        System.out.println("  Бренд: " + floss.getBrand());
                        System.out.println("  Номер: " + floss.getColorNumber());
                        System.out.println("  Название: " + floss.getColorName());
                        System.out.println("  Группа: " + floss.getColorGroup());
                        System.out.println("  RGB: (" + floss.getRed() + ", " + floss.getGreen() + ", " + floss.getBlue() + ")");
                        System.out.println("  HEX: " + floss.getHexCode());
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            case "similar" -> {
                // similar бренд номер
                // Пример: similar DMC 310
                if (cmd.length < 3) {
                    System.out.println("Ошибка: команда similar требует 2 аргумента: бренд и номер");
                    break;
                }
                System.out.println("Поиск похожей нитки...");
                try {
                    String brand = cmd[1];
                    String number = cmd[2];
                    Floss floss = flossService.findSimilar(brand, number);
                    if (floss == null){
                        System.out.println("Нитка с таким номером и брендом не найдена");
                    }
                    else {
                        System.out.println("Найдена нитка:");
                        System.out.println("  ID: " + floss.getId());
                        System.out.println("  Бренд: " + floss.getBrand());
                        System.out.println("  Номер: " + floss.getColorNumber());
                        System.out.println("  Название: " + floss.getColorName());
                        System.out.println("  Группа: " + floss.getColorGroup());
                        System.out.println("  RGB: (" + floss.getRed() + ", " + floss.getGreen() + ", " + floss.getBlue() + ")");
                        System.out.println("  HEX: " + floss.getHexCode());
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            case "metrics" -> {
                printMetrics();
            }

            case "help" -> {
                printHelp();
            }

            default -> System.out.println("Введена неизвестная команда. Введите 'help' для списка команд.");
        }
    }

    private void printHelp() {
        System.out.println("\n=== Доступные команды ===");
        System.out.println("create <бренд> <номер> <название> <группа> <r> <g> <b> - добавить новую нитку");
        System.out.println("find <бренд> <номер> - найти нитку по бренду и номеру");
        System.out.println("delete <id> - удалить нитку по ID");
        System.out.println("update <id> <r> <g> <b> - обновить RGB-аналог нитки по ID");
        System.out.println("findbycolor <r> <g> <b> - найти ближайший цвет по RGB");
        System.out.println("similar <бренд> <номер> - найти похожую нитку");
        System.out.println("metrics - показать метрики приложения");
        System.out.println("help - показать список команд");
        System.out.println("exit - выход из программы");
        System.out.println("==========================\n");
    }
}
