package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.Jeton;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChoixJetonsView {

    private final Button              backButton;
    private final Scene               scene;
    /** Jeton model → ImageView affiché (utilisé par le controller pour bind onClick) */
    private final Map<Jeton, ImageView> jetonViews = new LinkedHashMap<>();

    public ChoixJetonsView(Stage stage, Image background, Jeton[] jetons) {
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        HBox row = new HBox(10);
        row.setAlignment(Pos.TOP_CENTER);

        for (Jeton jeton : jetons) {
            ImageView base = ViewUtils.createCroppedImageView(stage, jeton.getImagePath(), 0.10);
            ImageView anim = ViewUtils.createCroppedImageView(stage, jeton.getAnimationPath(), 0.10);

            // Snap-shot pour le retour au PNG
            Image      fixeImage    = base.getImage();
            Rectangle2D fixeViewport = base.getViewport();
            Image      animImage    = anim.getImage();

            base.setOnMouseEntered(_ -> {
                base.setImage(animImage);
                base.setViewport(null);
            });
            base.setOnMouseExited(_ -> {
                base.setImage(fixeImage);
                base.setViewport(fixeViewport);
            });

            jetonViews.put(jeton, base);
            row.getChildren().add(base);
        }

        VBox layout = new VBox(10, row, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/com/totemtrials/totemtrials/styles/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    public Scene                  getScene()      { return scene; }
    public Button                 getBackButton() { return backButton; }
    public Map<Jeton, ImageView>  getJetonViews() { return jetonViews; }
}
