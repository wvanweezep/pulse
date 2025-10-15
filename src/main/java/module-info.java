module com.wvanw.pulse {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wvanw.pulse to javafx.fxml;
    exports com.wvanw.pulse;
}