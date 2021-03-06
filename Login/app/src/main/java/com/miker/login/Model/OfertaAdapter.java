package com.miker.login.Model;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.Logic.Oferta;
import com.miker.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class OfertaAdapter extends RecyclerView.Adapter<OfertaAdapter.MyViewHolder> implements Filterable {

    private List<Oferta> cursoList;
    private List<Oferta> cursoListFiltered;
    private OfertaAdapterListener listener;
    private Oferta object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, creditos;
        public RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            creditos = (TextView) view.findViewById(R.id.creditos);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(cursoListFiltered.get(getAdapterPosition()), nombre, creditos, viewForeground);
                }
            });
        }
    }

    public OfertaAdapter(List<Oferta> cursoList, OfertaAdapterListener listener) {
        this.cursoList = cursoList;
        this.listener = listener;
        this.cursoListFiltered = cursoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_oferta, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Oferta oferta = cursoListFiltered.get(position);
        holder.nombre.setText(oferta.getCurso().getDescripcion());
        holder.creditos.setText(String.valueOf(oferta.getCurso().getCreditos()));
        if (oferta.isSelected()) {
            holder.nombre.setTextColor(Color.WHITE);
            holder.creditos.setTextColor(Color.WHITE);
            holder.viewForeground.setBackgroundColor(Color.GRAY);
            holder.viewForeground.setVisibility(View.GONE);
        } else {
            holder.nombre.setTextColor(Color.BLACK);
            holder.creditos.setTextColor(Color.BLACK);
            holder.viewForeground.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return cursoListFiltered.size();
    }

    public void removeItem(int position) {
        object = cursoListFiltered.remove(position);
        Iterator<Oferta> iter = cursoList.iterator();
        while (iter.hasNext()) {
            Oferta aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (cursoListFiltered.size() == cursoList.size()) {
            cursoListFiltered.add(position, object);
        } else {
            cursoListFiltered.add(position, object);
            cursoList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Oferta getSwipedItem(int index) {
        if (this.cursoList.size() == this.cursoListFiltered.size()) { //not filtered yet
            return cursoList.get(index);
        } else {
            return cursoListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (cursoList.size() == cursoListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cursoListFiltered = cursoList;
                } else {
                    List<Oferta> filteredList = new ArrayList<>();
                    for (Oferta row : cursoList) {
                        // filter use two parameters
                        if (
                                row.getCurso().getDescripcion().toLowerCase().contains(charString.toLowerCase())
                                        || String.valueOf(row.getCurso().getCreditos()).contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    cursoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cursoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cursoListFiltered = (ArrayList<Oferta>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OfertaAdapterListener {
        void onSelected(Oferta curso, TextView nombre, TextView creditos, RelativeLayout viewForeground);
    }
}

