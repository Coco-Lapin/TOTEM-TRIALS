package com.totemtrials.totemtrials;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Jeton {

    private String nom;
    private final ImageView imageBase;
    private final ImageView imageAnimation;

    public Jeton(String nom, ImageView image, ImageView animation){
        this.nom = nom;
        this.imageBase = image;
        this.imageAnimation = animation;
    }

    public String getNom() {
        return nom;
    }

    public Node getImageBase() {
        return imageBase;
    }

    public Image getImageAnimation() {
        return imageAnimation.getImage();
    }

}
