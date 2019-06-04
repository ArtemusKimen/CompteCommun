package com.prod.artemus.comptecommun;

import java.io.Serializable;
import java.util.Observable;

public class Depense extends Observable implements Serializable{

    private String nom;
    private double valeur;

    public Depense() {
        nom = "";
        valeur = 0.0;
    }

    public Depense(String n, Double v){
        nom = n;
        valeur = BoiteAOutils.arrondi(v);
    }

    public String getNom() {
        return nom;
    }

    public double getValeur() {
        return valeur;
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

    public void remplace(Depense d) {
        nom = d.getNom();
        valeur = BoiteAOutils.arrondi(d.getValeur());
        setChanged();
        notifyObservers();
    }

    public String toString() {
        return "Dépense : " + nom + " = " + valeur;
    }
}