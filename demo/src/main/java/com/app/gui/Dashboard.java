package com.app.gui;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.app.data.RobotData;

import java.util.LinkedHashMap;
import java.util.Map;

public class Dashboard {
    public void init(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox vbox = new VBox();
    
        // Initialize data fetcher
        LinkedHashMap<String, Class<?>> typesToMock = new LinkedHashMap<>();
        typesToMock.put("Motor1_Speed", Integer.class);
        typesToMock.put("Motor2_Speed", Integer.class);
        typesToMock.put("Temperature", Double.class);
        typesToMock.put("idontknow", String.class);

        RobotData robotData = new RobotData(true, typesToMock);  // For mock data
        // RobotData robotData = new RobotData(false);  // For real data

        // Initialize TextArea to display data
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        vbox.getChildren().add(textArea);

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
                    Map<String, Object> data = robotData.fetchData();
                    updateTextArea(textArea, data);
                }
            )
        );
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTextArea(TextArea textArea, Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        textArea.setText(sb.toString());
    }
}
