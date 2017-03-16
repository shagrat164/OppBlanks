/*
 * @(#)SettingsXmlParser.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller.parser;

import javafx.scene.control.Alert;
import ru.solpro.asutp.oppblanksparser.controller.SettingController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public class SettingsXmlParser implements XmlParser {

    /**
     * Загружает настройки из указанного файла.
     *
     * @param fileName путь до файла с настройками.
     */
    @Override
    public void loadFromFile(String fileName) {
        File file = new File(fileName);
        try {
            JAXBContext context = JAXBContext.newInstance(SettingController.class);
            Unmarshaller um = context.createUnmarshaller();

            SettingController wrapper = (SettingController) um.unmarshal(file);

            SettingController.getInstance().getLineNames().clear();
            SettingController.getInstance().setLineNames(wrapper.getLineNames());

            SettingController.getInstance().getIdleGroups().clear();
            SettingController.getInstance().setIdleGroups(wrapper.getIdleGroups());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Сохраняет текущие настройки в указанном файле.
     *
     * @param fileName путь до файла с настройками.
     */
    @Override
    public void saveToFile(String fileName) {
        File file = new File(fileName);
        try {
            JAXBContext context = JAXBContext.newInstance(SettingController.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            SettingController wrapper = SettingController.getInstance();

            m.marshal(wrapper, file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
