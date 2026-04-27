package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.models.GameConfig;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;

public class HomePageController {

    private final HomePageView view;
    private final Partie       model;
    private GameConfig gameConfig;
    public HomePageController(HomePageView view, Partie model) {
        this.view  = view;
        this.model = model;
        bindEvents();
    }

    private void bindEvents() {
        // Hover visuel
        for (ImageView iv : new ImageView[]{view.getPlayButton(), view.getOptionButton(), view.getQuitButton()}) {
            iv.setCursor(Cursor.HAND);
            iv.setOnMouseEntered(_ -> iv.setOpacity(0.8));
            iv.setOnMouseExited(_  -> iv.setOpacity(1.0));
        }

        view.getQuitButton().setOnMouseClicked(_ -> Platform.exit());

        view.getPlayButton().setOnMouseClicked(_ -> {
            ChoixJoueursView choixView = new ChoixJoueursView(
                    SceneManager.getStage(), view.getBackground());
            new ChoixJoueursController(choixView, model, view,gameConfig);
            SceneManager.show(choixView.getScene(), "Choix des joueurs");
        });

        view.getOptionButton().setOnMouseClicked(_ -> {
            OptionsView optView = new OptionsView(SceneManager.getStage(), view.getBackground());
            new OptionsController(optView, view, SceneManager.getPlayer());
            SceneManager.show(optView.getScene(), "Options");
        });
    }
}
