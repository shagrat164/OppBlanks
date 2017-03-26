/*
 * @(#)SettingsXmlParser.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller.parser;


import ru.solpro.asutp.oppblanksparser.controller.SettingController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Класс для работы с xml файлом.
 * @author Protsvetov Danila
 * @version 1.0
 */
public class SettingsXmlParser implements XmlParser {

    /**
     * Загружает настройки из указанного xml файла.
     * @param fileName файл с настройками.
     */
    @Override
    public void loadFromFile(String fileName) {
        File file = new File(fileName);
        try {
            JAXBContext context = JAXBContext.newInstance(SettingController.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            SettingController wrapper = (SettingController) unmarshaller.unmarshal(file);

            SettingController.getInstance().getPath().clear();
            SettingController.getInstance().setPath(wrapper.getPath());

            SettingController.getInstance().getLineNames().clear();
            SettingController.getInstance().setLineNames(wrapper.getLineNames());

            SettingController.getInstance().getIdleGroups().clear();
            SettingController.getInstance().setIdleGroups(wrapper.getIdleGroups());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохраняет текущие настройки в xml файле.
     * @param fileName файл с настройками.
     */
    @Override
    public void saveToFile(String fileName) {
        File file = new File(fileName);
        try {
            JAXBContext context = JAXBContext.newInstance(SettingController.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            SettingController wrapper = SettingController.getInstance();

            marshaller.marshal(wrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
