package com.prod.artemus.comptecommun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DepenseDialog extends DialogFragment {

    public interface DepenseDialogListener {
        public void enregistrementDepense(DialogFragment dialog);
        public void annulationDepense(DialogFragment dialog);
    }

    private int typeDialog; //0 pour nouvelle Depense, 1 pour modification depense;
    private Depense depense;
    private int numDepense;
    private DepenseDialogListener mListener;
    private EditText nom, valeur;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_depense, null);
        builder.setView(view)
                // Définition des boutons du DialogFragment
                .setPositiveButton(R.string.enregistrer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Signale à l'activité qu'un click a été fait sur le bouton Enregistrer
                        mListener.enregistrementDepense(DepenseDialog.this);
                    }
                })
                .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Signale à l'activité qu'un click a été fait sur le bouton Annuler
                        mListener.annulationDepense(DepenseDialog.this);
                    }
                });

        nom = view.findViewById(R.id.nom);
        valeur = view.findViewById(R.id.valeur);

        //Remplissage du DialogFragment avec les informations de la depense sélectionnée
        typeDialog = (int) getArguments().getInt("typeDepense");
        if(typeDialog==1) { //Si on modifie un Depense...
            depense = (Depense) getArguments().getSerializable("depense");
            numDepense = (int) getArguments().getInt("numDepense");
            nom.setText(depense.getNom());
            valeur.setText(Double.toString(depense.getValeur()));
        }
        return builder.create();
    }

    //Permet au DialogFragment de récupérer les information à afficher
    public static DepenseDialog creation (int t, Depense d, int i){
        Bundle args = new Bundle();
        args.putInt("typeDepense", t);
        args.putSerializable("depense", d);
        args.putInt("numDepense", i);
        DepenseDialog dialog = new DepenseDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérification que l'activité implémente l'interface d'écoute
        try {
            mListener = (DepenseDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " n'implémente pas DepenseDialogListener");
        }
    }

    public int getTypeDialog(){
        return typeDialog;
    }

    public Depense getDepense(){
        return depense;
    }

    public int getNumDepense(){
        return numDepense;
    }

    //Retourne le nom saisie
    public String getNom() {
        if(nom.getText().toString().equals(""))
            return getString(R.string.depense);
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

}
