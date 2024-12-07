package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.database.VocDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.fx.Styler.StandardColor.GREEN;
import static com.katyshevtseva.fx.Styler.StandardColor.WHITE;

public class DaysReportService {

    public static List<List<ReportCell>> getReport() {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead());
        result.addAll(getContent());
        return result;
    }

    private static List<List<ReportCell>> getContent() {
        List<List<ReportCell>> result = new ArrayList<>();
        List<Date> logDates = getSortedLogDates();
        Date firstMonday = getPeriodOfWeekDateBelongsTo(logDates.get(0)).start();
        Date lastMonday = getPeriodOfWeekDateBelongsTo(new Date()).start();

        Date monday = new Date(firstMonday.getTime());
        do {
            ReportCell monthCell = ReportCell.builder().width(150).build();

            List<ReportCell> weekLine = new ArrayList<>();
            weekLine.add(monthCell);
            for (int i = 0; i <= 6; i++) {
                Date date = shiftDate(monday, TimeUnit.DAY, i);
                int dayOfMonth = DateUtils.getMonthDayIndex(date);
                boolean logExists = logDates.contains(date);
                weekLine.add(
                        ReportCell.builder()
                                .text(dayOfMonth + "")
                                .color(logExists ? GREEN.getCode() : WHITE.getCode())
                                .build());

                if (dayOfMonth == 1) {
                    monthCell.setText(MONTH_YEAR_DATE_FORMAT.format(date));
                }
            }

            result.add(weekLine);

            monday = shiftDate(monday, TimeUnit.DAY, 7);
        } while (!after(monday, lastMonday));


        return result;
    }

    private static List<Date> getSortedLogDates() {
        return VocDao.getAllLogs().stream()
                .map(LearningLog::getDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private static List<ReportCell> getReportHead() {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());
        result.add(ReportCell.builder().text("Пн").build());
        result.add(ReportCell.builder().text("Вт").build());
        result.add(ReportCell.builder().text("Ср").build());
        result.add(ReportCell.builder().text("Чт").build());
        result.add(ReportCell.builder().text("Пт").build());
        result.add(ReportCell.builder().text("Сб").build());
        result.add(ReportCell.builder().text("Вс").build());
        return result;
    }
}
