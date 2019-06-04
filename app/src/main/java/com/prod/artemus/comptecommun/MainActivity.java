package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements  RecetteDialog.RecetteDialogListener,
                                                                RecettesFragment.RecettesListener,
                                                                DepenseDialog.DepenseDialogListener,
                                                                DepensesFragment.DepensesListener{

    static final int MODIFICATION_PROFILS = 0;
    static final int TUTORIAL01 = 1;
    static final int TUTORIAL02 = 2;
    static final int TUTORIAL03 = 3;
    static final int TUTORIAL04 = 4;
    static final String nomFichier = "monCompteCommun";
    private CompteCommun compteCommun;
    TabLayout tabLayout;
    TabLayout.Tab firstTab, secondTab, thirdTab;
    FrameLayout simpleFrameLayout;
    Fragment fragment = null;
    private int tabSelected;


    /********************************
     **    DEFINITION INTERFACE    **
     ********************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha));
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //
        //Définition de l'affichage
        //
        simpleFrameLayout = findViewById(R.id.simpleFrameLayout);
        tabLayout = findViewById(R.id.simpleTabLayout);
        //Premier tab
        firstTab = tabLayout.newTab();
        firstTab.setText(R.string.recettes);
        tabLayout.addTab(firstTab);
        //Deuxieme tab
        secondTab = tabLayout.newTab();
        secondTab.setText(R.string.depenses);
        tabLayout.addTab(secondTab);
        //Troisième tab
        thirdTab = tabLayout.newTab();
        thirdTab.setText(R.string.resultats);
        tabLayout.addTab(thirdTab);


        //
        //Comportement des boutons tab
        //Sélection du fragment et chargement
        //
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabSelected = 0;
                        fragment = new RecettesFragment();
                        fragment = RecettesFragment.creation(compteCommun);
                        break;
                    case 1:
                        tabSelected = 1;
                        fragment = new DepensesFragment();
                        fragment = DepensesFragment.creation(compteCommun);
                        break;
                    case 2:
                        tabSelected = 2;
                        fragment = new ResultatsFragment();
                        fragment = ResultatsFragment.creation(compteCommun);
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //
        //Récupération des données
        //
        if (savedInstanceState != null) {
            compteCommun = (CompteCommun)savedInstanceState.getSerializable("compte_commun");
            thirdTab.select(); //Tab sélectionné par défaut
        } else {
            File fichier = new File(getFilesDir(), nomFichier);
            if(fichier.exists()) {
                chargement();
                thirdTab.select(); //Tab sélectionné par défaut
            }
            else{
                Personne commun = new Personne(0, getString(R.string.commun), 0);
                Personne p1 = new Personne(1, getString(R.string.profil1), 1);
                Personne p2 = new Personne(2, getString(R.string.profil2), 2);
                compteCommun = new CompteCommun(commun, p1, p2);
                Intent intentTuto = new Intent(this, Tuto01Activity.class);
                intentTuto.putExtra("P1", compteCommun.getP1());
                intentTuto.putExtra("P2", compteCommun.getP2());
                startActivityForResult(intentTuto,TUTORIAL01);
                firstTab.select(); //Tab sélectionné par défaut
                fragment = RecettesFragment.creation(compteCommun); //Affichage du fragment Recettes
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("compte_commun", compteCommun);
        super.onSaveInstanceState(savedInstanceState);
    }

    //Menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Affichage bouton "Restaurer" selon si on est en mode manuel ou pas.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem restoreButton = menu.findItem(R.id.action_restore);
        if (tabSelected == 2){
            if(compteCommun.getManuel())
                restoreButton.setVisible(true);
            else
                restoreButton.setVisible(false);
        }
        else
            restoreButton.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    //Comportement des boutons du Menu
    public boolean onOptionsItemSelected(MenuItem item){
        tabSelected = tabLayout.getSelectedTabPosition();
        switch(item.getItemId()){
            case R.id.action_restore: //Bouton Restaure
                compteCommun.setManuel(false);
                compteCommun.calculsResultats();
                supportInvalidateOptionsMenu();

                ((ResultatsFragment)fragment).rafraichissementAffichage();
                return true;
            case R.id.action_profils: //Bouton Profils
                Intent intentProfils = new Intent(this, ProfilsActivity.class);
                intentProfils.putExtra("P1", compteCommun.getP1());
                intentProfils.putExtra("P2", compteCommun.getP2());
                startActivityForResult(intentProfils,MODIFICATION_PROFILS);
                return true;
            case R.id.action_info: //Bouton Info
                Intent intentInfos = new Intent(this, InfosActivity.class);
                startActivity(intentInfos);
                return true;
            case R.id.action_aide: //Bouton Aide
                //Démarrage de l'activité tuto02
                Intent intentTuto02 = new Intent(this, Tuto02Activity.class);
                startActivityForResult(intentTuto02,TUTORIAL02);
        }
        return super.onOptionsItemSelected(item);
    }

    //Actions à exécuter au retour d'une activité
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFICATION_PROFILS) {
            if (resultCode == RESULT_OK) {
                //Récupération des données de profils
                compteCommun.setP1((Personne)data.getSerializableExtra("P1Modifié"));
                compteCommun.setP2((Personne)data.getSerializableExtra("P2Modifié"));
                //Rafraichissement de l'affichage avec les nouvelles données
                if(fragment instanceof RecettesFragment){
                    ((RecettesFragment) fragment).rafraichissement(compteCommun);
                }
                if(fragment instanceof ResultatsFragment){
                    ((ResultatsFragment) fragment).rafraichissementAffichage();
                }
            }
        }
        if (requestCode == TUTORIAL01) {
            if (resultCode == RESULT_OK) {
                //Récupération des données de profils
                compteCommun.setP1((Personne)data.getSerializableExtra("P1Modifié"));
                compteCommun.setP2((Personne)data.getSerializableExtra("P2Modifié"));
                //Démarrage de l'activité tuto02
                Intent intentTuto02 = new Intent(this, Tuto02Activity.class);
                startActivityForResult(intentTuto02,TUTORIAL02);
            }
        }
        if (requestCode == TUTORIAL02) {
            if (resultCode == RESULT_OK) {
                //Démarrage de l'activité tuto03
                Intent intentTuto03 = new Intent(this, Tuto03Activity.class);
                startActivityForResult(intentTuto03,TUTORIAL03);
            }
        }
        if (requestCode == TUTORIAL03) {
            if (resultCode == RESULT_OK) {
                //Démarrage de l'activité tuto03
                Intent intentTuto04 = new Intent(this, Tuto04Activity.class);
                startActivityForResult(intentTuto04,TUTORIAL04);
            }
        }
        if (requestCode == TUTORIAL04) {
            //Fin du tutoriel
        }
    }

    /****************************
     **    GESTION RECETTES    **
     ****************************/

    //Appelé lorsqu'une recette est rajoutée ou modifiée dans le Fragment Recettes
    @Override
    public void enregistrementRecette(DialogFragment dialog) {
        Personne personne;
        String nom;
        Double valeur;

        personne = ((RecetteDialog) dialog).getPersonneSelected();
        nom = ((RecetteDialog) dialog).getNom();
        if (((RecetteDialog) dialog).getValeur()==null)
            valeur = 0.0; //Si la valeur est nulle, elle devient 0.0
        else
            valeur = ((RecetteDialog) dialog).getValeur();
        Recette recetteRecuperee = new Recette(personne, nom, valeur);

        //Est ce que la recette est modifiée ou nouvelle ?
        if(((RecetteDialog)dialog).getTypeDialog() == 0) {
            compteCommun.ajoutRecette(recetteRecuperee);
        }
        else{
            compteCommun.remplaceRecette(((RecetteDialog)dialog).getNumRecette(),recetteRecuperee);
        }

        //Rafraichissement de l'affichage
        if(fragment instanceof RecettesFragment){
            ((RecettesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on annule la modification ou la création d'une recette
    @Override
    public void annulationRecette(DialogFragment dialog) {
        // Aucun changement.
    }

    //Appelé lorsqu'on supprime une recette
    @Override
    public void suppressionRecette(Recette recette) {
        compteCommun.supprimeRecette(recette);
        //Rafraichement de l'affichage
        if(fragment instanceof RecettesFragment){
            ((RecettesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on annule la suppression d'une recette
    @Override
    public void annulerSuppressionRecette(int indexRecette, Recette recette) {
        compteCommun.ajoutRecette(indexRecette, recette);
        //Rafraichissement de l'affichage
        if(fragment instanceof RecettesFragment){
            ((RecettesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on déplace une recette
    @Override
    public void echangeRecettes(Recette recette1, int position1, Recette recette2, int position2) {
        compteCommun.echangeRecettes(recette1, position1, recette2, position2);
    }

    /****************************
     **    GESTION DEPENSES    **
     ****************************/

    //Appelé lorsqu'une dépense est rajoutée ou modifiée dans le Fragment Dépenses
    @Override
    public void enregistrementDepense(DialogFragment dialog) {
        String nom;
        Double valeur;

        nom = ((DepenseDialog) dialog).getNom();
        if (((DepenseDialog) dialog).getValeur()==null)
            valeur = 0.0;
        else
            valeur = ((DepenseDialog) dialog).getValeur();
        Depense depenseRecuperee = new Depense(nom, valeur);

        //Est ce que la recette est modifiée ou nouvelle ?
        if(((DepenseDialog)dialog).getTypeDialog() == 0) {
            compteCommun.ajoutDepense(depenseRecuperee);
        }
        else{
            compteCommun.remplaceDepense(((DepenseDialog)dialog).getNumDepense(),depenseRecuperee);
        }
        //Rafraichissement dépense
        if(fragment instanceof DepensesFragment){
            ((DepensesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on annule la modification ou la création d'une dépense
    @Override
    public void annulationDepense(DialogFragment dialog) {
        // Aucun changement.
    }

    //Appelé lorsqu'on supprime une dépense
    @Override
    public void suppressionDepense(Depense depense) {
        compteCommun.supprimeDepense(depense);
        //Réfraichissement affichage
        if(fragment instanceof DepensesFragment){
            ((DepensesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on annule la suppression d'une dépense
    @Override
    public void annulerSuppressionDepense(int indexDepense, Depense depense) {
        compteCommun.ajoutDepense(indexDepense, depense);
        //Réfraichissement affichage
        if(fragment instanceof DepensesFragment){
            ((DepensesFragment) fragment).rafraichissement(compteCommun);
        }
    }

    //Appelé lorsqu'on déplace une dépense
    @Override
    public void echangeDepenses(Depense depense1, int position1, Depense depense2, int position2) {
        compteCommun.echangeDepenses(depense1, position1, depense2, position2);
    }

    /*****************************************
     **    GESTION SAUVEGARDE/CHARGEMENT    **
     *****************************************/

    @Override
    public void onStop(){
        sauvegarde();
        super.onStop();
    }

    public void sauvegarde() {
        try {
            File fichier = new File(getFilesDir(), nomFichier);
            if (!fichier.exists())
                fichier.createNewFile();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(new FileOutputStream(fichier));
            oos.writeObject(compteCommun);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chargement() {
        try {
            File fichier = new File(getFilesDir(), nomFichier);
            ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(fichier)) ;
            compteCommun = (CompteCommun)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
