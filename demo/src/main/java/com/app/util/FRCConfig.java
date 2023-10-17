package com.app.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Configuration class for FRC components and UI elements.
 */
public class FRCConfig {

    public enum UIType {
        GAUGE,
        BUTTON
        // Add more types as needed
    }

    /**
     * Configuration for Gauge elements.
     */
    public static class GaugeConfig {
        public double minValue;
        public double maxValue;
        public String unit;

        public GaugeConfig(double minValue, double maxValue, String unit) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.unit = unit;
        }
    }

    // Map to hold Gauge configurations
    private static final Map<String, GaugeConfig> frcConfigMap;

    // Map to hold UI types for each key
    public static final Map<String, UIType> UI_TYPE_MAP;

    // Set to hold keys for Buttons
    public static final Set<String> BUTTON_KEYS = Set.of(
        "SmartDashboard/turn"
        // ... add more keys here
    );

    static {
        frcConfigMap = new HashMap<>();
        UI_TYPE_MAP = new HashMap<>();

        // Battery Voltage
        frcConfigMap.put("BatV", new GaugeConfig(7, 14, "V"));
        UI_TYPE_MAP.put("BatV", UIType.GAUGE);

        // Gyro Angle
        frcConfigMap.put("Gyro Angle", new GaugeConfig(-180, 180, "Degrees"));
        UI_TYPE_MAP.put("Gyro Angle", UIType.GAUGE);

        // Encoder Positions
        frcConfigMap.put("BR ABS Encoder", new GaugeConfig(0, 360, "Degrees"));
        UI_TYPE_MAP.put("BR ABS Encoder", UIType.GAUGE);

        // ... (other configurations)

        // Buttons
        UI_TYPE_MAP.put("Turn", UIType.BUTTON);  // Added "Turn" as a button

        // Add more configurations here
    }

    /**
     * Retrieve the GaugeConfig for a given key.
     * @param key The key for which to retrieve the GaugeConfig.
     * @return The GaugeConfig for the given key.
     */
    public static GaugeConfig getConfig(String key) {
        return frcConfigMap.get(key);
    }

    /**
     * Retrieve the UIType for a given key.
     * @param key The key for which to retrieve the UIType.
     * @return The UIType for the given key.
     */
    public static UIType getUIType(String key) {
        return UI_TYPE_MAP.get(key);
    }
}
