package com.prod.artemus.comptecommun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class RecetteDialog extends DialogFragment {

    public interface RecetteDialogListener {
        public void enregistrementRecette(DialogFragment dialog);
        public void annulationRecette(DialogFragment dialog);
    }

    private int typeDialog; //0 pour nouvelle Recette, 1 pour modification recette;
    private Recette recette;
    private int numRecette;
    private Personne commun;
    private Personne p1;
    private Personne p2;
    private RecetteDialogListener mListener;
    private EditText nom, valeur;
    private RadioButton radio00;
    private RadioButton radio01;
    private RadioButton radio02;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_recette, null);
        builder.setView(view)
                // Définition des boutons du DialogFragment
                .setPositiveButton(R.string.enregistrer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Signale à l'activité qu'un click a été fait sur le bouton Enregistrer
                        mListener.enregistrementRecette(RecetteDialog.this);
                    }
                })
                .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Signale à l'activité qu'un click a été fait sur le bouton Annuler
                        mListener.annulationRecette(RecetteDialog.this);
                    }
                });

        nom = view.findViewById(R.id.nom);
        valeur = view.findViewById(R.id.valeur);
        radio00 = view.findViewById(R.id.radio00);
        radio01 = view.findViewById(R.id.radio01);
        radio02 = view.findViewById(R.id.radio02);

        //Remplissage du DialogFragment avec les informations de la recette sélectionnée
        typeDialog = getArguments().getInt("typeRecette");
        if(typeDialog==1) { //Si on modifie un Recette...
            recette = (Recette) getArguments().getSerializable("recette");
            numRecette = getArguments().getInt("numRecette");
            nom.setText(recette.getNom());
            valeur.setText(Double.toString(recette.getValeur()));
            switch (recette.getPersonne().getId()) {
                case 0:
                    radio00.setChecked(true);
                    break;
                case 1:
                    radio01.setChecked(true);
                    break;
                case 2:
                    radio02.setChecked(true);
                    break;
                default:
                    radio00.setChecked(true);
                    break;
            }
        }
        commun = (Personne) getArguments().getSerializable("commun");
        p1 = (Personne) getArguments().getSerializable("p1");
        p2 = (Personne) getArguments().getSerializable("p2");
        Drawable image00 = getContext().getResources().getDrawable(commun.quelleIcone());
        image00.setBounds( 0, 0, 60, 60 );
        radio00.setCompoundDrawables(image00,null, null, null);
        radio00.setText(commun.getNom());
        Drawable image01 = getContext().getResources().getDrawable(p1.quelleIcone());
        image01.setBounds( 0, 0, 60, 60 );
        radio01.setCompoundDrawables(image01,null, null, null);
        radio01.setText(p1.getNom());
        Drawable image02 = getContext().getResources().getDrawable(p2.quelleIcone());
        image02.setBounds( 0, 0, 60, 60 );
        radio02.setCompoundDrawables(image02,null, null, null);
        radio02.setText(p2.getNom());

        return builder.create();
    }

    //Permet au DialogFragment de récupérer les information à afficher
    public static RecetteDialog creation (int t, Recette r, int i, Personne pc, Personne p1, Personne p2){
        Bundle args = new Bundle();
        args.putInt("typeRecette", t);
        args.putSerializable("recette", r);
        args.putInt("numRecette", i);
        args.putSerializable("commun", pc);
        args.putSerializable("p1", p1);
        args.putSerializable("p2", p2);
        RecetteDialog dialog = new RecetteDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérification que l'activité implémente l'interface d'écoute
        try {
            mListener = (RecetteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " n'implémente pas RecetteDialogListener");
        }
    }

    public int getTypeDialog(){
        return typeDialog;
    }

    public Recette getRecette(){
        return recette;
    }

    public int getNumRecette(){
        return numRecette;
    }

    //Retourne le nom saisie
    public String getNom() {
        if(nom.getText().toString().equals(""))
            return getString(R.string.recette);
        else
            return nom.getText().toString();
    }

    //Retourne la valeur saisie
    public Double getValeur() {
        try {
            return Double.parseDouble(valeur.getText().toString().replace(',', '.'));
        }catch (NumberFormatException nfe){
            return 0.0;
        }
    }

    //Retourne la personne sélectionnée
    public Personne getPersonneSelected() {
        if (radio00.isChecked())
            return commun;
        if (radio01.isChecked())
        return p1;
        if (radio02.isChecked())
            return p2;
        return commun;
    }


}
