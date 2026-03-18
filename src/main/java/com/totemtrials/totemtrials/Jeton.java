package com.totemtrials.totemtrials;

import javafx.scene.image.Image;

public class Jeton {

    private String nom;
    private Image imageBase;
    private Image imageAnimation;

    public Jeton(String nom, Image image, Image animation){
        this.nom = nom;
        this.imageBase = image;
        this.imageAnimation = animation;
    }

    public String getNom() {
        return nom;
    }

    public Image getImageBase() {
        return imageBase;
    }

    public Image getImageAnimation() {
        return imageAnimation;
    }

}
