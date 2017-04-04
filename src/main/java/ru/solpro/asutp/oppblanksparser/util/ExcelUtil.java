/*
 * @(#)ExcelUtil.java 1.0 10.03.2017
 */

package ru.solpro.asutp.oppblanksparser.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.solpro.asutp.oppblanksparser.controller.SettingController;
import ru.solpro.asutp.oppblanksparser.model.BlankData;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Класс для работы с файлами Excel
 *
 * @author Protsvetov Danila
 * @version 1.0
 */
public class ExcelUtil {

    private static final Logger LOG = Logger.getLogger(ExcelUtil.class);
    private static final int CELL_NAME_PROD = 0;
    private static final int CELL_NAME_LINE = 1;
    private static final int CELL_EVENT_DATA = 2;
    private static final int CELL_STOP_TIME = 3;
    private static final int CELL_START_TIME = 4;
    private static final int CELL_DOWNTIME = 5;
    private static final int CELL_IDLE_GROUP_NUMBER = 6;
    private static final int CELL_TYPE_IDLE_GROUP = 7;
    private static final int CELL_CAUSE_DOWNTIME = 8;

    private static SettingController setting = SettingController.getInstance();

    private static int counterReadErrors = 0;
    private static int counterWriteErrors = 0;

    private ExcelUtil() {}

    public static int getCounterReadErrors() {
        return counterReadErrors;
    }

    public static void setCounterReadErrors(int counterReadErrors) {
        ExcelUtil.counterReadErrors = counterReadErrors;
    }

