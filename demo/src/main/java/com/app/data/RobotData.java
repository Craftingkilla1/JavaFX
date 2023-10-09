package com.app.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RobotData {
    private final Random random = new Random();

    public Map<String, Object> fetchData() {
        Map<String, Object> fakeData = new HashMap<>();
        fakeData.put("Motor1_Speed", 1200 + random.nextInt(100));
        fakeData.put("Motor2_Speed", 1300 + random.nextInt(100));
        
        System.out.println("Generated Data: " + fakeData);  // Debug print
        return fakeData;
    }
    
}
