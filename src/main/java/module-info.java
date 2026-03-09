module com.totemtrials.totemtrials {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.totemtrials.totemtrials to javafx.fxml;
    exports com.totemtrials.totemtrials;
}