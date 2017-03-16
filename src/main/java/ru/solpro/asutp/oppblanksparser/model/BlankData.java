/*
 * @(#)BlankData.java 1.0 10.03.2017
 */

package ru.solpro.asutp.oppblanksparser.model;

import java.util.Date;

/**
 * Объект с информацией о простое линии.
 *
 * @author Protsvetov Danila
 * @version 1.1
 */
public class BlankData {

    // Название линии.
    private String lineName;

    // Дата события.
    private Date eventDate;

    // Время остановки.
    private Date stopTime;

    // Время запуска.
    private Date launchTime;

    // Время простоя.
    private Date downtime;

    // Номер группы простоя.
    private String idleGroupNumber;

    // Тип группы простоя.
    private String typeIdleGroup;

    // Причина простоя.
    private String causeDowntime;

    public BlankData() {}

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public Date getDowntime() {
        return downtime;
    }

    public void setDowntime(Date downtime) {
        this.downtime = downtime;
    }

    public String getIdleGroupNumber() {
        return idleGroupNumber;
    }

    public void setIdleGroupNumber(String idleGroupNumber) {
        this.idleGroupNumber = idleGroupNumber;
    }

    public String getTypeIdleGroup() {
        return typeIdleGroup;
    }

    public void setTypeIdleGroup(String typeIdleGroup) {
        this.typeIdleGroup = typeIdleGroup;
    }

    public String getCauseDowntime() {
        return causeDowntime;
    }

    public void setCauseDowntime(String causeDowntime) {
        this.causeDowntime = causeDowntime;
    }

    @Override
    public String toString() {
        return "BlankData{" +
                "lineName='" + lineName + '\'' +
                ", stopTime=" + stopTime +
                ", launchTime=" + launchTime +
                ", idleGroupNumber=" + idleGroupNumber +
                ", typeIdleGroup='" + typeIdleGroup + '\'' +
                ", causeDowntime='" + causeDowntime + '\'' +
                '}';
    }
}
