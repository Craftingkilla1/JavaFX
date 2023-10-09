module com.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;  // Added this line
    
    // Add other required modules here
    exports com.app;
}
