/*
 * @(#)Controller.java 1.0 13.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.solpro.asutp.oppblanksparser.model.BlankData;
import ru.solpro.asutp.oppblanksparser.util.ExcelUtil;
import ru.solpro.asutp.oppblanksparser.util.Util;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Protsvetov Danila
 * @version 1.0
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
        StringBuilder sb = new StringBuilder("Группы простоев для поиска:\n");
        for (String s : setting.getIdleGroups()) {
            sb.append(s);
            sb.append("\n");
        }
        labelListIdleGroups.setText(sb.toString());
    }

    @FXML
    private void startAction(ActionEvent actionEvent) {
        File[] files = showDialogSelectedFile();
        if (files.length > 0) {
            for (File file : files) {
                String[] listFilesDir = file.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return Util.checkCorrectFileName(name);
                    }
                });

                if (listFilesDir != null) {
                    for (String fileName : listFilesDir) {
                        String pathToFile = file.getAbsolutePath() + "\\" + fileName;
                        try {
                            ArrayList<BlankData> blankDataFromFile = ExcelUtil.getBlankDataFromFile(pathToFile);
                            blankDataList.addAll(blankDataFromFile);
                        } catch (IOException e) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Ошибка обработки файла");
                            alert.setContentText("Error:\n" + e.toString());
                            alert.showAndWait();
                        }
                    }
                }
            }

            labelDataProcessingTime.setText("Записей найдёно: " + blankDataList.size());

            if (blankDataList.size() > 0) {
                ExcelUtil.setBlankDataToFile("output.xls", blankDataList);
                blankDataList.clear();
            }
        }
    }

    private File[] showDialogSelectedFile() {
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
