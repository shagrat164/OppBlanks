/*
 * @(#)SettingController.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller;

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

    private ArrayList<String> lineNames = new ArrayList<>(); // наименование линий
    private ArrayList<String> idleGroups = new ArrayList<>(); // список групп простоя

    public static SettingController getInstance() {
        if (ourInstance == null) {
            ourInstance = new SettingController();
        }
        return ourInstance;
    }

    private SettingController() {
    }

    @XmlElement
    public ArrayList<String> getLineNames() {
        return lineNames;
    }

    public void setLineNames(ArrayList<String> lineNames) {
        this.lineNames = lineNames;
    }

    @XmlElement
    public ArrayList<String> getIdleGroups() {
        return idleGroups;
    }

    public void setIdleGroups(ArrayList<String> idleGroups) {
        this.idleGroups = idleGroups;
    }
}
