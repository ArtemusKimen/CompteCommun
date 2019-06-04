package com.prod.artemus.comptecommun;

import java.io.Serializable;
import java.util.Observable;

public class Personne extends Observable implements Serializable {

    private int id;
    private String nom;
    private int idIcone;

    public Personne(int id, String n, int i) {
        this.id = id;
        this.nom = n;
        this.idIcone = i;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String n) {
        this.nom = n;
        setChanged();
        notifyObservers();
    }

    public int getIdIcone() {
        return idIcone;
    }
    public void setIdIcone(int i) {
        this.idIcone = i;
        setChanged();
        notifyObservers();
    }

    //Méthode permettant de trouver une ressource en fonction d'un idIcone
    public int quelleIcone() {
        switch (idIcone) {
            case 0:
                return(R.drawable.icone00);
            case 1:
                return(R.drawable.icone01);
            case 2:
                return(R.drawable.icone02);
            case 3:
                return(R.drawable.icone03);
            case 4:
                return(R.drawable.icone04);
            case 5:
                return(R.drawable.icone05);
            case 6:
                return(R.drawable.icone06);
            case 7:
                return(R.drawable.icone07);
            case 8:
                return(R.drawable.icone08);
            case 9:
                return(R.drawable.icone09);
            default:
                return(R.drawable.icone00);
        }
    }

    public String toString() {
        return "Id : " + id + ", Nom : " + nom + ", Icone :" + idIcone;
    }

}