package com.prod.artemus.comptecommun;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {

    private final List<Recette> listeRecettes = new ArrayList<Recette>();
    private CompteCommun compteCommun;
    private Context context;
    private final Activity activity;

    public RecetteAdapter(Activity mainActivity) {
        activity = mainActivity;
        context = activity.getApplicationContext();
    }

    public void onArticlesReceived(CompteCommun cc) {
        compteCommun = cc;
        listeRecettes.addAll(compteCommun.getRecettes());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listeRecettes.size();
    }

    public void listClear(){
        listeRecettes.clear();
    }

    public void removeItem(int position) {
        listeRecettes.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Recette recette, int position) {
        listeRecettes.add(position, recette);
        notifyItemInserted(position);
    }

    @Override
    public RecetteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cellule_recette, parent, false);
        return new RecetteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecetteViewHolder holder, int position) {
        ((RecetteViewHolder) holder).bind(listeRecettes.get(position));
    }

    public class RecetteViewHolder extends RecyclerView.ViewHolder  {
        private final TextView titre;
        private final TextView valeur;
        private final ImageView image;
        private Recette currentRecette;
        public LinearLayout viewBackground, viewForeground;
        public CardView cardView_foreground;


        public RecetteViewHolder(final View itemView) {
            super(itemView);
            titre = ((TextView) itemView.findViewById(R.id.title));
            valeur = ((TextView) itemView.findViewById(R.id.value));
            image = ((ImageView) itemView.findViewById(R.id.image));
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            cardView_foreground = itemView.findViewById(R.id.cardView_foreground);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cliquer sur l'item ouvre une Dialog permettant la modification de l'item
                    DialogFragment dialog = new RecetteDialog();
                    dialog = RecetteDialog.creation(1, currentRecette,
                            compteCommun.getRecettes().indexOf(currentRecette),
                            compteCommun.getCommun(),
                            compteCommun.getP1(),
                            compteCommun.getP2());
                    dialog.show(((MainActivity)activity).getSupportFragmentManager(), "RecetteDialog");
                }
            });
        }

        public void bind(Recette recette) {
            currentRecette = recette;
            int idIcone = recette.getPersonne().getIdIcone();
            titre.setText(recette.getNom());
            valeur.setText(NumberFormat.getCurrencyInstance().format(recette.getValeur()));
            switch (idIcone) {
                case 0:
                    image.setImageResource(R.drawable.icone00);
                    break;
                case 1:
                    image.setImageResource(R.drawable.icone01);
                    break;
                case 2:
                    image.setImageResource(R.drawable.icone02);
                    break;
                case 3:
                    image.setImageResource(R.drawable.icone03);
                    break;
                case 4:
                    image.setImageResource(R.drawable.icone04);
                    break;
                case 5:
                    image.setImageResource(R.drawable.icone05);
                    break;
                case 6:
                    image.setImageResource(R.drawable.icone06);
                    break;
                case 7:
                    image.setImageResource(R.drawable.icone07);
                    break;
                case 8:
                    image.setImageResource(R.drawable.icone08);
                    break;
                case 9:
                    image.setImageResource(R.drawable.icone09);
                    break;
                default:
                    image.setImageResource(R.drawable.icone00);
                    break;
            }
        }

        public Recette getCurrentRecette(){
            return currentRecette;
        }
    }
}
