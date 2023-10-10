package com.app.gui;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

import com.app.data.RobotData;
import com.app.util.ConfigManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Dashboard {
    private Map<String, Object> userConfig;  // Class-level variable
    private long baseTime = -1;

    public void init(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox vbox = new VBox();
        
        // Initialize ConfigManager and load user configuration
        ConfigManager configManager = new ConfigManager();
        this.userConfig = configManager.loadUserConfig();  // Initialize userConfig here

        // Add a button
        Button myButton = new Button("Click Me");
        vbox.getChildren().add(myButton);
        
        // Add a text field
        TextField myTextField = new TextField("Default Text");
        vbox.getChildren().add(myTextField);
        
        // Initialize data fetcher and visualizer
        RobotData robotData = new RobotData();
        DataVisualizer dataVisualizer = new DataVisualizer();
        LineChart<Number, Number> lineChart = dataVisualizer.createLineChart();
        vbox.getChildren().add(lineChart);

        root.setCenter(vbox);

        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Timeline to fetch and display data
        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                    System.out.println("Fetching Data");  // Debug print
                    Map<String, Object> data = robotData.fetchData();
                    updateLineChart(lineChart, data);
                }
            )
        );
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateLineChart(LineChart<Number, Number> lineChart, Map<String, Object> data) {
        

        System.out.println("Received Data: " + data);  // Debug print
    
        // Assuming you have a series added to your line chart
        XYChart.Series<Number, Number> series = (XYChart.Series<Number, Number>) lineChart.getData().get(0);
        
        // Get the motor to display from userConfig
        String displayMotor = (String) userConfig.getOrDefault("displayMotor", "Motor1_Speed");
    
        // Get the latest motor speed
        Number newSpeed = (Number) data.get(displayMotor);
    
        // Check for null before adding to the chart
        if (newSpeed != null) {
            // Initialize baseTime if it hasn't been set yet
            if (baseTime == -1) {
                baseTime = System.currentTimeMillis();
            }
            
            // Calculate the relative time
            long relativeTime = System.currentTimeMillis() - baseTime;
    
            // Add new data point to series
            series.getData().add(new XYChart.Data<>(relativeTime, newSpeed));
            
            //Remove older data to keep the chart manageable
            if (series.getData().size() > 100) {
                series.getData().remove(0);
            }
        }
    }    
}
