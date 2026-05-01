package com.totemtrials.totemtrials.models;

public class Jeton {

    private final String nom;
    private final String passif;
    private final String imagePath;
    private final String animationPath;

    public Jeton(String nom, String passif, String imagePath, String animationPath) {
        this.nom = nom;
        this.passif = passif;
        this.imagePath = imagePath;
        this.animationPath = animationPath;
    }

    public Jeton(String imagePath) {
       this(extraireNom(imagePath), "DEFAULT PASSIVE ", imagePath, "");
    }

    // Petite méthode utilitaire pour donner un nom cohérent
    private static String extraireNom(String path) {
        if (path.contains("Elephant")) return "Elephant";
        if (path.contains("Snake"))  return "Snake";
        if (path.contains("Eagle"))    return "Eagle";
        if (path.contains("Tiger"))    return "Tiger";
        return "Unknown";
    }

    public String getNom()           { return nom; }
    public String getImagePath()     { return imagePath; }
    public String getAnimationPath() { return animationPath; }
    public String getPassif()       { return passif; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Jeton j2){
            return this.nom.equalsIgnoreCase(j2.getNom()) || this.passif.equalsIgnoreCase(j2.getPassif()) ;
        }
        return false;
    }

}
