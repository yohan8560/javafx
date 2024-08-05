package com.itgroup.www;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        // 오늘은 날씨가 너무 너무 더웡진짜로
        // 오늘 점심은 부대찌개 먹을꺼얌
        // 푸쉬 푸쉬 베이베
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}