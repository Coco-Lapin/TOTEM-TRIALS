package com.totemtrials.totemtrials.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class RulesPopup {

    private static final double W = 860;
    private static final double H = 640;
    // zone parchemin (hors bordures pierre)
    private static final double PARCH_W = W * 0.65;
    private static final double COL_W   = PARCH_W * 0.46;

    private final StackPane vue;

    public RulesPopup(StackPane container, Runnable onClose) {

        ImageView bgView = new ImageView(new Image(Objects.requireNonNull(
            RulesPopup.class.getResourceAsStream("/images/questions/backgroundQuestions.png")
        )));
        bgView.setFitWidth(W);
        bgView.setFitHeight(H);
        bgView.setPreserveRatio(false);

        // ── Titre ────────────────────────────────────────────────────
        Label title = new Label("TOTEM TRIALS — RULES");
        title.setFont(Font.font("Impact", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #3D1C02;");
        VBox.setMargin(title, new Insets(0, 0, 8, 0));

        // ── Colonne gauche ───────────────────────────────────────────
        VBox left = col(
            section("GOAL",
                row("First player to reach FINISH wins.")),
            section("YOUR TURN",
                row("Pawn moves 1 space, then tile activates.")),
            section("QUIZ TILES",
                row("Choose difficulty 1–4."),
                row("Correct → advance that many spaces."),
                row("Wrong → back 1 (lvl 1–2) or 2 (lvl 3–4)."))
        );

        // ── Colonne droite ───────────────────────────────────────────
        VBox right = col(
            section("VERSUS",
                row("Both answer same question."),
                row("Win: +4 spaces  |  Lose: -4 spaces.")),
            section("HOP — SHORTCUT",
                row("Skip: +1  |  Win: +6  |  Lose: -3.")),
            section("TOKEN PASSIVES",
                row("Tiger    — +1 on VERSUS win."),
                row("Eagle    — +1 on correct answer."),
                row("Snake    — -1 on VERSUS defeat."),
                row("Elephant — -1 on wrong answer."))
        );

        Region vSep = new Region();
        vSep.setPrefWidth(1);
        vSep.setStyle("-fx-background-color: #8b6030;");

        HBox columns = new HBox(left, vSep, right);
        columns.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(left,  Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        // ── Bouton close ─────────────────────────────────────────────
        Button closeBtn = new Button("Close");
        closeBtn.setFont(Font.font("Impact", FontWeight.BOLD, 13));
        closeBtn.setStyle(
            "-fx-background-color: #5C3A1E; -fx-text-fill: #F5DEB3;" +
            "-fx-background-radius: 6; -fx-cursor: hand;"
        );
        closeBtn.setMinWidth(90);
        closeBtn.setOnAction(e -> onClose.run());
        VBox.setMargin(closeBtn, new Insets(10, 0, 0, 0));

        // ── Contenu centré sur le parchemin ──────────────────────────
        VBox content = new VBox(8, title, columns, closeBtn);
        content.setAlignment(Pos.CENTER);
        content.setPrefWidth(PARCH_W);
        content.setMaxWidth(PARCH_W);
        // padding vertical pour entrer dans la zone parchemin (hors bordures pierre)
        content.setPadding(new Insets(H * 0.13, 0, H * 0.09, 0));

        StackPane popup = new StackPane(bgView, content);
        popup.setPrefWidth(W);
        popup.setPrefHeight(H);
        popup.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 0);");

        vue = new StackPane(popup);
        vue.setPickOnBounds(false);
    }

    private VBox col(VBox... sections) {
        VBox col = new VBox(10);
        col.setPadding(new Insets(0, 10, 0, 10));
        col.getChildren().addAll(sections);
        return col;
    }

    private VBox section(String headerText, Label... rows) {
        Label h = new Label(headerText);
        h.setFont(Font.font("Impact", FontWeight.BOLD, 15));
        h.setStyle("-fx-text-fill: #6B2E00;");
        VBox box = new VBox(2, h);
        box.getChildren().addAll(rows);
        return box;
    }

    private Label row(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", 13));
        l.setStyle("-fx-text-fill: #2D1A0A;");
        l.setWrapText(true);
        l.setMaxWidth(COL_W);
        return l;
    }

    public StackPane getVue() { return vue; }
}
