package com.app.data;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RobotData {
    private NetworkTable nt;
    private List<DynamicData> dynamicDataList;

    public RobotData(String ipAddress, int port) {
        // Initialize NetworkTables
        NetworkTable.setIPAddress(ipAddress);
        NetworkTable.setPort(port);
        NetworkTable.setClientMode();
        nt = NetworkTable.getTable("SmartDashboard");

        dynamicDataList = new ArrayList<>();
    }

    public void updateDynamicData() {
        // Fetch all keys from the NetworkTable
        Set<String> keys = nt.getKeys();
        for (String key : keys) {
            // Fetch the value for each key
            double value = nt.getNumber(key, Double.NaN);
            long timestamp = System.currentTimeMillis();

            // Check if this key already exists in dynamicDataList
            DynamicData existingData = findDynamicDataByKey(key);
            if (existingData != null) {
                // Update the existing data
                existingData.setValue(value);
                existingData.setTimestamp(timestamp);
            } else {
                // Add new data
                DynamicData newData = new DynamicData(key, value, timestamp);
                dynamicDataList.add(newData);
            }
        }
    }

    public void updateMockData() {
        // For demonstration, let's assume mock keys are "Mock1", "Mock2", etc.
        for (int i = 1; i <= 5; i++) {
            String key = "Mock" + i;
            double value = Math.random() * 100;
            long timestamp = System.currentTimeMillis();

            DynamicData existingData = findDynamicDataByKey(key);
            if (existingData != null) {
                existingData.setValue(value);
                existingData.setTimestamp(timestamp);
            } else {
                DynamicData newData = new DynamicData(key, value, timestamp);
                dynamicDataList.add(newData);
            }
        }
    }

    private DynamicData findDynamicDataByKey(String key) {
        for (DynamicData data : dynamicDataList) {
            if (data.getName().equals(key)) {
                return data;
            }
        }
        return null;
    }

    public List<DynamicData> getDynamicDataList() {
        return dynamicDataList;
    }

    // Inner class to hold dynamic data
    public static class DynamicData {
        private String name;
        private double value;
        private long timestamp;

        public DynamicData(String name, double value, long timestamp) {
            this.name = name;
            this.value = value;
            this.timestamp = timestamp;
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
