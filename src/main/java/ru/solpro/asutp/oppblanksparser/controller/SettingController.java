/*
 * @(#)SettingController.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller;

import ru.solpro.asutp.oppblanksparser.model.Line;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
@XmlRootElement(name = "settings")
public class SettingController {

    private static SettingController ourInstance;

    private ArrayList<String> path = new ArrayList<>();         // пути к папкам с бланками
    private List<Line> lineNames = new ArrayList<>();           // наименование линий по производствам
    private ArrayList<String> idleGroups = new ArrayList<>();   // список групп простоя

    public static SettingController getInstance() {
        if (ourInstance == null) {
            ourInstance = new SettingController();
        }
        return ourInstance;
    }

    private SettingController() {}

    @XmlElement
    public List<Line> getLineNames() {
        return lineNames;
    }

    public void setLineNames(List<Line> lineNames) {
        this.lineNames = lineNames;
    }

    @XmlElement
    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
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
     * Получить название производства по имени линии.
     * @param lineName название линии.
     * @return Название производства. Null если соответствия нет.
     */
    public String getNameProdByNameLine(String lineName) {
        for (Line line : lineNames) {
            if (line.getNameLine().equals(lineName)) {
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
        for (Line line : lineNames) {
            if (line.getNameLine().equals(lineName)) {
                return true;
            }
        }
        return false;
    }
}
