package com.totemtrials.totemtrials.model;

public class Joueur {

    private final String nom;
    private Jeton jeton;

    public Joueur(String nom) {
        this.nom = nom;
    }

    public String getNom()           { return nom; }
    public Jeton  getJeton()         { return jeton; }
    public void   setJeton(Jeton j)  { this.jeton = j; }
}
