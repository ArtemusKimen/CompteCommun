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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

public class DepensesFragment extends Fragment {

    public interface DepensesListener {
        public void suppressionDepense(Depense depense);
        public void annulerSuppressionDepense(int indexDepense, Depense depense);
        public void echangeDepenses(Depense depense1, int position1, Depense depense2, int position2);
    }

    private CompteCommun compteCommun;
    private DepenseAdapter adapter;
    private DepensesListener dListener;
    private LinearLayout depenses_linear_layout;
    private TextView somme;
    private RecyclerView rv;

    public DepensesFragment() {
    }

    public static DepensesFragment creation (CompteCommun cc){
        Bundle args = new Bundle();
        args.putSerializable("compte", cc);
        DepensesFragment fragment = new DepensesFragment();
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
        return inflater.inflate(R.layout.fragment_depenses, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        depenses_linear_layout = view.findViewById(R.id.depenses_linear_layout);
        compteCommun = (CompteCommun) getArguments().getSerializable("compte");
        somme = view.findViewById(R.id.header_total);

        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Ajout des articles
        adapter = new DepenseAdapter(getActivity());
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
                final Depense depense1 = ((DepenseAdapter.DepenseViewHolder)viewHolder).getCurrentDepense();
                final Depense depense2 = ((DepenseAdapter.DepenseViewHolder)target).getCurrentDepense();
                final int position1 = viewHolder.getAdapterPosition();
                final int position2 = target.getAdapterPosition();

                //Déplacement de la dépense
                dListener.echangeDepenses(depense1, position1, depense2, position2);
                adapter.notifyItemMoved(position1, position2);
            }

            //Actions exécutées lors de la suppression
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Sauvegarde de la depense
                final Depense depenseSupprimée = ((DepenseAdapter.DepenseViewHolder)viewHolder).getCurrentDepense();
                final int indexDepenseSupprimée = viewHolder.getAdapterPosition();
                String name = depenseSupprimée.getNom();

                //Suppression de la depense
                dListener.suppressionDepense(depenseSupprimée);
                compteCommun.supprimeDepense(depenseSupprimée);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                rafraichissement(compteCommun);

                //Affichage Snack bar avec option "Annuler"
                Snackbar snackbar = Snackbar
                        .make(depenses_linear_layout, name + " " + getResources().getString(R.string.supprimé), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.setAction(R.string.annuler, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Restauration de la dépense supprimée
                        dListener.annulerSuppressionDepense(indexDepenseSupprimée, depenseSupprimée);
                        adapter.notifyItemInserted(indexDepenseSupprimée);
                        rafraichissement(compteCommun);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    //coloration de la cellule si elle est déplacée
                    if (actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                        final CardView cardView = ((DepenseAdapter.DepenseViewHolder) viewHolder).cardView_foreground;
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((DepenseAdapter.DepenseViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().clearView(foregroundView);

                final CardView cardView = ((DepenseAdapter.DepenseViewHolder) viewHolder).cardView_foreground;
                cardView.setCardBackgroundColor(getResources().getColor(R.color.blanc));
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { //Affichage du background seulement losqu'on déplace à droite.
                    final View foregroundView = ((DepenseAdapter.DepenseViewHolder) viewHolder).viewForeground;
                    getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                            actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) { //Affichage du background seulement losqu'on déplace à droite.
                    final View foregroundView = ((DepenseAdapter.DepenseViewHolder) viewHolder).viewForeground;

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
                DialogFragment dialog = new DepenseDialog();
                dialog = DepenseDialog.creation(0, null, 0);
                dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "DepenseDialog");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérification que l'activité implémente l'interface d'écoute
        try {
            dListener = (DepensesFragment.DepensesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " n'implémente pas DepensesListener");
        }
    }

    public void rafraichissement(CompteCommun cc){
        getActivity().invalidateOptionsMenu();
        compteCommun = cc;
        adapter.listClear();
        adapter.onArticlesReceived(compteCommun);
        somme.setText(NumberFormat.getCurrencyInstance().format(compteCommun.getTotalDepenses()));
        rv.smoothScrollToPosition(adapter.getItemCount());
    }

}