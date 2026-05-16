package com.ddangkhoa.trafficfx;

import com.ddangkhoa.trafficfx.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 760;

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getView(), WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        stage.setTitle("Quản lý và phân tích lưu lượng giao thông TP.HCM");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
