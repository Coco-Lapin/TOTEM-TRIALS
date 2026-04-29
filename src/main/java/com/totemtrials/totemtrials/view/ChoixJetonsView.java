package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.models.Jeton;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChoixJetonsView {

    private final Button infoPassiveButton;
    private final StackPane backButton;
    private final Scene scene;
    private final Label labelInstruction;
    private final Map<Jeton, ImageView> jetonViews = new LinkedHashMap<>();
    private final Map<Jeton, VBox> jetonGroupes = new LinkedHashMap<>();

    public ChoixJetonsView(Stage stage, Image background, Jeton[] jetons) {
        ImageView icon = ViewUtils.createCroppedImageView(stage, "src/main/resources/images/buttons/bouton-abilities.png", 0.20);
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        infoPassiveButton = new Button("");
        infoPassiveButton.setGraphic(icon);

        backButton = ViewUtils.createBackButton(stage, 0.15);

        labelInstruction = new Label("");
        labelInstruction.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_CENTER);

        for (Jeton jeton : jetons) {
            ImageView base = ViewUtils.createCroppedImageView(stage, jeton.getImagePath(), 0.10);
            ImageView anim = ViewUtils.createCroppedImageView(stage, jeton.getAnimationPath(), 0.10);

            Image fixeImage     = base.getImage();
            Rectangle2D fixeViewport = base.getViewport();
            Image animImage     = anim.getImage();


            base.setOnMouseEntered(_ -> {
                base.setImage(animImage);
                base.setViewport(null);
            });
            base.setOnMouseExited(_ -> {
                base.setImage(fixeImage);
                base.setViewport(fixeViewport);
            });

            VBox groupe = new VBox(10, base);
            groupe.setAlignment(Pos.CENTER);

            jetonViews.put(jeton, base);
            jetonGroupes.put(jeton, groupe);
            row.getChildren().add(groupe);
        }

        StackPane.setAlignment(infoPassiveButton, Pos.TOP_RIGHT);
        StackPane.setMargin(infoPassiveButton, new javafx.geometry.Insets(20));

        VBox layout = new VBox(10, labelInstruction, row, backButton, infoPassiveButton);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/styleSheet/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    public void cacherJeton(Jeton jeton) {
        VBox groupe = jetonGroupes.get(jeton);
        if (groupe != null) {
            groupe.setVisible(false);
            groupe.setManaged(false);
        }
    }

    public Scene getScene() { return scene; }
    public StackPane getBackButton() { return backButton; }
    public Button getInfoPassiveButton() {return infoPassiveButton; }
    public Map<Jeton, ImageView> getJetonViews() { return jetonViews; }
    public Label getLabelInstruction() { return labelInstruction; }
}