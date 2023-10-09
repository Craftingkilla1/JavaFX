package com.app.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DataVisualizer {
    
    public LineChart<Number, Number> createLineChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My Series");
        
        lineChart.getData().add(series);
        
        return lineChart;
    }
    
}
