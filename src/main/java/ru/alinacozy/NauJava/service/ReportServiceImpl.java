package ru.alinacozy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.entity.Report;
import ru.alinacozy.NauJava.entity.ReportStatus;
import ru.alinacozy.NauJava.repository.FlossRepository;
import ru.alinacozy.NauJava.repository.ReportRepository;
import ru.alinacozy.NauJava.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final FlossRepository flossRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository,
                             FlossRepository flossRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.flossRepository = flossRepository;
    }

    /**
     * Получение отчета по ID
     * @param id идентификатор отчета
     * @return объект сущности Report
     */
    public Report getReport(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report with id " + id + " not found"));
    }

    /**
     * Создание отчета.
     * Создает объект в БД и возвращает id объекта, а также запускает асинхронную генерацию отчета
     * @return id созданного объекта в БД
     */
    public Long createReport() {
        Report newReport = new Report();
        Report savedReport = reportRepository.save(newReport);
        Long reportId = savedReport.getId();
        CompletableFuture<Void> future = generateReportAsync(reportId);
        return reportId;
    }

    /**
     * Асинхронный запуск формирования отчета.
     * Вычисление количества пользователей и получение списка объектов вычисляется в отдельных потоках.
     * Отчет сохраняется в БД со статусом COMPLETED или ERROR.
     * @param reportId id объекта в БД
     * @return CompletableFuture, представляющий асинхронную операцию
     */
    public CompletableFuture<Void> generateReportAsync(Long reportId) {
        return CompletableFuture.runAsync(() -> {
            Report report = reportRepository.findById(reportId).orElseThrow();

            // если отчет уже завершен, не делаем ничего
            if (report.getStatus() != ReportStatus.CREATED) {
                System.out.println("Отчет уже в статусе: " + report.getStatus() + ", пропускаем");
                return;
            }

            try {
                long startTime = System.currentTimeMillis();

                // используем AtomicLong для хранения результатов (потокобезопасный класс)
                AtomicLong userCount = new AtomicLong(0);
                AtomicLong timeForUsers = new AtomicLong(0);
                AtomicLong timeForFlossList = new AtomicLong(0);

                List<Floss> flossList = new ArrayList<>();

                // отдельные потоки для получения количества пользователей и списка ниток
                Thread usersThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    userCount.set(userRepository.count());
                    timeForUsers.set(System.currentTimeMillis() - start);
                });

                Thread flossListThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    flossList.addAll(flossRepository.findAll());
                    timeForFlossList.set(System.currentTimeMillis() - start);
                });

                usersThread.start();
                flossListThread.start();

                // Ждем завершения
                usersThread.join();
                flossListThread.join();

                long totalTime = (System.currentTimeMillis() - startTime);

                // Создаем содержимое отчета
                String htmlContent = buildHtmlReport(
                        userCount.get(),
                        flossList,
                        timeForUsers.get(),
                        timeForFlossList.get(),
                        totalTime
                );

                // Обновляем отчет в БД
                report.setContent(htmlContent);
                report.setStatus(ReportStatus.COMPLETED);
                reportRepository.save(report);

            } catch (Exception e) {
                report.setStatus(ReportStatus.ERROR);
                reportRepository.save(report);
                e.printStackTrace();
            }
        });
    }

    /**
     * Вспомогательный метод для генерации HTML.
     * @param userCount количество зарегистрированных пользователей
     * @param flosses список объектов ниток
     * @param timeUsers время, затраченное на вычисление userCount
     * @param timeObjects время, затраченное на получение flosses
     * @param totalTime общее время, в течение которого формировались данные для отчета
     * @return строка в формате HTML (наполнение для отчета)
     */
    private String buildHtmlReport(long userCount, List<Floss> flosses,
                                   long timeUsers, long timeObjects, long totalTime) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h1>Статистика приложения</h1>");
        html.append("<table border='1'>");
        html.append("<tr><th>Показатель</th><th>Значение</th></tr>");
        html.append("<tr><td>Количество пользователей</td><td>").append(userCount).append("</td></tr>");
        html.append("<tr><td>Время подсчета пользователей</td><td>").append(timeUsers).append(" мс</td></tr>");
        html.append("<tr><td>Время получения списка объектов</td><td>").append(timeObjects).append(" мс</td></tr>");
        html.append("<tr><td>Общее время формирования отчета</td><td>").append(totalTime).append(" мс</td></tr>");
        html.append("</table>");

        html.append("<h2>Список объектов ниток:</h2><ul>");
        for (Floss floss : flosses) {
            html.append("<li>").append(floss.toString()).append("</li>");
        }
        html.append("</ul>");

        html.append("</body></html>");
        return html.toString();
    }

}
