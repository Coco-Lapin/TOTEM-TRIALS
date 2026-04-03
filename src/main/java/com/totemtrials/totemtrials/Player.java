package com.totemtrials.totemtrials;

public class Player {

    private String nom;
    private Token jetonJoueur;

    public Player(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Token getJetonJoueur() {
        return jetonJoueur;
    }
    public void setJetonJoueur(Token jetonJoueur) {
        this.jetonJoueur = jetonJoueur;
    }



}
