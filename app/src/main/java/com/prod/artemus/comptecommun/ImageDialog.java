package com.prod.artemus.comptecommun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ImageDialog extends DialogFragment {

    public interface ImageDialogListener {
        public void enregistrementImage(DialogFragment dialog);
    }

    private int id;
    private int iconeSelectionne;
    private ImageDialogListener iListener;
    private ImageAdapter iAdapter;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_images, null);
        builder.setView(view);

        //Récupération des informations transmises
        id = getArguments().getInt("id");
        iconeSelectionne = getArguments().getInt("icone");

        final GridView gridview = view.findViewById(R.id.gridview);
        iAdapter = new ImageAdapter(this.getContext());
        gridview.setAdapter(iAdapter);
        iAdapter.setImageSelectionnee(iconeSelectionne);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                iconeSelectionne = (position+1);
                iListener.enregistrementImage(ImageDialog.this);
            }
        });
        return builder.create();
    }

    //Permet au DialogFragment de récupérer les information à afficher
    public static ImageDialog creation (int id, int icone){
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("icone", icone);
        ImageDialog dialog = new ImageDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérification que l'activité implémente l'interface d'écoute
        try {
            iListener = (ImageDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " n'implémente pas ImageDialogListener");
        }
    }

    public int getIdPersonne() {
        return id;
    }

    public int getIconeSelectionne() {
        return iconeSelectionne;
    }

}
