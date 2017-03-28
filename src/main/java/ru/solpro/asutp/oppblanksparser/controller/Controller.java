/*
 * @(#)Controller.java 1.0 13.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.solpro.asutp.oppblanksparser.model.BlankData;
import ru.solpro.asutp.oppblanksparser.util.ExcelUtil;
import ru.solpro.asutp.oppblanksparser.util.Util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Protsvetov Danila
 * @version 1.1
 */
public class Controller {

    private static ArrayList<BlankData> blankDataList = new ArrayList<>(); // считанные данные

    @FXML
    private Label labelDataProcessingTime;
    @FXML
    private Button btnStartAnalise;
    @FXML
    private Label labelListIdleGroups;

    @FXML
    private void initialize() {
        SettingController setting = SettingController.getInstance();
        int count = 0;
        StringBuilder sb = new StringBuilder("Группы простоев для поиска:\n");
        for (String s : setting.getIdleGroups()) {
            sb.append(s);
            sb.append("; ");
            if (count > 5) {
                sb.append("\n");
                count = 0;
            }
            count++;
        }
        labelListIdleGroups.setText(sb.toString());
    }

    @FXML
    private void startAction(ActionEvent actionEvent) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File[] files = getListFolderFromSettings();

                if (files.length > 0) {
                    for (File file : files) {
                        String[] listFilesDir = file.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return Util.checkCorrectFileName(name);
                            }
                        });

                        if (listFilesDir != null) {
                            int countFile = 0;
                            for (String fileName : listFilesDir) {
                                String pathToFile = file.getAbsolutePath() + "\\" + fileName;
                                try {
                                    ArrayList<BlankData> blankDataFromFile = ExcelUtil.getBlankDataFromFile(pathToFile);
                                    blankDataList.addAll(blankDataFromFile);
                                    countFile++;
                                    String strProgress = "Обработано " + countFile + "/" + listFilesDir.length;
                                    Platform.runLater(() -> {
                                        labelDataProcessingTime.setText(strProgress);
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    Platform.runLater(() -> {
                        labelDataProcessingTime.setText("Записей найдёно: " + blankDataList.size());
                    });

                    if (blankDataList.size() > 0) {
                        ExcelUtil.setBlankDataToFile("output.xls", blankDataList);
                        blankDataList.clear();

                        Desktop desktop = Desktop.getDesktop();
                        try {
                            // открываю только что созданный файл
                            desktop.open(new File("output.xls"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private File[] getListFolderFromSettings() {
        File[] result = new File[SettingController.getInstance().getPath().size()];
        int i = 0;

        for (String strFile : SettingController.getInstance().getPath()) {
            File file = new File(strFile);
            result[i++] = file;
        }

        return result;
    }

    private File[] getListFolderFromDialog() {
        JFileChooser dialog = new JFileChooser();

        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);   // режим выбора только директории
        dialog.setApproveButtonText("Выбрать");                 // выбрать название для кнопки согласия
        dialog.setDialogTitle("Выберите файлы для обработки");  // выбрать название
        dialog.setDialogType(JFileChooser.OPEN_DIALOG);         // выбрать тип диалога Open или Save
        dialog.setMultiSelectionEnabled(true);                  // разрешить выбор нескольких файлов
        dialog.showOpenDialog(null);

        return dialog.getSelectedFiles();
    }
}
