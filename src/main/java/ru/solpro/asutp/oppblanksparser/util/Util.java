/*
 * @(#)Util.java 1.0 11.03.2017
 */

package ru.solpro.asutp.oppblanksparser.util;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Util {

    private Util() {}

    /**
     * Конвертация данных.
     * @param localDateTime исходное значение.
     * @return дата и время типа Date.
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.toInstant(ZoneOffset.ofHours(4));
        Date date = Date.from(instant);
        return date;
    }

    /**
     * Конвертация данных.
     * @param date исходное значение.
     * @return дата и время типа LocalDateTime.
     */
    public static LocalDateTime convertDateToLocalDateTime (Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(4));
        return localDateTime;
    }

    /**
     * Метод возвращает дату из имени указанного файла.
     * @param file файл для обработки
     * @return дата
     */
    public static LocalDateTime getLocalDateTimeFromFilename(File file) {
        Pattern pattern = Pattern.compile("[0-9]{2}[.]{1,}[0-9]{2}[.]{1,}[0-9]{4}"); //01.01.2010
        Pattern pattern2 = Pattern.compile("[0-9]{2}[.]{1,}[0-9]{2}[.]{1,}[0-9]{2}");//01.01.10

        String fileName = file.getName();
        LocalDateTime date;
        Matcher matcher = pattern.matcher(fileName);
        Matcher matcher2 = pattern2.matcher(fileName);

        if (matcher.find()) {
            Pattern patt = Pattern.compile("[.]{2,}");
            Matcher match = patt.matcher(matcher.group());
            String findString = match.replaceAll(".");

            String[] strings = findString.split("\\.");
            date = LocalDateTime.of(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0]), 8, 0);
        } else if (matcher2.find()) {
            Pattern patt = Pattern.compile("[.]{2,}");
            Matcher match = patt.matcher(matcher2.group());
            String findString = match.replaceAll(".");

            String[] strings = findString.split("\\.");
            date = LocalDateTime.of(Integer.parseInt(strings[2]) + 2000, Integer.parseInt(strings[1]), Integer.parseInt(strings[0]), 8, 0);
        } else {
            date = LocalDateTime.now();
        }

        return date;
    }

    /**
     * Метод возвращает дату из имени указанного файла.
     * @param file файл для обработки
     * @return дата
     */
    public static Date getDateFromFilename(File file) {
        LocalDateTime dateTimeFromFilename = getLocalDateTimeFromFilename(file);
        return convertLocalDateTimeToDate(dateTimeFromFilename);
    }

    /**
     * Проверка имени файла на соответствие виду: "название дата.xlsx"
     * @param fileName имя файла
     * @return true - соответствует, false - не соответствует.
     */
    public static boolean checkCorrectFileName(String fileName) {
        Pattern pattern = Pattern.compile("^[А-Яа-я\\s]*[0-9]{2}[.]+[0-9]{2}[.]+[0-9]{2,4}\\.[xlsxXLSX]{4}$");
        Matcher matcher = pattern.matcher(fileName);

        return matcher.find();
    }
}
