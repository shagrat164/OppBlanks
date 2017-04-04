/*
 * @(#)SettingController.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller;

import ru.solpro.asutp.oppblanksparser.model.Path;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
@XmlRootElement(name = "settings")
public class SettingController {

    private static SettingController ourInstance;

    private ArrayList<Path> path = new ArrayList<>();         // пути к папкам с бланками
    private ArrayList<String> lineNames = new ArrayList<>();  // наименование линий
    private ArrayList<String> idleGroups = new ArrayList<>(); // список групп простоя

    public static SettingController getInstance() {
        if (ourInstance == null) {
            ourInstance = new SettingController();
        }
        return ourInstance;
    }

    private SettingController() {}

    @XmlElement
    public ArrayList<String> getLineNames() {
        return lineNames;
    }

    public void setLineNames(ArrayList<String> lineNames) {
        this.lineNames = lineNames;
    }

    @XmlElement
    public ArrayList<Path> getPath() {
        return path;
    }

    public void setPath(ArrayList<Path> path) {
        this.path = path;
    }

    @XmlElement
    public ArrayList<String> getIdleGroups() {
        return idleGroups;
    }

    public void setIdleGroups(ArrayList<String> idleGroups) {
        this.idleGroups = idleGroups;
    }

    /**
     * Получить название производства по пути к бланку.
     * @param pathname название линии.
     * @return Название производства. Null если соответствия нет.
     */
    public String getNameProductionByPathname(String pathname) {
        for (Path line : path) {
            if (pathname.contains(line.getPath())) {
                return line.getNameProduction();
            }
        }
        return null;
    }

    /**
     * Метод позволяет проверить, есть линия в списке или нет.
     * @param lineName название линии.
     * @return true - линия найдена, false - линия не найдена.
     */
    public boolean containsNameLine(String lineName) {
        return lineNames.contains(lineName);
    }
}
