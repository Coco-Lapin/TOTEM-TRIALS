package com.totemtrials.totemtrials.model;

public class Jeton {

    private final String nom;
    private final String imagePath;
    private final String animationPath;

    public Jeton(String nom, String imagePath, String animationPath) {
        this.nom           = nom;
        this.imagePath     = imagePath;
        this.animationPath = animationPath;
    }

    public String getNom()           { return nom; }
    public String getImagePath()     { return imagePath; }
    public String getAnimationPath() { return animationPath; }
}
