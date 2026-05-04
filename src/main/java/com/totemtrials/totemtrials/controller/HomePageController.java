package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.*;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import com.totemtrials.totemtrials.view.RulesPopup;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class HomePageController {

    private final HomePageView view;
    private final Partie model;

    public HomePageController(HomePageView view, Partie model) {
        this.view  = view;
        this.model = model;
        bindEvents();
    }

    private void bindEvents() {
        for (ImageView iv : new ImageView[]{view.getPlayButton(), view.getOptionButton(), view.getQuitButton(), view.getRulesButton()}) {
            iv.setCursor(Cursor.HAND);
            iv.setOnMouseEntered(_ -> iv.setOpacity(0.8));
            iv.setOnMouseExited(_  -> iv.setOpacity(1.0));
        }

        view.getQuitButton().setOnMouseClicked(_ -> Platform.exit());

        view.getPlayButton().setOnMouseClicked(_ -> {
            ChoixJoueursView choixView = new ChoixJoueursView(
                    SceneManager.getStage(), view.getBackground());
            new ChoixJoueursController(choixView, model, view);
            SceneManager.show(choixView.getScene(), "Chose your token");
        });

        view.getOptionButton().setOnMouseClicked(_ -> {
            OptionsView optView = new OptionsView(SceneManager.getStage(), view.getBackground());
            new OptionsController(optView, view, SceneManager.getPlayer());
            SceneManager.show(optView.getScene(), "Settings");
        });

        view.getRulesButton().setOnMouseClicked(_ -> showRules());
    }

    private void showRules() {
        StackPane root = view.getRootPane();

        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        backdrop.prefWidthProperty().bind(root.widthProperty());
        backdrop.prefHeightProperty().bind(root.heightProperty());
        backdrop.maxWidthProperty().bind(root.widthProperty());
        backdrop.maxHeightProperty().bind(root.heightProperty());

        StackPane[] holder = { null };
        RulesPopup popup = new RulesPopup(root, () -> {
            root.getChildren().remove(holder[0]);
            root.getChildren().remove(backdrop);
        });
        holder[0] = popup.getVue();

        root.getChildren().add(backdrop);
        root.getChildren().add(holder[0]);
    }
}