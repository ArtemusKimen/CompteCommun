package com.prod.artemus.comptecommun;

import java.io.Serializable;
import java.util.Observable;

public class Recette extends Observable implements Serializable{

    private Personne personne;
    private String nom;
    private double valeur;

    public Recette() {
        personne = new Personne(0, "Commun", 0);
        nom = "";
        valeur = 0.0;
    }

    public Recette(Personne p, String n, Double v){
        personne = p;
        nom = n;
        valeur = BoiteAOutils.arrondi(v);
    }

    public Personne getPersonne() {
        return personne;
    }

    public String getNom() {
        return nom;
    }

    public double getValeur() {
        return valeur;
    }

    public void setPersonne(Personne p) {
        personne = p;
        setChanged();
        notifyObservers();
    }

    public void setNom(String n) {
        nom = n;
        setChanged();
        notifyObservers();
    }

    public void setValeur(double v) {
        valeur = BoiteAOutils.arrondi(v);
        setChanged();
        notifyObservers();
    }

    public void remplace(Recette r) {
        personne = r.getPersonne();
        nom = r.getNom();
        valeur = BoiteAOutils.arrondi(r.getValeur());
        setChanged();
        notifyObservers();
    }

    public String toString() {
        return "Recette de " + personne.getNom() + " : " + nom + " = " + valeur;
    }

}