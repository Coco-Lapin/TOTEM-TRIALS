package com.totemtrials.totemtrials.models;

public class GameConfig {
    private static final GameConfig INSTANCE = new GameConfig();
    private String[] nomsJoueurs = {"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"}; // Valeurs par défaut


    public String[] getNomsJoueurs() { return nomsJoueurs; }
    public void setNomsJoueurs(String[] noms) { this.nomsJoueurs = noms; }

    private int nbJoueurs = 4;
    private String[] jetonsChoisis = {
            "/images/tokens/jetonElephan.png",
            "/images/tokens/jetonSnake.png",
            "/images/tokens/jetonAigle.png",
            "/images/tokens/jetonTigre.png"
    };

    private GameConfig() {}

    public static GameConfig getInstance() { return INSTANCE; }

    public int getNbJoueurs() { return nbJoueurs; }
    public void setNbJoueurs(int n) { this.nbJoueurs = n; }

    public String[] getJetonsChoisis() { return jetonsChoisis; }
    public void setJetonsChoisis(String[] chemins) { this.jetonsChoisis = chemins; }
}