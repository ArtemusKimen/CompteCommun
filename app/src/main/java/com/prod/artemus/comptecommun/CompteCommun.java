package com.prod.artemus.comptecommun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CompteCommun extends Observable implements Serializable{

    private static final long serialVersionUID = 1L;
    private Personne commun;
    private Personne p1;
    private Personne p2;
    private List<Depense> depenses = new ArrayList<Depense>();
    private List<Recette> recettes = new ArrayList<Recette>();
    private double totalDepenses = 0.0;
    private double depensesRestantes = 0.0;
    private double totalRecettes = 0.0;
    private double recettesCommun = 0.0;
    private double recettesP1 = 0.0;
    private double recettesP2 = 0.0;
    private double apportCommun = 0.0;
    private double apportP1 = 0.0;
    private double apportP2 = 0.0;
    private double resteCommun = 0.0;
    private double resteP1 = 0.0;
    private double resteP2 = 0.0;
    private double sommeApports = 0.0;
    private double pourcentageCommun = 0.0;
    private double pourcentageP1 = 0.0;
    private double pourcentageP2 = 0.0;
    private boolean manuel;

    public CompteCommun() {
        commun = new Personne(0, "Commun", 0);
        p1 = new Personne(1, "Vincent", 1);
        p2 = new Personne(2, "Cynthia", 2);
        manuel = false;
        calculsResultats();
    }

    public CompteCommun(Personne _pc, Personne _p1, Personne _p2) {
        commun = new Personne(0, _pc.getNom(), 0);
        p1 = new Personne(1, _p1.getNom(), 1);
        p2 = new Personne(2, _p2.getNom(), 2);
        manuel = false;
        calculsResultats();
    }

    //Calcul des résultats
    public void calculsResultats() {
        if(manuel) { //Si le mode manuel est ON
            calculTotalDepenses();
            calculTotalRecettes();
            calculRecettesCommun();
            calculRecettesP1();
            calculRecettesP2();
        }
        else { //Si le mode manuel est OFF
            calculTotalDepenses();
            calculTotalRecettes();
            calculRecettesCommun();
            calculRecettesP1();
            calculRecettesP2();
            calculDepensesRestantes();
            calculApports();
            calculPourcentages();
        }
    }

    public void calculTotalDepenses() {
        totalDepenses=0.0;
        for (Depense d : depenses) {
            totalDepenses += d.getValeur();
        }
        totalDepenses = BoiteAOutils.arrondi(totalDepenses);
    }

    public void calculDepensesRestantes() {
        calculTotalDepenses();
        calculRecettesCommun();
        depensesRestantes=totalDepenses - recettesCommun;
        depensesRestantes = BoiteAOutils.arrondi(depensesRestantes);
    }

    public void calculTotalRecettes() {
        totalRecettes=0.0;
        for (Recette r : recettes) {
            totalRecettes += r.getValeur();
        }
        totalRecettes = BoiteAOutils.arrondi(totalRecettes);
    }

    public void calculRecettesCommun() {
        recettesCommun=0.0;
        for (Recette r : recettes) {
            if(r.getPersonne().getId()==0)
                recettesCommun += r.getValeur();
        }
        recettesCommun = BoiteAOutils.arrondi(recettesCommun);
    }

    public void calculRecettesP1() {
        recettesP1=0.0;
        for (Recette r : recettes) {
            if(r.getPersonne().getId()==1)
                recettesP1 += r.getValeur();
        }
        recettesP1 = BoiteAOutils.arrondi(recettesP1);
    }

    public void calculRecettesP2() {
        recettesP2=0.0;
        for (Recette r : recettes) {
            if(r.getPersonne().getId()==2)
                recettesP2 += r.getValeur();
        }
        recettesP2 = BoiteAOutils.arrondi(recettesP2);
    }

    public void calculApports() {
        //Cas où les dépenses sont entièrement absorbés par les apports communs
        if (depensesRestantes <= 0) {
            apportCommun = totalDepenses;
            apportP1 = 0.0;
            apportP2 = 0.0;
        }
        else {
            apportCommun = recettesCommun;
            if (recettesP1 != 0 || recettesP2 != 0) {
                apportP1 = BoiteAOutils.arrondi((recettesP1*depensesRestantes)/(recettesP2+recettesP1));
                apportP2 = BoiteAOutils.arrondi((recettesP2*depensesRestantes)/(recettesP1+recettesP2));
                if (apportP1 > recettesP1)
                    apportP1 = recettesP1;
                if (apportP2 > recettesP2)
                    apportP2 = recettesP2;
            }
            else {
                apportP1 = 0.0;
                apportP2 = 0.0;
            }
        }
        calculSommeApports();
        calculResteCommun();
        calculResteP1();
        calculResteP2();
    }

    public void calculResteCommun() {
        resteCommun=recettesCommun-apportCommun;
        resteCommun=BoiteAOutils.arrondi(resteCommun);
    }

    public void calculResteP1() {
        resteP1=recettesP1-apportP1;
        resteP1=BoiteAOutils.arrondi(resteP1);
    }

    public void calculResteP2() {
        resteP2=recettesP2-apportP2;
        resteP2=BoiteAOutils.arrondi(resteP2);
    }

    public void calculSommeApports(){
        sommeApports=apportP1+apportP2+apportCommun;
        sommeApports=BoiteAOutils.arrondi(sommeApports);
    }

    public void calculPourcentages() {
        pourcentageCommun = ((apportCommun*100)/recettesCommun);
        pourcentageP1 = ((apportP1*100)/recettesP1);
        pourcentageP2 = ((apportP2*100)/recettesP2);
    }

    public void ajoutDepense(String n, double v){
        depenses.add(new Depense(n,v));
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void ajoutDepense(Depense d){
        depenses.add(d);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void ajoutDepense(int i, Depense d){
        depenses.add(i, d);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void remplaceDepense(int i, Depense d) {
        depenses.set(i, d);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void modificationDepense(){
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void supprimeDepense(Depense d){
        depenses.remove(d);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void echangeDepenses(Depense depense1, int position1, Depense depense2, int position2){
        depenses.set(position2, depense1); //La depense 1 va à la position 2
        depenses.set(position1, depense2); //La depense 2 va à la position 1
        setChanged();
        notifyObservers();
    }

    public void ajoutRecette(Personne p, String n, double v){
        recettes.add(new Recette(p,n,v));
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void ajoutRecette(Recette r){
        recettes.add(r);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void ajoutRecette(int i, Recette r){
        recettes.add(i, r);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void remplaceRecette(int i, Recette r){
        recettes.set(i,r);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void echangeRecettes(Recette recette1, int position1, Recette recette2, int position2){
        recettes.set(position2,recette1); //La recette 1 va à la position 2
        recettes.set(position1,recette2); //La recette 2 va à la position 1
        setChanged();
        notifyObservers();
    }

    public void modificationRecette(){
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public void supprimeRecette(Recette r){
        recettes.remove(r);
        setManuel(false);
        calculsResultats();
        setChanged();
        notifyObservers();
    }

    public Personne getCommun() {
        return commun;
    }

    public Personne getP1() {
        return p1;
    }

    public Personne getP2() {
        return p2;
    }

    public void setPCommun(Personne p) {
        commun.setNom(p.getNom());
        commun.setIdIcone(p.getIdIcone());
        setChanged();
        notifyObservers();
    }

    public void setP1(Personne p) {
        p1.setNom(p.getNom());
        p1.setIdIcone(p.getIdIcone());
        setChanged();
        notifyObservers();
    }

    public void setP2(Personne p) {
        p2.setNom(p.getNom());
        p2.setIdIcone(p.getIdIcone());
        setChanged();
        notifyObservers();
    }

    public List<Depense> getDepenses() {
        return depenses;
    }

    public List<Recette> getRecettes() {
        return recettes;
    }

    public double getTotalDepenses() {
        return totalDepenses;
    }

    public double getDepensesRestantes() {
        return depensesRestantes;
    }

    public double getTotalRecettes() {
        return totalRecettes;
    }

    public double getRecettesCommun() {
        return recettesCommun;
    }

    public double getRecettesP1() {
        return recettesP1;
    }

    public double getRecettesP2() {
        return recettesP2;
    }

    public double getApportCommun() {
        return apportCommun;
    }

    public double getApportP1() {
        return apportP1;
    }

    public double getApportP2() {
        return apportP2;
    }

    public void setApportCommun(int i){
        apportCommun = i;
        calculSommeApports();
        calculResteCommun();
    }

    public void setApportP1(int i){
        apportP1 = i;
        calculSommeApports();
        calculResteP1();
    }

    public void setApportP2(int i){
        apportP2 = i;
        calculSommeApports();
        calculResteP2();
    }

    public double getSommeApports() {
        return sommeApports;
    }

    public double getPourcentageCommun() {
        return pourcentageCommun;
    }

    public double getPourcentageP1() {
        return pourcentageP1;
    }

    public double getPourcentageP2() {
        return pourcentageP2;
    }

    public double getResteCommun() {
        return resteCommun;
    }

    public double getResteP1() {
        return resteP1;
    }

    public double getResteP2() {
        return resteP2;
    }

    public boolean getManuel(){
        return manuel;
    }

    public void setManuel(boolean m){
        manuel = m;
    }

}