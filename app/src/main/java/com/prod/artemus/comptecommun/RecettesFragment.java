package com.prod.artemus.comptecommun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

public class RecettesFragment extends Fragment {

    public interface RecettesListener {
        public void suppressionRecette(Recette recette);
        public void annulerSuppressionRecette(int indexRecette, Recette recette);
        public void echangeRecettes(Recette recette1, int position1, Recette recette2, int position2);
    }

    private CompteCommun compteCommun;
    private RecetteAdapter adapter;
    private RecettesListener rListener;
    private LinearLayout recettes_linear_layout;
    private TextView somme;

    public RecettesFragment() {
    }

    //Création du fragment avec les données du compteCommun
    public static RecettesFragment creation (CompteCommun cc){
        Bundle args = new Bundle();
        args.putSerializable("compte", cc);
        RecettesFragment fragment = new RecettesFragment();
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
        return inflater.inflate(R.layout.fragment_recettes, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recettes_linear_layout = view.findViewById(R.id.recettes_linear_layout);
        compteCommun = (CompteCommun) getArguments().getSerializable("compte");
        somme = view.findViewById(R.id.header_total);

        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Ajout des articles
        adapter = new RecetteAdapter(getActivity());
        rv.setAdapter(adapter);
        rafraichissement(compteCommun);

        //Gestion du glisser / Supprimer
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                                                                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                                                                        ItemTouchHelper.RIGHT) {

            //Actions exécutées pendant déplacement
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;

            }

            //Actions exécutées à la fin du déplacement
            @Override
            public void onMoved(RecyclerView recyclerView,
                                   RecyclerView.ViewHolder viewHolder,
                                   int fromPos,
                                   RecyclerView.ViewHolder target,
                                   int toPos,
                                   int x,
                                   int y) {
                final Recette recette1 = ((RecetteAdapter.RecetteViewHolder)viewHolder).getCurrentRecette();
                final Recette recette2 = ((RecetteAdapter.RecetteViewHolder)target).getCurrentRecette();
                final int position1 = viewHolder.getAdapterPosition();
                final int position2 = target.getAdapterPosition();

                //Déplacement de la recette
                rListener.echangeRecettes(recette1, position1, recette2, position2);
                adapter.notifyItemMoved(position1, position2);
            }

            //Actions exécutées lors de la suppression
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Sauvegarde de la recette
                final Recette recetteSupprimée = ((RecetteAdapter.RecetteViewHolder)viewHolder).getCurrentRecette();
                final int indexRecetteSupprimée = viewHolder.getAdapterPosition();
                String name = recetteSupprimée.getNom();

                //Suppression de la recette
                rListener.suppressionRecette(recetteSupprimée);
                compteCommun.supprimeRecette(recetteSupprimée);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                rafraichissement(compteCommun);

                //Affichage Snack bar avec option "Annuler"
                Snackbar snackbar = Snackbar
                        .make(recettes_linear_layout, name + " " + getResources().getString(R.string.supprimé), Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.annuler, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Restauration de la recette supprimée
                        rListener.annulerSuppressionRecette(indexRecetteSupprimée, recetteSupprimée);
                        adapter.notifyItemInserted(indexRecetteSupprimée);
                        rafraichissement(compteCommun);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(viewHolder!=null){
                    //coloration de la cellule si elle est déplacée
                    if (actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                        final CardView cardView = ((RecetteAdapter.RecetteViewHolder) viewHolder).cardView_foreground;
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((RecetteAdapter.RecetteViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);

                final CardView cardView = ((RecetteAdapter.RecetteViewHolder) viewHolder).cardView_foreground;
                cardView.setCardBackgroundColor(getResources().getColor(R.color.blanc));
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { //Affichage du background seulement losqu'on déplace à droite.
                    final View foregroundView = ((RecetteAdapter.RecetteViewHolder) viewHolder).viewForeground;
                    getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                            actionState, isCurrentlyActive);
                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { //Affichage du background seulement losqu'on déplace à droite.
                    final View foregroundView = ((RecetteAdapter.RecetteViewHolder) viewHolder).viewForeground;
                    getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                            actionState, isCurrentlyActive);
                }
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);

        //Ajout bouton rajout
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new RecetteDialog();
                dialog = RecetteDialog.creation(0, null, 0, compteCommun.getCommun(), compteCommun.getP1(), compteCommun.getP2());
                dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "RecetteDialog");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérification que l'activité implémente l'interface d'écoute
        try {
            rListener = (RecettesFragment.RecettesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " n'implémente pas RecettesListener");
        }
    }

    public void rafraichissement(CompteCommun cc){
        getActivity().invalidateOptionsMenu();
        compteCommun = cc;
        adapter.listClear();
        adapter.onArticlesReceived(compteCommun);
        somme.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getTotalRecettes()));
    }

}