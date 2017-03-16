/*
 * @(#)ExcelUtil.java 1.0 10.03.2017
 */

package ru.solpro.asutp.oppblanksparser.util;

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

    private static SettingController setting = SettingController.getInstance();

    private ExcelUtil() {}

    /**
     * Парсит файл бланка ОПП и выбирает данные о простоях.
     *
     * @param file файл для обработки
     * @return массив с данными о простоях
     * @throws IOException
     */
    public static ArrayList<BlankData> getBlankDataFromFile(String file) throws IOException {
        ArrayList<BlankData> res = new ArrayList<>();
        File xlsxFile = new File(file);
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
                    if (!setting.getLineNames().contains(lineName)) {
                        break;
                    }
                }

                cell = row.getCell(13);

                if ((cell != null) && (cell.getCellType() == Cell.CELL_TYPE_STRING)) {
                    if (setting.getIdleGroups().contains(cell.getStringCellValue())) {
                        BlankData blankData = new BlankData();

                        blankData.setEventDate(Util.getDateFromFilename(xlsxFile));

                        blankData.setLineName(lineName);

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
                        if (cell != null) {
                            blankData.setCauseDowntime(cell.getStringCellValue());
                        }

                        res.add(blankData);
                    }
                }
            }
        }
        workbook.close();
        return res;
    }

    /**
     * Метод записывает переданные данные в файл Excel.
     *
     * @param file          файл для записи.
     * @param dataArrayList данные для записи.
     */
    public static void setBlankDataToFile(String file, ArrayList<BlankData> dataArrayList) {
        HSSFWorkbook workbook = getWorkbookFromFile(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;

        // актуальное число строк,
        // необходимо для записи данных в конец файла
        int rowsTotal = sheet.getPhysicalNumberOfRows();

        for (BlankData blankData : dataArrayList) {
            rowsTotal++;
            row = sheet.createRow(rowsTotal);

            // название линии
            cell = row.createCell(0);
            cell.setCellValue(blankData.getLineName());

            // дата события
            cell = row.createCell(1);
            cell.setCellValue(blankData.getEventDate());

            // время остановки
            cell = row.createCell(2);
            Date stopTime = blankData.getStopTime();
            if (stopTime != null) {
                stopTime.setYear(stopTime.getYear() + 10);
                cell.setCellValue(stopTime);
            }

            // время запуска
            cell = row.createCell(3);
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
            cell = row.createCell(4);
            Date downtime = blankData.getDowntime();
            if ((stopTime != null) && (launchTime != null)) {
                String formula = row.getCell(3).getAddress().toString() + "-" + row.getCell(2).getAddress().toString();
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
            cell = row.createCell(5);
            String idleGroupNumber = blankData.getIdleGroupNumber();
            if (idleGroupNumber != null) {
                cell.setCellValue(idleGroupNumber);
            }

            // описание группы простоя
            cell = row.createCell(6);
            String typeIdleGroup = blankData.getTypeIdleGroup();
            if (typeIdleGroup != null) {
                cell.setCellValue(typeIdleGroup);
            }

            // причина простоя
            cell = row.createCell(7);
            String causeDowntime = blankData.getCauseDowntime();
            if (causeDowntime != null) {
                cell.setCellValue(causeDowntime);
            }
        }

        try {
            workbook.write(new FileOutputStream(file));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            workbook.createSheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
}
