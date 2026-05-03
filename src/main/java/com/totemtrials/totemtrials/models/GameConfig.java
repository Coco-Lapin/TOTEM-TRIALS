package com.totemtrials.totemtrials.models;

public class GameConfig {
    private static final GameConfig INSTANCE = new GameConfig();
    private String[] nomsJoueurs = {"Player 1", "Player 2", "Player 3", "Player 4"}; // Valeurs par défaut


    public String[] getNomsJoueurs() { return nomsJoueurs; }
    public void setNomsJoueurs(String[] noms) { this.nomsJoueurs = noms; }

    private int nbJoueurs = 4;
    private String[] jetonsChoisis = {
            "/images/tokens/ElephantToken.png",
            "/images/tokens/SnakeToken.png",
            "/images/tokens/EagleToken.png",
            "/images/tokens/TigerToken.png"
    };

    private GameConfig() {}

    public static GameConfig getInstance() { return INSTANCE; }

    public int getNbJoueurs() { return nbJoueurs; }
    public void setNbJoueurs(int n) { this.nbJoueurs = n; }

    public String[] getJetonsChoisis() { return jetonsChoisis; }
    public void setJetonsChoisis(String[] chemins) { this.jetonsChoisis = chemins; }
}