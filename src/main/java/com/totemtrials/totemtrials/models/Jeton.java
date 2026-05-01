package com.totemtrials.totemtrials.models;

public class Jeton {

    private final String nom;
    private final String passif;
    private final String imagePath;
    private final String animationPath;

    public Jeton(String nom, String passif, String imagePath, String animationPath) {
        this.nom           = nom;
        this.passif = passif;
        this.imagePath     = imagePath;
        this.animationPath = animationPath;
    }

    public String getNom()           { return nom; }
    public String getImagePath()     { return imagePath; }
    public String getAnimationPath() { return animationPath; }
    public String getPassif()       { return passif; }
}
