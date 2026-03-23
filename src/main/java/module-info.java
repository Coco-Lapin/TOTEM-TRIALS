module com.totemtrials.totemtrials {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;


    opens com.totemtrials.totemtrials to javafx.fxml;
    opens com.totemtrials.totemtrials.questions to com.google.gson;
    exports com.totemtrials.totemtrials;

    opens com.totemtrials.totemtrials.plateau to javafx.graphics, javafx.fxml;

    // Exportez aussi le package pour qu'il soit visible globalement
    exports com.totemtrials.totemtrials.plateau;
}