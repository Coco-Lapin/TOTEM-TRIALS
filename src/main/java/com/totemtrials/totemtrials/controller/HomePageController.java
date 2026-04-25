package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.*;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;

public class HomePageController {

    private final HomePageView view;
    private final Partie model;

    public HomePageController(HomePageView view, Partie model) {
        this.view  = view;
        this.model = model;
        bindEvents();
    }

    private void bindEvents() {
        for (ImageView iv : new ImageView[]{view.getPlayButton(), view.getOptionButton(), view.getQuitButton()}) {
            iv.setCursor(Cursor.HAND);
            iv.setOnMouseEntered(_ -> iv.setOpacity(0.8));
            iv.setOnMouseExited(_  -> iv.setOpacity(1.0));
        }

        view.getQuitButton().setOnMouseClicked(_ -> Platform.exit());

        view.getPlayButton().setOnMouseClicked(_ -> {
            ChoixJoueursView choixView = new ChoixJoueursView(
                    SceneManager.getStage(), view.getBackground());
            new ChoixJoueursController(choixView, model, view);
            SceneManager.show(choixView.getScene(), "Choix des joueurs");
        });

        view.getOptionButton().setOnMouseClicked(_ -> {
            OptionsView optView = new OptionsView(SceneManager.getStage(), view.getBackground());
            new OptionsController(optView, view, SceneManager.getPlayer());
            SceneManager.show(optView.getScene(), "Options");
        });

        view.getTestFinButton().setOnMouseClicked(_ -> {
            Jeton jetonTest = new Jeton("tigre", "passif test",
                    "com/totemtrials/totemtrials/Images/tokkens/jetonTigre.png",
                    "com/totemtrials/totemtrials/Images/tokkens/jetonTigre_anim.gif");

            Joueur j1 = new Joueur("Joueur 1"); j1.setJeton(jetonTest);
            Joueur j2 = new Joueur("Joueur 2"); j2.setJeton(jetonTest);

            StatistiquesJoueur[] stats = new StatistiquesJoueur[2];
            stats[0] = new StatistiquesJoueur(j1); stats[0].setPosition(1); stats[0].incrementerTour();
            stats[1] = new StatistiquesJoueur(j2); stats[1].setPosition(2); stats[1].incrementerTour();

            StatistiquesPartie statsPartie = new StatistiquesPartie(stats, 300);
            FinPartieController.lancerFinPartie(statsPartie, model, view);
        });
    }
}