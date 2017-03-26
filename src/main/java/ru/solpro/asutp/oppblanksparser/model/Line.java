/*
 * @(#)Line.java 1.0 24.03.2017
 */

package ru.solpro.asutp.oppblanksparser.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
@XmlType(propOrder = {"nameProduction", "nameLine"})
public class Line {
    private String nameProduction = "";
    private String nameLine = "";

    public Line() {}

    public Line(String nameProduction, String nameLine) {
        this.nameProduction = nameProduction;
        this.nameLine = nameLine;
    }

    @XmlAttribute
    public String getNameProduction() {
        return nameProduction;
    }

    public void setNameProduction(String nameProduction) {
        this.nameProduction = nameProduction;
    }

    @XmlAttribute
    public String getNameLine() {
        return nameLine;
    }

    public void setNameLine(String nameLine) {
        this.nameLine = nameLine;
    }
}
