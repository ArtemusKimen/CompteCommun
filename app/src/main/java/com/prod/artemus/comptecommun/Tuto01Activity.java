package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Tuto01Activity extends AppCompatActivity implements ImageDialog.ImageDialogListener {

    private Personne p1;
    private Personne p2;
    private EditText nomP1, nomP2;
    private ImageButton imageP1;
    private ImageButton imageP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto01);

        Intent intent = getIntent();
        p1 = (Personne) intent.getSerializableExtra("P1");
        p2 = (Personne) intent.getSerializableExtra("P2");

        imageP1 = findViewById(R.id.imageP1);
        nomP1 = findViewById(R.id.nomP1);
        imageP2 = findViewById(R.id.imageP2);
        nomP2 = findViewById(R.id.nomP2);
        ImageButton next01 = findViewById(R.id.next01);

        imageP1.setImageResource(p1.quelleIcone());
        //nomP1.setText(p1.getNom());
        imageP2.setImageResource(p2.quelleIcone());
        //nomP2.setText(p2.getNom());

        imageP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = ImageDialog.creation(1, p1.getIdIcone());
                dialog.show(getSupportFragmentManager(), "ImageDialog");
            }
        });

        imageP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = ImageDialog.creation(2, p2.getIdIcone());
                dialog.show(getSupportFragmentManager(), "ImageDialog");
            }
        });

        next01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nomP1.getText().toString().equals(""))
                    p1.setNom(getString(R.string.profil1));
                else
                    p1.setNom(nomP1.getText().toString());
                if(nomP2.getText().toString().equals(""))
                    p2.setNom(getString(R.string.profil2));
                else
                    p2.setNom(nomP2.getText().toString());
                Intent donneesRetournees = new Intent();
                donneesRetournees.putExtra("P1Modifié", p1);
                donneesRetournees.putExtra("P2Modifié", p2);
                setResult(RESULT_OK, donneesRetournees);
                finish();
            }
        });
    }

    //Réaction lorsqu'on sélectionne une image dans le profil
    @Override
    public void enregistrementImage(DialogFragment dialog) {
        if (((ImageDialog) dialog).getIdPersonne()==1) {
            p1.setIdIcone(((ImageDialog) dialog).getIconeSelectionne());
            imageP1.setImageResource(p1.quelleIcone());
        }
        else{
            p2.setIdIcone(((ImageDialog) dialog).getIconeSelectionne());
            imageP2.setImageResource(p2.quelleIcone());
        }
        dialog.dismiss();
    }

}
