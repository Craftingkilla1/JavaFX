package com.app.data;

import com.app.util.FRCConfig;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.LinkedHashMap;
import java.util.Map;

public class RobotData {
    private final NetworkTableInstance ntInstance;
    private final Map<String, Object> dynamicDataMap;

    public RobotData(String ipAddress, int port) {
        ntInstance = NetworkTableInstance.getDefault();
        ntInstance.setServer(ipAddress, port);
        ntInstance.startClient3("Riley");
        dynamicDataMap = new LinkedHashMap<>();

        // Initialize keys based on FRCConfig
        NetworkTable table = ntInstance.getTable("SmartDashboard");
        for (String key : FRCConfig.UI_TYPE_MAP.keySet()) {
            if (FRCConfig.UI_TYPE_MAP.get(key) == FRCConfig.UIType.BUTTON) {
                table.getEntry(key).setBoolean(false);
            }
        }
    }

    public Map<String, Object> fetchData() {
        dynamicDataMap.clear();
        updateTableData(ntInstance.getTable(""), "");
        return new LinkedHashMap<>(dynamicDataMap);
    }

    public void setBooleanValue(String key, boolean value) {
        NetworkTable table = ntInstance.getTable("SmartDashboard");
        table.getEntry(key).setBoolean(value);
    }

    public boolean getBooleanValue(String key) {
        NetworkTable table = ntInstance.getTable("SmartDashboard");
        return table.getEntry(key).getBoolean(false);
    }

    private void updateTableData(NetworkTable table, String prefix) {
        for (String key : table.getKeys()) {
            Object value = table.getEntry(key).getValue().getValue();
            dynamicDataMap.put(prefix + key, value);
        }

        for (String subTableName : table.getSubTables()) {
            NetworkTable subTable = table.getSubTable(subTableName);
            updateTableData(subTable, prefix + subTableName + "/");
        }
    }
}
