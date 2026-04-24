package com.totemtrials.totemtrials;

import javafx.scene.image.ImageView;

public class Token {

    private String nom;
    private String passif;
    private final ImageView imageBase;
    private final ImageView imageAnimation;


    public Token(String nom, String passif, ImageView image, ImageView animation) {
        this.nom = nom;
        this.imageBase = image;
        this.imageAnimation = animation;
        this.passif = passif;
    }

    public String getNom() {
        return nom;
    }

    public ImageView getImageBase() {
        return imageBase;
    }

    public ImageView getImageAnimation() {
        return imageAnimation;
    }

    public String getPassif() {
        return passif;
    }
}
