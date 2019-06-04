package com.prod.artemus.comptecommun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfilsActivity extends AppCompatActivity implements ImageDialog.ImageDialogListener {

    private Personne p1;
    private Personne p2;
    private EditText nomP1, nomP2;
    private ImageButton imageP1, imageP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profils);
        Toolbar toolbarProfils = (Toolbar) findViewById(R.id.toolbar_profils);
        setSupportActionBar(toolbarProfils);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.profils);

        Intent intent = getIntent();
        p1 = (Personne) intent.getSerializableExtra("P1");
        p2 = (Personne) intent.getSerializableExtra("P2");

        imageP1 = findViewById(R.id.imageP1);
        nomP1 = findViewById(R.id.nomP1);
        imageP2 = findViewById(R.id.imageP2);
        nomP2 = findViewById(R.id.nomP2);

        imageP1.setImageResource(p1.quelleIcone());
        nomP1.setText(p1.getNom());
        imageP2.setImageResource(p2.quelleIcone());
        nomP2.setText(p2.getNom());

        imageP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new ImageDialog();
                dialog = ImageDialog.creation(1, p1.getIdIcone());
                dialog.show(getSupportFragmentManager(), "ImageDialog");
            }
        });

        imageP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new ImageDialog();
                dialog = ImageDialog.creation(2, p2.getIdIcone());
                dialog.show(getSupportFragmentManager(), "ImageDialog");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profils, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                // Annulation des modification et retour à la MainActivity
                this.finish();
                return true;
            case R.id.action_sauvegarder:
                // Sauvegarde des modification et retour à la MainActivity
                if(nomP1.getText().toString().equals(""))
                    p1.setNom(getString(R.string.profil1));
                else
                    p1.setNom(nomP1.getText().toString());
                if(nomP2.getText().toString().equals(""))
                    p2.setNom(getString(R.string.profil2));
                else
                    p2.setNom(nomP2.getText().toString());
                Intent donnéesRetournées = new Intent();
                donnéesRetournées.putExtra("P1Modifié", p1);
                donnéesRetournées.putExtra("P2Modifié", p2);
                setResult(RESULT_OK, donnéesRetournées);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Appelée lorsqu'on enregistre le changement d'image
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
