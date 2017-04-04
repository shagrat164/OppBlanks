/*
 * @(#)Path.java 1.0 04.04.2017
 */

package ru.solpro.asutp.oppblanksparser.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public class Path {
    private String nameProduction;
    private String path;

    public Path() {
    }

    public Path(String nameProduction, String path) {
        this.nameProduction = nameProduction;
        this.path = path;
    }

    @XmlAttribute
    public String getNameProduction() {
        return nameProduction;
    }

    public void setNameProduction(String nameProduction) {
        this.nameProduction = nameProduction;
    }

    @XmlValue
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{" +
                "nameProduction='" + nameProduction + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
