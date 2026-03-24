package com.totemtrials.totemtrials;

public class Joueur {

    private String nom;
    private Jeton jetonJoueur;

    public Joueur(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Jeton getJetonJoueur() {
        return jetonJoueur;
    }
    public void setJetonJoueur(Jeton jetonJoueur) {
        this.jetonJoueur = jetonJoueur;
    }



}
