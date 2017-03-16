/*
 * @(#)MainApp.java 1.0 10.03.2017
 */

package ru.solpro.asutp.oppblanksparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.solpro.asutp.oppblanksparser.controller.parser.SettingsXmlParser;

/**
 * @author Protsvetov Danila
 * @version 1.0
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public AnchorPane getRootLayout() {
        return rootLayout;
    }

    @Override
    public void init() throws Exception {
        new SettingsXmlParser().loadFromFile("data/setting.xml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.rootLayout = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));

        this.primaryStage.setTitle("Парсер бланков ОПП");
        this.primaryStage.setScene(new Scene(rootLayout, 350, 300));
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {}
}
