/*
 * @(#)XmlParser.java 1.0 14.03.2017
 */

package ru.solpro.asutp.oppblanksparser.controller.parser;

import java.net.URL;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public interface XmlParser {
    void loadFromFile(String fileName);
    void saveToFile(String fileName);
}
