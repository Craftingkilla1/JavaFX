package com.app.gui;

import com.app.data.RobotData;
import com.app.util.FRCConfig;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Dashboard {
    private RobotData robotData;
    private TextArea textArea = new TextArea();
    private VBox smartDashboardVBox = new VBox();
    private Map<String, CheckBox> createdCheckBoxes = new HashMap<>();
    private Map<String, Gauge> gauges = new HashMap<>();

    public void init(Stage primaryStage) {
        // Initialize RobotData
        robotData = new RobotData("10.20.28.2", 1735);

        // Initialize UI
        BorderPane root = new BorderPane();
        VBox checkBoxVBox = new VBox();
        ScrollPane checkBoxScrollPane = new ScrollPane(checkBoxVBox);

        // Create SplitPane and add TextArea and VBox for gauges
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(textArea, smartDashboardVBox);

        // Add to root layout
        root.setLeft(checkBoxScrollPane);
        root.setCenter(splitPane);

        // Stage setup
        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(new Scene(root, 1900, 1060));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        // Initialize Timeline to fetch and display data
        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.seconds(0.02),
                event -> {
                    // Fetch data in a separate thread
                    new Thread(() -> {
                        Map<String, Object> data = robotData.fetchData();
                        // Update UI on JavaFX Application Thread
                        Platform.runLater(() -> {
                            updateCheckBoxes(data, checkBoxVBox);
                            updateSmartDashboard(data);
                        });
                    }).start();
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateCheckBoxes(Map<String, Object> data, VBox checkBoxVBox) {
        // Existing functionality
        for (String key : data.keySet()) {
            String rootTable = key.split("/")[0];
            if (!createdCheckBoxes.containsKey(rootTable)) {
                CheckBox checkBox = new CheckBox(rootTable);
                checkBox.setSelected(true);
                createdCheckBoxes.put(rootTable, checkBox);
                checkBoxVBox.getChildren().add(checkBox);
            }
        }
    }

    private void updateSmartDashboard(Map<String, Object> data) {
        GridPane gridPane = new GridPane();

        ToggleButton button = new ToggleButton("Turn");
        button.setSelected(false);
        button.setOnAction(e -> {

            System.out.println("onAction");
            if (robotData.getBooleanValue("Turn")) {
                robotData.setBooleanValue("Turn", false);
                System.out.println("false");
            }
            else {
                robotData.setBooleanValue("Turn", true);
                System.out.println("true");
            }

        });
        gridPane.add(button, 0, 0);


        int row = 0, col = 1;
        StringBuilder textOutput = new StringBuilder();
    
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            String rootTable = key.split("/")[0];
            CheckBox checkBox = createdCheckBoxes.get(rootTable);
    
            if (checkBox != null && checkBox.isSelected()) {
                textOutput.append(key).append(": ").append(entry.getValue()).append("\n");
    
                if (key.startsWith("SmartDashboard/")) {
                    Object value = entry.getValue();
                    String shortKey = key.replace("SmartDashboard/", "");
                    FRCConfig.UIType uiType = FRCConfig.getUIType(shortKey);
    
                    if (uiType == FRCConfig.UIType.GAUGE && value instanceof Number) {
                        FRCConfig.GaugeConfig config = FRCConfig.getConfig(shortKey);
                        Gauge gauge = gauges.getOrDefault(key, GaugeBuilder.create().title(shortKey).build());
                        gauge.setPrefSize(200, 200);
                        gauge.setMinValue(config.minValue);
                        gauge.setMaxValue(config.maxValue);
                        gauge.setUnit(config.unit);
                        gauge.setValue(((Number) value).doubleValue());
                        gauges.put(key, gauge);
                        gridPane.add(gauge, col, row);
                    
                    }
                
                    

                    col++;
                    if (col > 3) {  // Adjust based on your layout
                        col = 0;
                        row++;
                    }
                    
                }
            }
        }
    
        textArea.setText(textOutput.toString());
        smartDashboardVBox.getChildren().clear();
        smartDashboardVBox.getChildren().add(gridPane);
    }   
}
