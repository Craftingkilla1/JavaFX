package com.app;

import com.app.gui.Dashboard;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Dashboard dashboard = new Dashboard();
        dashboard.init(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}