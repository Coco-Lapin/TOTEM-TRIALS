package com.totemtrials.totemtrials.models;

public class Joueur {

    private final String nom;
    private Jeton jeton;

    public Joueur(String nom) {
        this.nom = nom;
    }

    public String getNom()           { return nom; }
    public Jeton  getJeton()         { return jeton; }
    public void   setJeton(Jeton j)  { this.jeton = j; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Joueur j2){
            return this.nom.equalsIgnoreCase(j2.getNom());
        }
        return false ;
    }
}
