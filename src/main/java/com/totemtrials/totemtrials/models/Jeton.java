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
    public Jeton(String imagePath) {
        // On appelle le constructeur principal avec des valeurs par défaut
        // On essaie d'extraire le nom depuis le chemin de l'image
        this(extraireNom(imagePath), "Passif par défaut", imagePath, "");
    }

    // Petite méthode utilitaire pour donner un nom cohérent
    private static String extraireNom(String path) {
        if (path.contains("Elephant")) return "Elephant";
        if (path.contains("Serpent"))  return "Serpent";
        if (path.contains("Aigle"))    return "Aigle";
        if (path.contains("Tigre"))    return "Tigre";
        return "Inconnu";
    }

    public String getNom()           { return nom; }
    public String getImagePath()     { return imagePath; }
    public String getAnimationPath() { return animationPath; }
    public String getPassif()       { return passif; }
}
