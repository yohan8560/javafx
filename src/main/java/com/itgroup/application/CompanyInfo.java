package com.itgroup.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompanyInfo extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        String fxmlFile = "/com/itgroup/fxml/" + "CompanyInfo.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));


        Parent container = fxmlLoader.load();
        Scene scene = new Scene(container);
        stage.setTitle("회사 정보 등록");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
