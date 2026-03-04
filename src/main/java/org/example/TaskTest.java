package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Задание 5, вариант 3.
 * тестовый класс для проверки реализации интерфейса
 */
public class TaskTest {

    /**
     * Подготовка тестовых папок, создание файлового дерева для теста
     */
    private static void prepareTestFolders(String sourceFolder, String targetFolder) {
        try {
            Path sourcePath = Paths.get(sourceFolder);
            Path targetPath = Paths.get(targetFolder);

            // Удаляем старые папки, если они существуют
            System.out.println("Очистка старых папок...");
            deleteDirectory(sourcePath);
            deleteDirectory(targetPath);

            // Создаем новую исходную папку
            Files.createDirectories(sourcePath);
            System.out.println("Создана папка: " + sourcePath);

            // Создаем тестовые файлы
            System.out.println("Создание тестовых файлов...");

            // Создаем 20 файлов в корне
            for (int i = 1; i <= 20; i++) {
                Path filePath = sourcePath.resolve("file" + i + ".txt");
                String content = "Это содержимое файла №" + i + "\nСтрока 2\nСтрока 3";
                Files.writeString(filePath, content);
            }
            System.out.println("Создано 20 файлов в корневой папке");

            // Создаем вложенную структуру папок
            Path subDir1 = sourcePath.resolve("subfolder1");
            Path subDir2 = sourcePath.resolve("subfolder2");
            Path subSubDir = subDir1.resolve("subsubfolder");

            Files.createDirectories(subDir1);
            Files.createDirectories(subDir2);
            Files.createDirectories(subSubDir);

            // Добавляем файлы во вложенные папки
            for (int i = 1; i <= 5; i++) {
                // Файлы в subfolder1
                Path file1 = subDir1.resolve("sub1_file" + i + ".dat");
                Files.writeString(file1, "Данные в subfolder1, файл " + i);

                // Файлы в subfolder2
                Path file2 = subDir2.resolve("sub2_file" + i + ".txt");
                Files.writeString(file2, "Данные в subfolder2, файл " + i);

                // Файлы в subsubfolder
                Path file3 = subSubDir.resolve("subsub_file" + i + ".log");
                Files.writeString(file3, "Лог-запись #" + i + " во вложенной папке");
            }
            System.out.println("Создана структура вложенных папок с файлами");

            // Создаем файлы разных размеров
            Path smallFile = sourcePath.resolve("small_file.bin");
            Files.write(smallFile, new byte[1024]); // 1 KB

            Path mediumFile = sourcePath.resolve("medium_file.bin");
            Files.write(mediumFile, new byte[10 * 1024]); // 10 KB

            System.out.println("Созданы файлы разных размеров");

            // Подсчитываем общее количество созданных файлов
            long totalFiles = Files.walk(sourcePath)
                    .filter(Files::isRegularFile)
                    .count();

            System.out.println("Всего создано файлов в исходной папке: " + totalFiles);

            // добавляем лишние файлы в целевую папку
            Files.createDirectories(targetPath);
            System.out.println("Создана папка: " + targetPath);

            // Создаем лишние файлы в корне целевой папки
            for (int i = 1; i <= 5; i++) {
                Path extraFile = targetPath.resolve("extra_file_" + i + ".txt");
                Files.writeString(extraFile, "Это лишний файл №" + i + " в целевой папке");
            }
            System.out.println("Создано 5 лишних файлов в корне целевой папки");

        } catch (IOException e) {
            System.err.println("Ошибка при подготовке тестовых папок: " + e.getMessage());
        }
    }


    /**
     * Удаление существующих тестовых папок
     */
    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("Удалена папка: " + path);
        }
    }

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        String sourceFolder = baseDir + "\\test_src_folder";
        String targetFolder = baseDir + "\\test_target_folder";

        System.out.println("=== Создаем файлы в исходной папке ===");
        prepareTestFolders(sourceFolder, targetFolder);
        System.out.println("=== Файлы созданы ===");

        // Создаем экземпляр задачи
        Task fileSyncTask = new TaskImpl(sourceFolder, targetFolder);

        // Запускаем синхронизацию
        System.out.println("=== Запуск синхронизации ===");
        fileSyncTask.start();

        try {
            Thread.sleep(10); // ждем немного чтобы синхронизация успела начаться, но не успела закончиться
        } catch (InterruptedException e) {
            System.err.println("Ошибка прерывания: " + e.getMessage());
        }
        // Пробуем остановить
        System.out.println("\n=== Попытка остановки ===");
        fileSyncTask.stop();
    }

}
