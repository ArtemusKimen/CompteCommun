package com.prod.artemus.comptecommun;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder> {

    private final List<Depense> listeDepenses = new ArrayList<Depense>();
    private CompteCommun compteCommun;
    private Context context;
    private final Activity activity;

    public DepenseAdapter(Activity mainActivity) {
        activity = mainActivity;
        context = activity.getApplicationContext();
    }

    public void onArticlesReceived(CompteCommun cc) {
        compteCommun = cc;
        listeDepenses.addAll(compteCommun.getDepenses());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listeDepenses.size();
    }

    public void listClear(){
        listeDepenses.clear();
    }

    public void removeItem(int position) {
        listeDepenses.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Depense depense, int position) {
        listeDepenses.add(position, depense);
        notifyItemInserted(position);
    }

    @Override
    public DepenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cellule_depense, parent, false);
        return new DepenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepenseViewHolder holder, int position) {
        ((DepenseViewHolder) holder).bind(listeDepenses.get(position));
    }

    public class DepenseViewHolder extends RecyclerView.ViewHolder  {
        private final TextView titre;
        private final TextView valeur;
        private Depense currentDepense;
        public LinearLayout viewBackground, viewForeground;
        public CardView cardView_foreground;


        public DepenseViewHolder(final View itemView) {
            super(itemView);
            titre = ((TextView) itemView.findViewById(R.id.title));
            valeur = ((TextView) itemView.findViewById(R.id.value));
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            cardView_foreground = itemView.findViewById(R.id.cardView_foreground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cliquer sur l'item ouvre une Dialog permettant la modification de l'item
                    DialogFragment dialog = new DepenseDialog();
                    dialog = DepenseDialog.creation(1, currentDepense, compteCommun.getDepenses().indexOf(currentDepense));
                    dialog.show(((MainActivity)activity).getSupportFragmentManager(), "DepenseDialog");
                }
            });
        }

        public void bind(Depense depense) {
            currentDepense = depense;
            titre.setText(depense.getNom());
            valeur.setText(NumberFormat.getCurrencyInstance().format(depense.getValeur()));
        }

        public Depense getCurrentDepense(){
            return currentDepense;
        }
    }
}