    /**
     * Парсит файл бланка ОПП и выбирает данные о простоях.
     * @param file файл для обработки
     * @return массив с данными о простоях
     * @throws IOException
     */
    public static ArrayList<BlankData> getBlankDataFromFile(String file) {
        ArrayList<BlankData> res = new ArrayList<>();
        File xlsxFile = new File(file);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsxFile));

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                String lineName = null;
                boolean flag = false;

                for (int j = 0; j < 50; j++) {
                    XSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        break;
                    }

                    XSSFCell cell = row.getCell(1);

                    if (cell == null) {
                        break;
                    }

                    if ((cell.getCellType() == Cell.CELL_TYPE_STRING) && (!flag)) {
                        flag = true;
                        lineName = row.getCell(1).getStringCellValue();
                        if (!setting.containsNameLine(lineName)) {
                            break;
                        }
                    }

                    cell = row.getCell(13);

                    if ((cell != null) && (cell.getCellType() == Cell.CELL_TYPE_STRING)) {
                        if (setting.getIdleGroups().contains(cell.getStringCellValue())) {

                            BlankData blankData = new BlankData();

                            blankData.setEventDate(Util.getDateFromFilename(xlsxFile));
                            blankData.setLineName(lineName);
                            blankData.setProductionName(setting.getNameProductionByPathname(file));

                            cell = row.getCell(10);
                            if (cell != null) {
                                Date dateCellValue = cell.getDateCellValue();
                                if (dateCellValue != null) {
                                    blankData.setStopTime(dateCellValue);
                                }
                            }

                            cell = row.getCell(11);
                            if (cell != null) {
                                Date dateCellValue = cell.getDateCellValue();
                                if (dateCellValue != null) {
                                    blankData.setLaunchTime(dateCellValue);
                                }
                            }

                            cell = row.getCell(12);
                            if (cell != null && (cell.getCellType() != Cell.CELL_TYPE_ERROR)) {
                                Date dateCellValue = cell.getDateCellValue();
                                if (dateCellValue != null) {
                                    blankData.setDowntime(dateCellValue);
                                }
                            }

                            cell = row.getCell(13);
                            if (cell != null) {
                                blankData.setIdleGroupNumber(cell.getStringCellValue());
                            }

                            cell = row.getCell(14);
                            if (cell != null) {
                                blankData.setTypeIdleGroup(cell.getStringCellValue());
                            }

                            cell = row.getCell(15);
                            if ((cell != null) && (cell.getCellType() == Cell.CELL_TYPE_STRING)) {
                                blankData.setCauseDowntime(cell.getStringCellValue());
                            }

                            res.add(blankData);
                            LOG.info("Прочитано: File=" + xlsxFile.getPath() + "; Data=" + blankData.toString());
                        }
                    }
                }
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
            counterReadErrors++;
        } catch (IOException e) {
            LOG.error(e.getMessage());
            counterReadErrors++;
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            counterReadErrors++;
        }
        return res;
    }

    /**
     * Метод записывает переданные данные в файл Excel.
     * @param pathname      файл для записи.
     * @param dataArrayList данные для записи.
     */
    public static void setBlankDataToFile(String pathname, ArrayList<BlankData> dataArrayList) {
        HSSFWorkbook workbook = getWorkbookFromFile(pathname);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;

        // актуальное число строк,
        // необходимо для записи данных в конец файла
        int rowsTotal = sheet.getPhysicalNumberOfRows() + 1;

        for (BlankData blankData : dataArrayList) {
            row = sheet.createRow(rowsTotal++);

            // название производства
            cell = row.createCell(CELL_NAME_PROD);
            cell.setCellValue(blankData.getProductionName());

            // название линии
            cell = row.createCell(CELL_NAME_LINE);
            cell.setCellValue(blankData.getLineName());

            // дата события
            cell = row.createCell(CELL_EVENT_DATA);
            cell.setCellValue(blankData.getEventDate());

            // время остановки
            cell = row.createCell(CELL_STOP_TIME);
            Date stopTime = blankData.getStopTime();
            if (stopTime != null) {
                stopTime.setYear(stopTime.getYear() + 10);
                cell.setCellValue(stopTime);
            }

            // время запуска
            cell = row.createCell(CELL_START_TIME);
            Date launchTime = blankData.getLaunchTime();
            if (launchTime != null) {
                launchTime.setYear(launchTime.getYear() + 10);
                cell.setCellValue(launchTime);
                if (stopTime != null && stopTime.after(launchTime)) {
                    launchTime.setDate(1);
                    launchTime.setMonth(0);
                    launchTime.setYear(launchTime.getYear() + 1);
                    cell.setCellValue(launchTime);
                }
            }

            // время простоя
            cell = row.createCell(CELL_DOWNTIME);
            Date downtime = blankData.getDowntime();
            if ((stopTime != null) && (launchTime != null)) {
                String formula = row.getCell(CELL_START_TIME).getAddress().toString() + "-" + row.getCell(CELL_STOP_TIME).getAddress().toString();
                cell.setCellFormula(formula);
            } else if (downtime != null) {
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("hh:mm:ss"));

                LocalDateTime localDateTime = Util.convertDateToLocalDateTime(downtime);
                String strTime = localDateTime.toLocalTime().toString();

                cell.setCellValue(DateUtil.convertTime(strTime));
                cell.setCellStyle(cellStyle);
            }

            // номер группы простоя
            cell = row.createCell(CELL_IDLE_GROUP_NUMBER);
            String idleGroupNumber = blankData.getIdleGroupNumber();
            if (idleGroupNumber != null) {
                cell.setCellValue(idleGroupNumber);
            }

            // описание группы простоя
            cell = row.createCell(CELL_TYPE_IDLE_GROUP);
            String typeIdleGroup = blankData.getTypeIdleGroup();
            if (typeIdleGroup != null) {
                cell.setCellValue(typeIdleGroup);
            }

            // причина простоя
            cell = row.createCell(CELL_CAUSE_DOWNTIME);
            String causeDowntime = blankData.getCauseDowntime();
            if (causeDowntime != null) {
                cell.setCellValue(causeDowntime);
            }
            LOG.info("Записано: " + blankData.toString());
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathname);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Метод открывает существующий файл книги Excel или создаёт новый.
     *
     * @param xlsFile файл
     * @return экземпляр HSSFWorkbook
     */
    private static HSSFWorkbook getWorkbookFromFile(String xlsFile) {
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(xlsFile)));
        } catch (FileNotFoundException e) {
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = null;

            cell = row.createCell(0);
            cell.setCellValue("Производство");

            cell = row.createCell(1);
            cell.setCellValue("Линия");

            cell = row.createCell(2);
            cell.setCellValue("Дата");

            cell = row.createCell(3);
            cell.setCellValue("Время остановки");

            cell = row.createCell(4);
            cell.setCellValue("Время запуска");

            cell = row.createCell(5);
            cell.setCellValue("Время простоя");

            cell = row.createCell(6);
            cell.setCellValue("Группа простоя");

            cell = row.createCell(7);
            cell.setCellValue("Описание группы");

            cell = row.createCell(8);
            cell.setCellValue("Комментарий");

        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return workbook;
    }
}
