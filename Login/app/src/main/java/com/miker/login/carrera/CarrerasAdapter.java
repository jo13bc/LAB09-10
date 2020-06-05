package com.miker.login.carrera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.R;
import com.miker.login.curso.Curso;
import com.miker.login.curso.CursosAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class CarrerasAdapter extends RecyclerView.Adapter<CarrerasAdapter.MyViewHolder> implements Filterable {

    private List<Carrera> carreraList;
    private List<Carrera> carreraListFiltered;
    private CarrerasAdapter.CarreraAdapterListener listener;
    private Carrera object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView codigo, nombre;
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            codigo = (TextView) view.findViewById(R.id.codigo);
            nombre = (TextView) view.findViewById(R.id.nombre);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(carreraListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CarrerasAdapter(List<Carrera> carreraList, CarrerasAdapter.CarreraAdapterListener listener) {
        this.carreraList = carreraList;
        this.listener = listener;
        this.carreraListFiltered = carreraList;
    }

    @Override
    public CarrerasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrera_card, parent, false);

        return new CarrerasAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CarrerasAdapter.MyViewHolder holder, final int position) {
        final Carrera carrera = carreraListFiltered.get(position);
        holder.codigo.setText(carrera.getCodigo());
        holder.nombre.setText(carrera.getNombre());
    }

    @Override
    public int getItemCount() {
        return carreraListFiltered.size();
    }

    public void removeItem(int position) {
        object = carreraListFiltered.remove(position);
        Iterator<Carrera> iter = carreraList.iterator();
        while (iter.hasNext()) {
            Carrera aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (carreraListFiltered.size() == carreraList.size()) {
            carreraListFiltered.add(position, object);
        } else {
            carreraListFiltered.add(position, object);
            carreraList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Carrera getSwipedItem(int index) {
        if (this.carreraList.size() == this.carreraListFiltered.size()) { //not filtered yet
            return carreraList.get(index);
        } else {
            return carreraListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (carreraList.size() == carreraListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraListFiltered, i, i - 1);
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
                    carreraListFiltered = carreraList;
                } else {
                    List<Carrera> filteredList = new ArrayList<>();
                    for (Carrera row : carreraList) {
                        // filter use two parameters
                        if (row.getCodigo().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    carreraListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = carreraListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                carreraListFiltered = (ArrayList<Carrera>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CarreraAdapterListener {
        void onSelected(Carrera carrera);
    }
}

