package com.app.util;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public Map<String, Object> loadUserConfig() {
        // Simulate loading user configuration from a file
        Map<String, Object> mockConfig = new HashMap<>();
        mockConfig.put("displayMotor", "Motor1");
        return mockConfig;
    }
}
