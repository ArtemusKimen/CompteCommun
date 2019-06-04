package com.prod.artemus.comptecommun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class ResultatsFragment extends Fragment {

    private CompteCommun compteCommun;
    private ProgressBar cercle;
    private TextView balance;
    private TextView balanceValeur;
    private TextView totalRevenus;
    private TextView totalDepenses;
    private TextView totalApports;
    private ImageView imageCommun;
    private TextView nomCommun;
    private TextView apportCommun;
    private TextView resteCommun;
    private SeekBar seekBarCommun;
    private ImageView imageP1;
    private TextView nomP1;
    private TextView apportP1;
    private TextView resteP1;
    private SeekBar seekBarP1;
    private ImageView imageP2;
    private TextView nomP2;
    private TextView apportP2;
    private TextView resteP2;
    private SeekBar seekBarP2;
    private double sommeDepenses;
    private double sommeApports;
    private double valeurBalance;


    public ResultatsFragment() {
    }

    public static ResultatsFragment creation (CompteCommun cc){
        Bundle args = new Bundle();
        args.putSerializable("compte", cc);
        ResultatsFragment fragment = new ResultatsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resultats, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compteCommun = (CompteCommun) getArguments().getSerializable("compte");
        cercle = view.findViewById(R.id.cercle);
        balance = view.findViewById(R.id.balance);
        balanceValeur = view.findViewById(R.id.valeurBalance);
        totalDepenses = view.findViewById(R.id.totalDepenses);
        totalApports = view.findViewById(R.id.totalApports);
        imageCommun = view.findViewById(R.id.resultat_imageCommun);
        nomCommun = view.findViewById(R.id.resultat_nomCommun);
        apportCommun = view.findViewById(R.id.resultat_apportCommun);
        resteCommun = view.findViewById(R.id.resultat_resteCommun);
        imageP1 = view.findViewById(R.id.resultat_imageP1);
        nomP1 = view.findViewById(R.id.resultat_nomP1);
        apportP1 = view.findViewById(R.id.resultat_apportP1);
        resteP1 = view.findViewById(R.id.resultat_resteP1);
        seekBarP1 = view.findViewById(R.id.seekBar_P1);
        imageP2 = view.findViewById(R.id.resultat_imageP2);
        nomP2 = view.findViewById(R.id.resultat_nomP2);
        apportP2 = view.findViewById(R.id.resultat_apportP2);
        resteP2 = view.findViewById(R.id.resultat_resteP2);
        seekBarP2 = view.findViewById(R.id.seekBar_P2);

        rafraichissementAffichage();

        seekBarP1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if (fromUser) {
                    compteCommun.setManuel(true);
                    compteCommun.setApportP1(progressValue);
                }
                rafraichissementAffichage();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarP2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if (fromUser) {
                    compteCommun.setManuel(true);
                    compteCommun.setApportP2(progressValue);
                }
                rafraichissementAffichage();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public void rafraichissementAffichage(){
        getActivity().invalidateOptionsMenu();
        sommeApports = compteCommun.getSommeApports();
        sommeDepenses = compteCommun.getTotalDepenses();
        valeurBalance = BoiteAOutils.arrondi(sommeApports-sommeDepenses);

        //Affichage différent de la progressBar selon si l'apport est suffisant ou pas.
        if(sommeApports<sommeDepenses){
            //Lorsque il manque de l'argent
            cercle.setProgressDrawable(getResources().getDrawable(R.drawable.cercle_bad));
            cercle.setMax((int)sommeDepenses);
            cercle.setProgress((int)sommeApports);
            balance.setText(R.string.manque);
            balanceValeur.setText(NumberFormat.getCurrencyInstance().format(valeurBalance));
            balanceValeur.setTextColor(getResources().getColor(R.color.rouge));
        }
        else {
            //Lorsque il y a un excedent d'argent
            cercle.setProgressDrawable(getResources().getDrawable(R.drawable.cercle_good));
            cercle.setMax((int)sommeDepenses);
            cercle.setProgress((int)(valeurBalance));
            balance.setText(R.string.epargne);
            balanceValeur.setText("+" + NumberFormat.getCurrencyInstance().format(valeurBalance));
            balanceValeur.setTextColor(getResources().getColor(R.color.vert));
        }

        //Initialisation des valeurs
        totalDepenses.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getTotalDepenses()));
        totalApports.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getSommeApports()));
        imageCommun.setImageResource(compteCommun.getCommun().quelleIcone());
        nomCommun.setText(compteCommun.getCommun().getNom());
        apportCommun.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getApportCommun()));
        resteCommun.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getResteCommun()));
        imageP1.setImageResource(compteCommun.getP1().quelleIcone());
        nomP1.setText(compteCommun.getP1().getNom());
        apportP1.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getApportP1()));
        resteP1.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getResteP1()));
        seekBarP1.setMax((int)compteCommun.getRecettesP1());
        seekBarP1.setProgress((int)compteCommun.getApportP1());
        imageP2.setImageResource(compteCommun.getP2().quelleIcone());
        nomP2.setText(compteCommun.getP2().getNom());
        apportP2.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getApportP2()));
        resteP2.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getResteP2()));
        seekBarP2.setMax((int)compteCommun.getRecettesP2());
        seekBarP2.setProgress((int)compteCommun.getApportP2());
    }

}