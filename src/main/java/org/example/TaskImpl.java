package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание 5, вариант 3.
 * Реализуйте интерфейс “Task” для синхронизации файлов между
 * двумя заданными папками. Каждый синхронизированный файл логируется
 * в консоль. Метод start() начинает процесс синхронизации, а stop()
 * останавливает его. Для выполнения задания рекомендуется использовать
 * классы из пакета “java.nio.file”.
 */
public class TaskImpl implements Task {

    private Path sourceDir;
    private Path targetDir;
    private volatile boolean isRunning = false;
    private List<Path> synchronizedFiles;
    private Thread syncThread;

    public TaskImpl(String sourceDir, String targetDir) {
        this.sourceDir = Paths.get(sourceDir);
        this.targetDir = Paths.get(targetDir);
        this.synchronizedFiles = new ArrayList<>();
    }

    private void deleteExtraFilesInTarget() throws IOException {
        Files.walkFileTree(targetDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path targetFile, BasicFileAttributes attrs) {
                if (!isRunning) return FileVisitResult.TERMINATE;

                try {
                    Path relativePath = targetDir.relativize(targetFile);
                    Path sourceFile = sourceDir.resolve(relativePath);

                    // Если в исходной папке такого файла нет - удаляем
                    if (!Files.exists(sourceFile)) {
                        Files.delete(targetFile);
                        System.out.println("Удален лишний файл в целевой папке: " + relativePath);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при удалении: " + e.getMessage());
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                // Удаляем пустые папки
                if (Files.list(dir).findAny().isEmpty()) {
                    Files.delete(dir);
                    System.out.println("Удалена пустая папка: " + targetDir.relativize(dir));
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void copyFiles() throws IOException {
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) {
                if (!isRunning) {
                    return FileVisitResult.TERMINATE; // Прерываем, если синхронизация остановлена
                }

                try {
                    // Вычисляем относительный путь от исходной папки
                    Path relativePath = sourceDir.relativize(sourceFile);
                    // Формируем путь к файлу в целевой папке
                    Path targetFile = targetDir.resolve(relativePath);

                    // Создаем вложенные папки в целевой директории, если нужно
                    Files.createDirectories(targetFile.getParent());

                    // Копируем файл (с заменой существующего)
                    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);

                    // Логируем синхронизированный файл
                    System.out.println("Синхронизирован: " + relativePath);
                    synchronizedFiles.add(relativePath);

                } catch (IOException e) {
                    System.err.println("Ошибка при копировании " + sourceFile + ": " + e.getMessage());
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("Ошибка доступа к файлу: " + file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void start() {
        if (isRunning) {
            System.out.println("Синхронизация уже запущена");
            return;
        }

        // Проверяем, существуют ли папки
        if (!Files.exists(sourceDir)) {
            System.err.println("Исходная папка не существует: " + sourceDir);
            return;
        }

        try {
            // Создаем целевую папку, если её нет
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
                System.out.println("Создана целевая папка: " + targetDir);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при синхронизации: " + e.getMessage());
        }

        isRunning = true;
        synchronizedFiles.clear();

        syncThread = new Thread(() -> {
            try {
                System.out.println("Запуск синхронизации...");
                copyFiles();
                deleteExtraFilesInTarget();

            } catch (IOException e) {
                System.err.println("Ошибка при синхронизации: " + e.getMessage());
            } finally {
                isRunning=false;
            }
            System.out.println("Поток синхронизации завершен");
        });


        syncThread.start();
        System.out.println("Синхронизация запущена в отдельном потоке");
    }

    @Override
    public void stop() {
        if (!isRunning || syncThread == null) {
            System.out.println("Синхронизация не запущена");
            return;
        }

        System.out.println("Остановка синхронизации...");
        isRunning = false;

        // Прерываем поток, если он в ожидании
        syncThread.interrupt();

        try {
            // Ждем завершения потока
            syncThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Синхронизация остановлена. Синхронизировано файлов: " + synchronizedFiles.size());
    }

}
