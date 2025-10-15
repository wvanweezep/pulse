module com.wvanw.pulse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.wvanw.pulse to javafx.fxml;
    exports com.wvanw.pulse;
}